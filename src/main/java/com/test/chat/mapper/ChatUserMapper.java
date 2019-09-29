package com.test.chat.mapper;

import com.test.chat.dto.ChatUserDto;
import com.test.chat.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

/**
 * Map User object to ChatUser object
 */
@Mapper(componentModel = "spring")
public interface ChatUserMapper {
    @Mapping(source = "user", target = "name", qualifiedByName = "userToName")
    ChatUserDto toChatUserDto(User user);

    Set<ChatUserDto> toChatUserDto(Set<User> user);

    User toMessage(ChatUserDto chatUserDto);

    @Named("userToName")
    default String userToUserName(User obj) {
        return obj.getName();
    }
}
