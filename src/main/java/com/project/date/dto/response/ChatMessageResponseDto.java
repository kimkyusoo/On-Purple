//package com.project.date.dto.response;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.project.date.dto.request.ChatMessageDto;
//import com.project.date.model.ChatMessage;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
//import java.time.LocalDateTime;
//
///**
// * Long userId;
// * String nickname;
// * Long otherUserId;
// * String otherNickname;
// * String message;
// * String otherImageUrl;
// * String roomId;
// * LocalDateTime createdAt;
// */
//@Getter
//@Setter
//public class ChatMessageResponseDto {
//    public enum MessageType {
//        TALK, UNREAD_MESSAGE_COUNT_ALARM, COMMENT_ALARM
//    }
//
//    private ChatMessageDto.MessageType type; // 메시지 타입
//    private Long userId;
//    private String nickname;
//    private Long otherUserId;
//    private String otherNickname;
//    @NotBlank
//    @Size(max=2000)
//    private String message;
//    private String otherImageUrl;
//
//    private String roomId;
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
//    private LocalDateTime createdAt;
//
//    public ChatMessageResponseDto(ChatMessage chatMessage) {
//
//        this.type = ChatMessageDto.MessageType.UNREAD_MESSAGE_COUNT_ALARM; // 메시지 타입
//        this.userId = chatMessage.getUser().getId();
//        this.nickname = chatMessage.getUser().getUsername();
//        this.otherUserId = chatMessage.getOtherUserId();
//        this.otherNickname = chatMessage.getOtherNickname();
//        this.otherImageUrl = chatMessage.getOtherImageUrl();
//        this.message = chatMessage.getMessage();
//        this.createdAt = chatMessage.getCreatedAt();
//        this.roomId = chatMessage.getRoomId();
//    }
//}
