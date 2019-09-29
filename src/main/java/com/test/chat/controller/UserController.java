package com.test.chat.controller;

import com.test.chat.dto.UserDto;
import com.test.chat.entity.Role;
import com.test.chat.entity.User;
import com.test.chat.mapper.UserMapper;
import com.test.chat.service.RoleService;
import com.test.chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * This controller is used for managing users,
 * e.g. adding and removing users, getting profile picture
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${user.upload.path}")
    private String imageUploadPath;

    private UserService userService;
    private RoleService roleService;
    private UserMapper userMapper;

    /**
     * Regular constructor
     *
     * @param userService service to deal with users
     * @param roleService service to deal with rols
     * @param userMapper converter for User objects
     */
    @Autowired
    public UserController(UserService userService, RoleService roleService, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    /**
     * Display users page
     *
     * @return ModelAndView object with users
     */
    @GetMapping("/users")
    public ModelAndView userList(){
        logger.info("Entering user list page");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/users/user-list");

        List<User> users = this.userService.findAll();
        List<UserDto> userDtos = this.userMapper.toUserDTOs(users);
        modelAndView.addObject("users",userDtos);

        return modelAndView;
    }

    /**
     * Display add user page
     *
     * @return ModelAndView object with roles to be selected
     */
    @GetMapping("/users/add")
    public ModelAndView getAddUser(){
        logger.info("Entering add user page");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/users/user-create");
        UserDto userDto = new UserDto();
        List<Role> roles = this.roleService.getAllRoles();

        modelAndView.addObject("userDto", userDto);
        modelAndView.addObject("roles", roles);

        return modelAndView;
    }

    /**
     * Add new user with given info and file as profile picture
     *
     * @param  userDto user to add
     * @param  file profile picture of the user
     * @param  bindingResult object to manage bindings
     *
     * @return ModelAndView object with users
     */
    @PostMapping("/users/add")
    public ModelAndView postAddUser(@Valid UserDto userDto, @RequestParam(value = "image") MultipartFile file, BindingResult bindingResult) throws IOException {
        logger.info("Adding user["+ userDto +"]");

        ModelAndView modelAndView = new ModelAndView();

        User userExists = this.userService.findUserByEmail(userDto.getEmail());
        if(userExists != null){
            bindingResult.rejectValue("email","error.user","There is already a user registered with the provided email");
            logger.error("User with provided email[" + userDto.getEmail() + "] already exists");
        }

        if(bindingResult.hasErrors()){
            modelAndView.setViewName("/admin/users/user-create");
        } else {
            User user = this.userMapper.toUser(userDto);
            this.userService.saveUser(user, file);
            modelAndView.setViewName("redirect:/users");
        }

        return modelAndView;
    }

    /**
     * Update given user
     *
     * @param  userId id of user to update
     *
     * @return ModelAndView object with roles
     */
    @GetMapping("/users/update/{userId}")
    public ModelAndView getUpdateUser(@PathVariable int userId){
        logger.info("Entering user update page for user with id[" + userId + "]");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/users/user-update");
        User user = this.userService.findById(userId);
        UserDto userDto = this.userMapper.toUserDto(user);
        List<Role> roles = this.roleService.getAllRoles();
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("roles", roles);

        return modelAndView;
    }

    /**
     * Update user with given info and file as profile picture
     *
     * @param  userDto user to update
     * @param  file profile picture of the user
     * @param  bindingResult object to manage bindings
     *
     * @return ModelAndView object with users
     */
    @PostMapping("/users/update")
    public ModelAndView postUpdateUser(@Valid UserDto userDto, @NotNull @RequestParam(value = "image") MultipartFile file, BindingResult bindingResult) throws IOException {
        logger.info("Updating user["+ userDto +"]");

        ModelAndView modelAndView = new ModelAndView();

        User userToUodate = this.userService.findById(userDto.getId());
        if(userToUodate == null){
            bindingResult.rejectValue("id","error.user","No user found with id");
            modelAndView.setViewName("/admin/users/user-update");
            logger.error("Wrong id[" + userDto.getId() + "]");
        } else {
            User user = this.userMapper.toUser(userDto);
            this.userService.updateUser(user, file);
            modelAndView.setViewName("redirect:/users");
        }
        return modelAndView;
    }

    /**
     * Delete given user
     *
     * @param  userId id of user to delete
     *
     * @return ModelAndView object with users
     */
    @GetMapping("/users/delete/{userId}")
    public ModelAndView deleteUser(@PathVariable int userId) throws IOException {
        logger.info("Deleting user with id["+ userId +"]");

        ModelAndView modelAndView = new ModelAndView();

        this.userService.deleteUser(userId);
        modelAndView.setViewName("redirect:/users");

        return modelAndView;
    }

    /**
     * Display profile page of the user
     *
     * @param  userId id of user to display
     *
     * @return ModelAndView object with user info
     */
    @GetMapping("/users/profile/{userId}")
    public ModelAndView userProfile(@PathVariable int userId){
        logger.info("Entering user profile page with id["+ userId +"]");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/admin/users/user-profile");
        User user = this.userService.findById(userId);
        UserDto userDto = this.userMapper.toUserDto(user);
        modelAndView.addObject("user", userDto);

        return modelAndView;
    }

    /**
     * Display given image
     *
     * @param  imageName name of the image to display
     *
     * @return byte array with the payload of image data
     */
    @RequestMapping(value = "users/profile/image/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
        logger.info("Getting image with name["+ imageName +"]");

        File serverFile = new File( imageUploadPath + imageName);

        return Files.readAllBytes(serverFile.toPath());
    }
}
