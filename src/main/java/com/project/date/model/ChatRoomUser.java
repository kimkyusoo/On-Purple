package com.project.date.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatRoomUser extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String name;
    private String otherUserImage;
    @ManyToOne
    private User otherUser;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatRoomUser(User user, User anotherUser, ChatRoom room) {
        this.user = user;
        this.name = anotherUser.getUsername();
        this.chatRoom = room;
        this.otherUser = anotherUser;
        this.otherUserImage = anotherUser.getImageUrl();
    }
}
