package com.project.date.dto.response;

import com.project.date.dto.request.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

/**
 * String roomId
 * String message
 * String createdAt
 * Long otherNickname
 * Long userId
 */
@Getter
@Setter
public class MessageResponseDto {
    private String roomId;
    private String message;
    private String createdAt;
    private Long otherNickname;
    private Long userId;

    public MessageResponseDto(ChatMessageDto roomMessage) {

        this.roomId = roomMessage.getRoomId();
        this.message = roomMessage.getMessage();
        this.createdAt = roomMessage.getCreatedAt();
        this.otherNickname = roomMessage.getOtherNickname();
        this.userId = roomMessage.getUserId();
    }
}
