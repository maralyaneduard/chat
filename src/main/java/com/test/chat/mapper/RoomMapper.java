package com.test.chat.mapper;

import com.test.chat.dto.RoomDto;
import com.test.chat.entity.Room;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper to map Room object to RoomDto object
 */
@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toRoomDto(Room room);

    List<RoomDto> toRoomDTOs(List<Room> rooms);

    Room toRoom(RoomDto roomDTO);
}
