package com.project.date.model;

import com.project.date.dto.request.PostRequestDto;
import com.project.date.dto.request.ProfileRequestDto;
import com.sun.istack.NotNull;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "userId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
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
    private String drink;

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

    //싫어요 count
    @Column(nullable = false)
    private int unLike;

    public void update(ProfileRequestDto requestDto) {
        this.age = requestDto.getAge();
        this.mbti = requestDto.getMbti();
        this.introduction = requestDto.getIntroduction();
        this.idealType = requestDto.getIdealType();
        this.job = requestDto.getJob();
        this.hobby = requestDto.getHobby();
        this.drink = requestDto.getDrink();
        this.pet = requestDto.getPet();
        this.smoke = requestDto.getSmoke();
        this.likeMovieType = requestDto.getLikeMovieType();
        this.area = requestDto.getArea();

    }
    public void addLike(){

        this.likes +=1;
    }

    public void minusLike(){

        this.likes -=1;
    }
    public void addUnLike(){
        this.likes +=1;
    }

    public void minusUnLike(){

        this.unLike -=1;
    }

    // 회원정보 검증
    public boolean validateUser(User user) {

        return !this.user.equals(user);
    }

}
