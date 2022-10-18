package com.project.date.dto.response;

import com.project.date.dto.request.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadMessageCount {

    private Long otherMemberId;
    private int unreadCount;
    private String roomId;
    private String type;

    public UnreadMessageCount(ChatMessageDto roomMessage) {
        this.type = "UNREAD";
        this.otherMemberId = roomMessage.getOtherMemberId();
        this.unreadCount = roomMessage.getCount();
        this.roomId = roomMessage.getRoomId();
    }
}
