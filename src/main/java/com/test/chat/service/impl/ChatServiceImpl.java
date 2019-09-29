package com.test.chat.service.impl;

import com.test.chat.controller.RoomController;
import com.test.chat.dto.MessageDto;
import com.test.chat.entity.Message;
import com.test.chat.entity.Room;
import com.test.chat.entity.User;
import com.test.chat.enumeration.MessageType;
import com.test.chat.exception.ChatException;
import com.test.chat.repository.RoomRepository;
import com.test.chat.repository.UserRepository;
import com.test.chat.service.ChatService;
import com.test.chat.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("chatService")
public class ChatServiceImpl implements ChatService {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private static String[] BAD_WORDS =  {"badword1", "badword2", "bad", "word"};

    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private SimpMessageSendingOperations messagingTemplate;
    private MessageService messageService;

    @Autowired
    public ChatServiceImpl(RoomRepository roomRepository, UserRepository userRepository,
                           SimpMessageSendingOperations messagingTemplate, MessageService messageService) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @Transactional
    @Override
    public void addUserToRoom(int roomId, String email) {
        logger.info("Adding user with email[" + email + "] to room with id[" +  roomId + "]");
        User user = this.userRepository.findUserByEmail(email);
        Room room = this.roomRepository.getOne(roomId);

        if(user == null || room == null){
            logger.error("Username or room not found");
            throw new ChatException(roomId, email, "Cant add user to room");
        }

        room.getUsers().add(user);

        sendMessage(roomId, user.getName(), MessageType.JOIN);
    }

    @Transactional
    @Override
    public void sendMessage(int roomId, String email, String text) {
        logger.info("Sending message with text[" + text + "] from user with email[" + email + "] to room with id[" +  roomId + "]");

        User user = this.userRepository.findUserByEmail(email);
        Room room = this.roomRepository.getOne(roomId);

        if(user == null || room == null){
            logger.error("Username or room not found");
            throw new ChatException(roomId, email, "Cant send message");
        }

        MessageType type = MessageType.CHAT;

        if(checkBadWord(text)){
            type = MessageType.KICK;
            text = null;
        } else {
            Message message = new Message();
            message.setText(text);
            message.setUser(user);
            message.setRoom(room);
            this.messageService.saveMessage(message);
        }

        sendMessage(roomId, user.getName(), type, text);
    }

    @Transactional
    @Override
    public void removeUserFromRoom(int roomId, String email) {
        this.removeUserFromRoom(roomId, email, MessageType.LEAVE);
    }

    @Transactional
    @Override
    public void removeUserFromRoom(int roomId, String email, MessageType type) {
        logger.info("Remove user with email[" + email + "] from room with id[" +  roomId + "]");
        User user = this.userRepository.findUserByEmail(email);
        Room room = this.roomRepository.getOne(roomId);

        if(user == null || room == null){
            logger.error("Username or room not found");
            throw new ChatException(roomId, email, "Cant remove user from room");
        }

        Set<User> userSet = new HashSet<>();
        Set<User> usersOfRoom = room.getUsers();

        for(User u : usersOfRoom){
            if(!u.equals(user)){
                userSet.add(u);
            }
        }
        room.setUsers(userSet);

        sendMessage(roomId, user.getName(), type);
    }

    @Transactional
    @Override
    public void removeUserFromRoom(String email) {
        logger.info("Remove user with email[" + email + "] from all rooms");
        User user = this.userRepository.findUserByEmail(email);
        List<Room> roomsOfUser = this.roomRepository.findByUsers_Email(email);

        if(user == null || roomsOfUser.isEmpty()){
            logger.error("Username or room not found");
            throw new ChatException(null, email, "Cant remove user from rooms");
        }

        for(Room room : roomsOfUser){
            Set<User> usersOfRoom = room.getUsers();
            Set<User> userSet = new HashSet<>();

            for(User u : usersOfRoom){
                if(!u.equals(user)){
                    userSet.add(u);
                }
            }
            room.setUsers(userSet);

            sendMessage(room.getId(), user.getName(), MessageType.LEAVE);
        }
    }

    private void sendMessage(int roomId, String userName, MessageType type){
        sendMessage(roomId,userName,type,null);
    }

    private void sendMessage(int roomId, String userName, MessageType type, String messageText){
        MessageDto message = new MessageDto();
        message.setMessageType(type);
        message.setUserName(userName);
        message.setText(messageText);
        this.messagingTemplate.convertAndSend("/topic/public/" + roomId, message);
    }

    private boolean checkBadWord(String message){
        boolean result = false;

        for (String BAD_WORD : BAD_WORDS) {
            if (message.contains(BAD_WORD)) {
                return true;
            }
        }

        return result;
    }
}
