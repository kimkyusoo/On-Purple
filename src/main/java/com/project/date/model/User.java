package com.project.date.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.date.dto.request.UserUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends Timestamped {

    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 1000)
    private String imageUrl;

    @JoinColumn(name = "userInfoId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo userInfo;

    //img
    @Transient
    @OneToMany(fetch = FetchType.LAZY)
    private final List<Img> imgList = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public void update(UserUpdateRequestDto userUpdateRequestDto) {
        this.nickname = userUpdateRequestDto.getNickname();
        this.password = userUpdateRequestDto.getPassword();
        this.imageUrl = userUpdateRequestDto.getImageUrl();
    }


}
