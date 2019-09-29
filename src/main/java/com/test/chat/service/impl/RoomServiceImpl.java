package com.test.chat.service.impl;

import com.test.chat.entity.Room;
import com.test.chat.entity.User;
import com.test.chat.repository.MessageRepository;
import com.test.chat.repository.RoomRepository;
import com.test.chat.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private RoomRepository roomRepository;
    private MessageRepository messageRepository;

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, MessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Room findById(int id) {
        logger.info("Find room with id[" + id + "]");
        return this.roomRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Room findByName(String name) {
        logger.info("Find room with name[" + name + "]");

        return this.roomRepository.findRoomByName(name);
    }

    @Override
    @Transactional
    public void saveRoom(Room room) {
        logger.info("Adding new room[" + room + "]");
        this.roomRepository.save(room);
    }

    @Override
    @Transactional
    public void updateRoom(Room room) {
        logger.info("Updating room with id[" + room.getId() + "]");

        Room roomToUpdate = this.findById(room.getId());
        if(roomToUpdate != null){
            roomToUpdate.setName(room.getName());
        }
    }

    @Override
    @Transactional
    public void deleteRoom(int roomId) {
        logger.info("Deleting room with id[" + roomId + "]");

        this.roomRepository.deleteById(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return this.roomRepository.findAll();
    }

    @Override
    @Transactional
    public void clearRoom(int roomId) {
        logger.info("Removing all users and messages from room with id[" + roomId + "]");
        Room room = this.findById(roomId);
        if(room != null){
            room.setUsers(new HashSet<User>());
            this.messageRepository.removeByRoom(room);
        }
    }

    @Override
    @Transactional
    public void removeUser(int roomId, int userId) {
        logger.info("Removing user with id[" + userId + "] from room with id[" + roomId + "]");

        Room room = this.findById(roomId);
        Set<User> userSet = new HashSet<>();
        if(room != null){
            Set<User> roomUsers = room.getUsers();
            for(User user : roomUsers){
                if(user.getId() != userId){
                    userSet.add(user);
                }
            }

            room.setUsers(userSet);
        }
    }
}
