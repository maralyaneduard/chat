package com.test.chat.controller;

import com.test.chat.dto.RoomDto;
import com.test.chat.entity.Room;
import com.test.chat.mapper.RoomMapper;
import com.test.chat.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller is used for managing rooms,
 * e.g. adding , updating and removing rooms
 */
@Controller
public class RoomController {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private RoomService roomService;
    private RoomMapper roomMapper;

    /**
     * Regular constructor
     *
     * @param roomService service to deal with rooms
     * @param roomMapper converter for Room objects
     */
    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    /**
     * Display list rooms
     *
     * @return ModelAndView object with all rooms
     */
    @GetMapping("/rooms")
    public ModelAndView roomList(){
        logger.info("Entering room list page");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/rooms/room-list");

        List<Room> rooms = roomService.findAll();
        modelAndView.addObject("rooms",rooms);

        return modelAndView;
    }

    /**
     * Display page to add room
     *
     * @return ModelAndView object with add room page
     */
    @GetMapping("/rooms/add")
    public ModelAndView getAddRoom(){
        logger.info("Entering room add page");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/rooms/room-create");
        RoomDto roomDto = new RoomDto();
        modelAndView.addObject("roomDto", roomDto);

        return modelAndView;
    }

    /**
     * Add new room
     *
     * @param  roomDto room object to add
     * @param  bindingResult object to manage bindings
     *
     * @return ModelAndView object with rooms
     */
    @PostMapping("/rooms/add")
    public ModelAndView postAddRoom(@Valid RoomDto roomDto, BindingResult bindingResult) {
        logger.info("Adding room["+ roomDto +"]");

        ModelAndView modelAndView = new ModelAndView();

        Room room = this.roomService.findByName(roomDto.getName());
        if(room != null){
            bindingResult.rejectValue("name","error.room","There is already a room added with the provided name");
            logger.error("Wrong name[" + roomDto.getName() + "]");
        }

        if(bindingResult.hasErrors()){
            modelAndView.setViewName("/admin/rooms/room-create");
        } else {
            Room newRoom = this.roomMapper.toRoom(roomDto);
            this.roomService.saveRoom(newRoom);
            modelAndView.setViewName("redirect:/rooms");
        }

        return modelAndView;
    }

    /**
     * Show update room page
     *
     * @param  roomId room object to update
     *
     * @return ModelAndView object with given room
     */
    @GetMapping("/rooms/update/{roomId}")
    public ModelAndView getUpdateRoom(@PathVariable int roomId){
        logger.info("Entering room update page for room with id[" + roomId + "]");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/rooms/room-update");
        Room room = this.roomService.findById(roomId);
        RoomDto roomDto = this.roomMapper.toRoomDto(room);
        modelAndView.addObject("room", roomDto);

        return modelAndView;
    }

    /**
     * Update room
     *
     * @param  roomDto room object to update
     * @param  bindingResult object to manage bindings
     *
     * @return ModelAndView object with rooms
     */
    @PostMapping("/rooms/update")
    public ModelAndView postUpdateRoom(@Valid RoomDto roomDto, BindingResult bindingResult){
        logger.info("Updating room["+ roomDto +"]");

        ModelAndView modelAndView = new ModelAndView();

        Room roomToUpdate = this.roomService.findById(roomDto.getId());
        if(roomToUpdate == null){
            bindingResult.rejectValue("id","error.room","No room found with id");
            modelAndView.setViewName("/admin/rooms/room-update");
            logger.error("Wrong id[" + roomDto.getId() + "]");
        } else {
            Room room = this.roomMapper.toRoom(roomDto);
            this.roomService.updateRoom(room);
            modelAndView.setViewName("redirect:/rooms");
        }
        return modelAndView;
    }

    /**
     * Delete room with given id
     *
     * @param  roomId id of room to delete
     *
     * @return ModelAndView object with rooms
     */
    @GetMapping("/rooms/delete/{roomId}")
    public ModelAndView deleteRoom(@PathVariable int roomId){
        logger.info("Deleting room with id["+ roomId +"]");

        ModelAndView modelAndView = new ModelAndView();

        this.roomService.deleteRoom(roomId);
        modelAndView.setViewName("redirect:/rooms");

        return modelAndView;
    }
}
