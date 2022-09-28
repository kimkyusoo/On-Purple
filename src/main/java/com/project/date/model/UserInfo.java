package com.project.date.model;

import com.project.date.dto.request.UserInfoUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInfo {

    @Id
    @Column(name = "userInfoId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column
    private int age;

    @Column
    private String mbti;

    @Column
    private String introduction;

    @Column
    private String idealType;

    @Column
    private String job;

    @Column
    private String hobby;

    @Column
    private String pet;

    @Column
    private String smoke;

    @Column
    private String likeMovieType;

    @Column
    private String area;


    public boolean validateUser(User user) {

        return !this.user.equals(user);
    }

    public void update(UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        this.age = userInfoUpdateRequestDto.getAge();
        this.mbti = userInfoUpdateRequestDto.getMbti();
        this.introduction = userInfoUpdateRequestDto.getIntroduction();
        this.idealType = userInfoUpdateRequestDto.getIdealType();
        this.job = userInfoUpdateRequestDto.getJob();
        this.hobby = userInfoUpdateRequestDto.getHobby();
        this.pet = userInfoUpdateRequestDto.getPet();
        this.smoke = userInfoUpdateRequestDto.getSmoke();
        this.likeMovieType = userInfoUpdateRequestDto.getLikeMovieType();
        this.area = userInfoUpdateRequestDto.getArea();

    }


}