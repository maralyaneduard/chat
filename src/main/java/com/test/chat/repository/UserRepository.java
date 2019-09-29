package com.test.chat.repository;

import com.test.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage User entities
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);
}
