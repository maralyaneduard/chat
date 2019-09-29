package com.test.chat.service.impl;

import com.test.chat.entity.Message;
import com.test.chat.repository.MessageRepository;
import com.test.chat.service.MessageService;
import com.test.chat.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageRepository messageRepository;
    private RoomService roomService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, RoomService roomService) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
    }

    @Override
    public Message findById(int id) {
        return this.messageRepository.getOne(id);
    }

    @Override
    public void saveMessage(Message message) {
        logger.info("Adding message[" + message + "]");

        this.messageRepository.save(message);
    }

    @Override
    public void updateMessage(Message message) {
        logger.info("Updating message[" + message + "]");

        Message messageToUpdate = this.findById(message.getId());
        if(messageToUpdate != null){
            messageToUpdate.setText(message.getText());
        }
    }

    @Override
    public void deleteMessage(int messageId) {
        logger.info("Deleting message with id[" + messageId + "]");

        this.messageRepository.deleteById(messageId);
    }

    @Override
    public List<Message> getAllMessagesByRoomId(int roomId) {
        logger.info("Getting all messages for room with id[" + roomId + "]");

        return this.messageRepository.findAllByRoom(this.roomService.findById(roomId));
    }

    @Override
    public void removeAllMessagesByRoomId(int roomId) {
        logger.info("Removing all messages for room with id[" + roomId + "]");

        this.messageRepository.removeByRoom(this.roomService.findById(roomId));
    }
}
