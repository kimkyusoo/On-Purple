package com.project.date.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.date.model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageTestDto {

    private Long userId;
    private String username;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createdAt;

    public ChatMessageTestDto(ChatMessage chatMessage) {
        this.userId = chatMessage.getUser().getId();
        this.username = chatMessage.getUser().getUsername();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
