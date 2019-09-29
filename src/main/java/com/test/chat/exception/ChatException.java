package com.test.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Encapsulates information about room and user when exception happened
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="User or room are not found")
public class ChatException extends RuntimeException {
    private Integer roomId;
    private String email;

    public ChatException(Integer roomId, String email,String errorMessage) {
        super(errorMessage);
        this.roomId = roomId;
        this.email = email;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ChatException{" +
                "roomId=" + roomId +
                ", email='" + email + '\'' +
                '}';
    }
}
