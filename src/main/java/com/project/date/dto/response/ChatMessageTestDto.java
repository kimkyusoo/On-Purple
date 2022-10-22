package com.project.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.date.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Long userId
 * String username
 * String message
 * LocalDateTime createdAt
 */
@Getter
@Setter
public class ChatMessageTestDto {

    private Long userId;
    private String nickname;
    private Long otherUserId;
    private String otherNickname;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;

    public ChatMessageTestDto(ChatMessage chatMessage) {
        this.userId = chatMessage.getUser().getId();
        this.nickname = chatMessage.getUser().getUsername();
        this.otherUserId = chatMessage.getOtherUserId();
        this.otherNickname = chatMessage.getOtherNickname();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
