package com.project.date.service;

import com.project.date.dto.request.ProfileUpdateRequestDto;
import com.project.date.dto.response.ProfileResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.ImgRepository;
import com.project.date.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {


    private final UserRepository userRepository;

    private final ImgRepository imgRepository;

    private final TokenProvider tokenProvider;


    @Transactional(readOnly = true)
    public ResponseDto<?> getAllProfiles() {
        List<User> profileList = userRepository.findAll();
        List<ProfileResponseDto> profileResponseDto = new ArrayList<>();
//        랜덤 추출 코드
        Collections.shuffle(profileList);
        for (User user : profileList) {
            List<Img> findImgList = imgRepository.findByUser_id(user.getId());
            List<String> imgList = new ArrayList<>();
            for (Img img : findImgList) {
                imgList.add(img.getImageUrl());
            }
            profileResponseDto.add(
                    ProfileResponseDto.builder()
                            .userId(user.getId())
                            .gender(user.getGender())
                            .nickname(user.getNickname())
                            .age(user.getAge())
                            .introduction(user.getIntroduction())
                            .imageUrl(user.getImageUrl())
                            .area(user.getArea())
                            .build()
            );
        }
        return ResponseDto.success(profileResponseDto);
    }

    @Transactional
    public ResponseDto<?> getProfile(Long userId) {
        User user= isPresentProfile(userId);
        if (null == user) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필입니다.");
        }

        List<Img> findImgList = imgRepository.findByUser_id(user.getId());
        List<String> imgList = new ArrayList<>();
        for (Img img : findImgList) {
            imgList.add(img.getImageUrl());
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .userId(user.getId())
                        .imageUrl(user.getImageUrl())
                        .nickname(user.getNickname())
                        .age(user.getAge())
                        .mbti(user.getMbti())
                        .introduction(user.getIntroduction())
                        .idealType(user.getIdealType())
                        .job(user.getJob())
                        .hobby(user.getHobby())
                        .drink(user.getDrink())
                        .pet(user.getPet())
                        .smoke(user.getSmoke())
                        .likeMovieType(user.getLikeMovieType())
                        .area(user.getArea())

                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> updateProfile(ProfileUpdateRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }


        user.update(requestDto);
        userRepository.save(user);
        return ResponseDto.success("프로필 정보 수정이 완료되었습니다!");
    }

    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }

    @Transactional(readOnly = true)
    public User isPresentProfile(Long userId) {
        Optional<User> optionalProfile = userRepository.findById(userId);
        return optionalProfile.orElse(null);
    }
}