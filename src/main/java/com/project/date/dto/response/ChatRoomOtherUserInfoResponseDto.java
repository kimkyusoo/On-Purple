package com.project.date.dto.response;

import com.project.date.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomOtherUserInfoResponseDto {

    private Long otherUserId;
    private String otherUsername;
    private String otherDepartment;

    public ChatRoomOtherUserInfoResponseDto(User otherUser){
        this.otherUserId = otherUser.getId();
        this.otherUsername = otherUser.getUsername();
//        this.otherDepartment = otherUser.getDepartmentName();
//        this.otherAdmission = otherUser.getAdmission().substring(2, 4) + "학번";
    }
}
