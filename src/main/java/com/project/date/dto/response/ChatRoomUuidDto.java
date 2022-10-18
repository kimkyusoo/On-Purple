package com.project.date.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomUuidDto {
    private String roomId;

    public ChatRoomUuidDto(String roomId) {
        this.roomId = roomId;
    }
}
