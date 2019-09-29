package com.test.chat.controller;

import com.test.chat.dto.ChatUserDto;
import com.test.chat.dto.MessageDto;
import com.test.chat.dto.UserRoomDto;
import com.test.chat.entity.Message;
import com.test.chat.entity.Room;
import com.test.chat.entity.User;
import com.test.chat.enumeration.MessageType;
import com.test.chat.mapper.ChatUserMapper;
import com.test.chat.mapper.MessageMapper;
import com.test.chat.service.ChatService;
import com.test.chat.service.MessageService;
import com.test.chat.service.RoomService;
import com.test.chat.service.UserService;
import com.test.chat.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

/**
 * This controller is used for managing chat,
 * e.g. adding and removing users, sending messages
 */
@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private MessageService messageService;
    private RoomService roomService;
    private ChatService chatService;
    private UserService userService;
    private MessageMapper messageMapper;
    private ChatUserMapper chatUserMapper;

    /**
     * Regular constructor
     *
     * @param messageService service to deal with messages
     * @param roomService service to deal with rooms
     * @param chatService service to deal with chat logic
     * @param userService service to deal with users
     * @param messageMapper converter for Message objects
     * @param chatUserMapper converter for ChatUser objects
     */
    @Autowired
    public ChatController(MessageService messageService, RoomService roomService, ChatService chatService,
                          UserService userService, MessageMapper messageMapper, ChatUserMapper chatUserMapper) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.chatService = chatService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.chatUserMapper = chatUserMapper;
    }

    /**
     * Display list of chat rooms
     *
     * @return ModelAndView object with all rooms
     */
    @GetMapping("/chat/home")
    public ModelAndView roomList(){
        logger.info("Entering chat");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/chat/chat-home");

        List<Room> rooms = roomService.findAll();
        modelAndView.addObject("rooms",rooms);

        return modelAndView;
    }

    /**
     * Display room by given id
     *
     * @param  authentication authentication object used to get usernamne
     * @param  roomId id of room to search for
     *
     * @return ModelAndView object with room
     */
    @GetMapping("/chat/home/{roomId}")
    public ModelAndView selectRoom(Authentication authentication, @PathVariable int roomId) {
        logger.info("Entering room with id[" + roomId + "]");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userService.findUserByEmail(userDetails.getUsername());

        List<Room> rooms = roomService.findAll();
        List<Message> messages = this.messageService.getAllMessagesByRoomId(roomId);
        List<MessageDto> messageDtos = messageMapper.toMessageDto(messages);

        Room room = this.roomService.findById(roomId);
        Set<ChatUserDto> chatUserDtos = this.chatUserMapper.toChatUserDto(room.getUsers());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("chatUserDtos", chatUserDtos);
        modelAndView.addObject("messageDtos", messageDtos);
        modelAndView.addObject("rooms",rooms);
        modelAndView.addObject("selectedRoomId",roomId);
        modelAndView.addObject("userName",currentUser.getName());
        modelAndView.setViewName("/chat/chat-home");
        return modelAndView;
    }

    /**
     * Clear messages and users from room by given id
     *
     * @param  roomId id of room to search for
     *
     * @return ModelAndView object of chat home page
     */
    @GetMapping("/chat/clear/{roomId}")
    public ModelAndView clearRoom(@PathVariable int roomId) {
        logger.info("Clear all users and message from room with id[" + roomId + "]");

        ModelAndView modelAndView = new ModelAndView();
        this.roomService.clearRoom(roomId);
        modelAndView.setViewName("redirect:/chat/home");
        return modelAndView;
    }

    /**
     * Displays chat manage page
     *
     * @return ModelAndView object of chat management page
     */
    @GetMapping("/chat/manage")
    public ModelAndView getChatManage() {
        logger.info("Entering chat manage page");

        ModelAndView modelAndView = new ModelAndView();
        List<Room> rooms = this.roomService.findAll();
        modelAndView.setViewName("/admin/chat/chat-user-management");
        modelAndView.addObject("userRooms", UserRoomDto.getDtoFromList(rooms));
        return modelAndView;
    }

    /**
     * Remove given user from given room
     *
     * @param  roomId id of room to search for\
     * @param  userId id of user to be removed
     *
     * @return ModelAndView object with chat manage page
     */
    @GetMapping("/chat/user/remove/{roomId}/{userId}")
    public ModelAndView removeUserFromRoom(@PathVariable int roomId, @PathVariable int userId) {
        logger.info("Remove user with id[" + userId + "] from room with id[" + roomId + "]");

        ModelAndView modelAndView = new ModelAndView();
        this.chatService.removeUserFromRoom(roomId, this.userService.findById(userId).getEmail(), MessageType.KICK);
        modelAndView.setViewName("redirect:/chat/manage");
        return modelAndView;
    }

    /**
     * Sending message to given room
     *
     * @param  authentication authentication object used to get usernamne
     * @param  roomId id of room to search for
     * @param  chatMessage message to be sent
     */
    @MessageMapping("/chat/sendMessage/{roomId}")
    public void sendMessage(Authentication authentication, @Payload MessageDto chatMessage,
                                  @DestinationVariable String roomId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        logger.info("Sending message [" + chatMessage +"] from user with email[" + userDetails.getUsername() + "] to room with id[" + roomId + "]");

        this.chatService.sendMessage(Integer.parseInt(roomId), userDetails.getUsername(), chatMessage.getText());
    }

    /**
     * Add user to given room
     *
     * @param  authentication authentication object used to get usernamne
     * @param  roomId id of room to search for
     * @param  chatMessage message to be sent
     */
    @MessageMapping("/chat/join/{roomId}")
    public void addUser(Authentication authentication, @Payload MessageDto chatMessage,
                        @DestinationVariable String roomId ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        logger.info("Adding user with email[" + userDetails.getUsername() + "] to room with id[" + roomId + "]");

        this.chatService.addUserToRoom(Integer.parseInt(roomId), userDetails.getUsername());
    }
}
