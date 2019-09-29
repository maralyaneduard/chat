package com.test.chat.mapper;

import com.test.chat.dto.UserDto;
import com.test.chat.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper to map User object to UserDto object
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    List<UserDto> toUserDTOs(List<User> users);

    User toUser(UserDto userDTO);
}
