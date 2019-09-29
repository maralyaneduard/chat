package com.test.chat.service.impl;

import com.test.chat.entity.Role;
import com.test.chat.entity.User;
import com.test.chat.enumeration.RoleType;
import com.test.chat.repository.RoleRepository;
import com.test.chat.repository.UserRepository;
import com.test.chat.service.FileService;
import com.test.chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private FileService fileService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, FileService fileService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.fileService = fileService;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(int id) {
        return userRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(1);
        Role r = new Role();
        r.setRole(RoleType.ADMIN);
        r.setId(1L);
        user.setRole(r);
        user.setPicName("");

        logger.info("Save user [" + user + "]");
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void saveUser(User user, MultipartFile file) throws IOException {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(1);
        String picName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        user.setPicName(picName);
        this.userRepository.save(user);

        logger.info("Save user [" + user + "] with picture name[" + picName + "]");
        this.fileService.uploadFile(file, picName);
    }

    @Override
    @Transactional
    public void updateUser(User user, MultipartFile file) throws IOException {
        logger.info("Updating user with id[" + user.getId() + "]");

        User userToUpdate = this.findById(user.getId());
        if(userToUpdate != null){
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setRole(user.getRole());
            userToUpdate.setPassword(encoder.encode(user.getPassword()));
            String pictureName = userToUpdate.getPicName();
            if(!pictureName.contains(Objects.requireNonNull(file.getOriginalFilename()))){
                String picName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                this.fileService.removeFile(pictureName);
                userToUpdate.setPicName(picName);
                this.fileService.uploadFile(file, picName);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(int userId) throws IOException {
        logger.info("Deleting user with id[" + userId + "]");

        User userToDelete = this.findById(userId);
        if(userToDelete != null){
            this.fileService.removeFile(userToDelete.getPicName());
            this.userRepository.delete(userToDelete);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
