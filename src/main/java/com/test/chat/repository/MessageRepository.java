package com.test.chat.repository;

import com.test.chat.entity.Message;
import com.test.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to manage Message entities
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByRoom(Room room);
    void removeByRoom(Room room);
}
