package com.test.chat.service;

import com.test.chat.entity.Message;

import java.util.List;

/**
 * Service to manage messages
 */
public interface MessageService {
    /**
     * Returns message with given id
     * @param id id of message to search for
     * @return
     */
    Message findById(int id);

    /**
     * Saves message
     * @param message message to be saved
     */
    void saveMessage(Message message);

    /**
     * Updates message
     * @param message message to be updated
     */
    void updateMessage(Message message);

    /**
     * Deletes message with given id
     * @param messageId id of the message to delete
     */
    void deleteMessage(int messageId);

    /**
     * Returns all messages of the room
     * @param roomId id of the room
     * @return List with all messages found in the room
     */
    List<Message> getAllMessagesByRoomId(int roomId);

    /**
     * Removes all messages with given room
     * @param roomId id of the room
     */
    void removeAllMessagesByRoomId(int roomId);
}
