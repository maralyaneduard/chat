package com.test.chat.mapper;

import com.test.chat.dto.MessageDto;
import com.test.chat.entity.Message;
import com.test.chat.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper to map Message to MessageDto
 */
@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "user", target = "userName", qualifiedByName = "userToName")
    MessageDto toMessageDto(Message message);

    List<MessageDto> toMessageDto(List<Message> messages);

    Message toMessage(MessageDto messageDto);

    @Named("userToName")
    default String userToUserName(User obj) {
        return obj.getName();
    }
}
