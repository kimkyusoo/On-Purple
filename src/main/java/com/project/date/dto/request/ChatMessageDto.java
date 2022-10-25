package com.project.date.dto.request;

import com.project.date.model.ChatMessage;
import com.project.date.model.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        TALK, UNREAD_MESSAGE_COUNT_ALARM, COMMENT_ALARM
    }

//    private Long userId;
    private String nickname;
    private MessageType type; // 메시지 타입
    private String roomId; // 공통으로 만들어진 방 번호

    private String otherNickname;
    private Long otherUserId;
    private String userId;




//    @NotBlank
//    @Size(max=2000)
    private String message; // 메시지
//
    private String createdAt;
    private int count;

    public ChatMessageDto(ChatMessageDto chatMessageDto, int count) {
        this.type = MessageType.UNREAD_MESSAGE_COUNT_ALARM; // 메시지 타입
        this.roomId = chatMessageDto.roomId;// 방 이름
        this.nickname = chatMessageDto.nickname;
        this.userId = chatMessageDto.userId;
        this.otherNickname = chatMessageDto.otherNickname;
        this.count = count; //안읽은 메세지 개수
    }
}
