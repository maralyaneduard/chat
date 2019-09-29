package com.test.chat.repository;

import com.test.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to manage Room entities
 */
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findRoomByName(String name);

    List<Room> findByUsers_Email(String userName);
}
