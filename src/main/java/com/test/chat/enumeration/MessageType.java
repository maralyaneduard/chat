package com.test.chat.enumeration;

/**
 * Enum for message types, used to identify whether the message is e.g. text message(CHAT) or System message(KICK)
 */
public enum MessageType {
    CHAT,
    JOIN,
    LEAVE,
    KICK
}
