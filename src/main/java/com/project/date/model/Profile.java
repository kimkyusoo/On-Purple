package com.project.date.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile extends Timestamped{
    @Id
    @Column(name = "profileId", nullable = false)
    private Long id;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;


    @JoinColumn(name = "userInfoId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo userInfo;


    public Profile(User user) {
        this.user = user;
    }

}