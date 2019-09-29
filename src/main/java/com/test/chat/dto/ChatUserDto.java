package com.test.chat.dto;

import javax.validation.constraints.NotNull;

/**
 *  Dto with the info of username and id
 *  to be used in users of room page
 */
public class ChatUserDto {
    private int id;

    @NotNull
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
