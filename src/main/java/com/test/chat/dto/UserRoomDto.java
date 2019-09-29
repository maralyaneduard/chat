package com.test.chat.dto;

import com.test.chat.entity.Room;
import com.test.chat.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Dto for user and room info, used for
 * displaying users with appropriate rooms
 */
public class UserRoomDto {
    private int userId;

    private int roomId;

    private String userName;

    private String roomName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static List<UserRoomDto> getDtoFromList(Collection<Room> rooms){
        List<UserRoomDto> userRoomDtos = new ArrayList<>();

        for (Room room: rooms) {
            if(!room.getUsers().isEmpty()){
                for(User user: room.getUsers()){
                    UserRoomDto userRoomDto = new UserRoomDto();
                    userRoomDto.setRoomId(room.getId());
                    userRoomDto.setRoomName(room.getName());
                    userRoomDto.setUserName(user.getName());
                    userRoomDto.setUserId(user.getId());
                    userRoomDtos.add(userRoomDto);
                }
            }
        }

        return userRoomDtos;
    }
}
