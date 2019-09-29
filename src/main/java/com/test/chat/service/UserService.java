package com.test.chat.service;

import com.test.chat.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service to manage users
 */
public interface UserService {
    /**
     * Returns user with given id
     *
     * @param id id of the user to search for
     * @return User with given id
     */
    User findById(int id);

    /**
     * Returns user with given email
     *
     * @param email email of the user to search for
     * @return User with given email
     */
    User findUserByEmail(String email);

    /**
     * Adds new user with given file as profile picture
     *
     * @param user user to add
     * @param file file to use as profile picture
     * @throws IOException throws exception when file cannot be added
     */
    void saveUser(User user, MultipartFile file) throws IOException;

    /**
     * Adds new user
     * @param user user to add
     */
    void saveUser(User user);

    /**
     * Updates given user and file
     * @param user user to be updated
     * @param file file to be set as new profile pictures
     * @throws IOException throws exception when file cannot be added
     */
    void updateUser(User user, MultipartFile file) throws IOException;

    /**
     * Deletes user with given id
     *
     * @param userId id of the user to delete
     * @throws IOException throws exception when file cannot be deleted
     */
    void deleteUser(int userId) throws IOException;

    /**
     * Returns all users
     * @return List with all users
     */
    List<User> findAll();
}
