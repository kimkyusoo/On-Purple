package com.project.date.model;

import com.project.date.dto.request.ProfileRequestDto;
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
public class Profile extends Timestamped{

    @Id
    @Column(name = "profileId", nullable = false)
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

    //좋아요 count
    @Column(nullable = false)
    private int likes;

    public void profileUpdate(ProfileRequestDto profileRequestDto) {
        this.age = profileRequestDto.getAge();
        this.mbti = profileRequestDto.getMbti();
        this.introduction = profileRequestDto.getIntroduction();
        this.idealType = profileRequestDto.getIdealType();
        this.job = profileRequestDto.getJob();
        this.hobby = profileRequestDto.getHobby();
        this.pet = profileRequestDto.getPet();
        this.smoke = profileRequestDto.getSmoke();
        this.likeMovieType = profileRequestDto.getLikeMovieType();
        this.area = profileRequestDto.getArea();

    }
}
