package com.test.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This controller is used for authentication
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Display room by given id
     *
     * @return ModelAndView object for login page
     */
    @GetMapping({"/","/"})
    public ModelAndView login(){
        logger.info("Entering login page");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * Method to invoke when logged in
     *
     * @return ModelAndView object with room
     */
    @GetMapping({"/loginSuccess","/home"})
    public ModelAndView loginSuccess(){
        logger.info("Login success");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/home");
        return modelAndView;
    }
}
