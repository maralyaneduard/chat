package com.test.chat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(SQLException.class)
	public ModelAndView handleSQLException(HttpServletRequest request, Exception ex){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		modelAndView.addObject("error", "Something went wrong during the request");
		logger.error("SQLException Occured ", ex);
		logger.error("Request info: ", request);

		return modelAndView;
	}
	
	@ExceptionHandler(IOException.class)
	public ModelAndView handleIOException(HttpServletRequest request, Exception ex){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		modelAndView.addObject("error", "Something is wrong when tryng to upload or delete file");

		logger.error("IO exception occured", ex);
		logger.error("Request info: ", request);

		return modelAndView;
	}

	@ExceptionHandler(ChatException.class)
	public ModelAndView handleChatException(HttpServletRequest request, Exception ex){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		modelAndView.addObject("error", "User or room is incorrect");

		logger.error("ChatException occured", ex);
		logger.error("Request info: ", request);

		return modelAndView;
	}
}