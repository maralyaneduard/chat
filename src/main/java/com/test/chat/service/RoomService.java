package com.test.chat.service;

import com.test.chat.entity.Room;

import java.util.List;

/**
 * Service to manage rooms
 */
public interface RoomService {
    /**
     * Returns room by given id
     * @param id if of the room to search for
     * @return Room with given id
     */
    Room findById(int id);

    /**
     * Returns room by given name
     * @param name name of the room to search for
     * @return Room with given name
     */
    Room findByName(String name);

    /**
     * Adds new room
     * @param room room to be added
     */
    void saveRoom(Room room);

    /**
     * Updates room
     * @param room room to be updated
     */
    void updateRoom(Room room);

    /**
     * Deletes room with given id
     * @param roomId id of room to be deleted
     */
    void deleteRoom(int roomId);

    /**
     * Returns all rooms
     * @return List with all rooms found
     */
    List<Room> findAll();

    /**
     * Remove users and messages from
     * the room with given id
     *
     * @param roomId id of the room to clear
     */
    void clearRoom(int roomId);

    /**
     * Remove user with given id from
     * room with given id
     *
     * @param roomId id of the room ro remove from
     * @param userId id of the user to remove from
     */
    void removeUser(int roomId, int userId);
}
