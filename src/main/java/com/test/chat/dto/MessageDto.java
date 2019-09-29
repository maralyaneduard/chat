package com.test.chat.dto;

import com.test.chat.enumeration.MessageType;

import javax.validation.constraints.NotNull;

/**
 * Dto with message info, containing text to be sent
 * username of sender, and MessageType can be  CHAT,JOIN,LEAVE,KICK
 */
public class MessageDto {
    private int id;

    @NotNull
    private String text;

    @NotNull
    private String userName;

    private MessageType messageType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
