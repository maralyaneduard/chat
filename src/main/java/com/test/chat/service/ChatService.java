package com.test.chat.service;

import com.test.chat.enumeration.MessageType;

/**
 * Service to manage Chat
 */
public interface ChatService {
    /**
     * Adds user with given email
     * to room with given id
     *
     * @param roomId id of room to be added in
     * @param email email of the user to be added
     */
    void addUserToRoom(int roomId, String email);

    /**
     * Send message from user with given email
     * to room with given id
     *
     * @param roomId id of room to add a message
     * @param email email of user who sends message
     * @param text content of the message
     */
    void sendMessage(int roomId, String email, String text);

    /**
     * Remove user with given email
     * from room with given id
     *
     * @param roomId id of room to remove from
     * @param email email of user to remove
     */
    void removeUserFromRoom(int roomId, String email);

    /**
     * Remove user with given email
     * from room with given id
     *
     * @param roomId id of room to remove from
     * @param email email of user to remove
     * @param type type of the message(KICK, LEAVE)
     */
    void removeUserFromRoom(int roomId, String email, MessageType type);

    /**
     * Remove user with given email
     * from all rooms
     *
     * @param email email of user to remove
     */
    void removeUserFromRoom(String email);
}
