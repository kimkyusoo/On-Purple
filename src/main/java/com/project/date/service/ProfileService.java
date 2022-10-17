package com.project.date.service;

import com.project.date.dto.request.ProfileRequestDto;
import com.project.date.dto.request.ProfileUpdateRequestDto;
import com.project.date.dto.response.ProfileResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.ImgRepository;
import com.project.date.repository.ProfileRepository;
import com.project.date.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final ImgRepository imgRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createProfile(ProfileRequestDto requestDto, HttpServletRequest request) {
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


        Profile profile = Profile.builder()
                .user(user)
                .age(requestDto.getAge())
                .mbti(requestDto.getMbti())
                .introduction(requestDto.getIntroduction())
                .idealType(requestDto.getIdealType())
                .job(requestDto.getJob())
                .hobby(requestDto.getHobby())
                .drink(requestDto.getDrink())
                .pet(requestDto.getPet())
                .smoke(requestDto.getSmoke())
                .likeMovieType(requestDto.getLikeMovieType())
                .area(requestDto.getArea())
                .build();

        profileRepository.save(profile);

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .profileId(profile.getId())
                        .age(profile.getAge())
                        .mbti(profile.getMbti())
                        .introduction(profile.getIntroduction())
                        .idealType(profile.getIdealType())
                        .job(profile.getJob())
                        .hobby(profile.getHobby())
                        .drink(profile.getDrink())
                        .pet(profile.getPet())
                        .smoke(profile.getSmoke())
                        .likeMovieType(profile.getLikeMovieType())
                        .area(profile.getArea())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllProfiles() {
        List<Profile> profileList = profileRepository.findAllByOrderByModifiedAtDesc();
        List<ProfileResponseDto> profileResponseDto = new ArrayList<>();
        for (Profile profile : profileList) {
            List<Img> findImgList = imgRepository.findByUser_id(profile.getUser().getId());
            List<String> imgList = new ArrayList<>();
            for (Img img : findImgList) {
                imgList.add(img.getImageUrl());
            }
            profileResponseDto.add(
                    ProfileResponseDto.builder()
                            .profileId(profile.getId())
                            .nickname(profile.getUser().getNickname())
                            .age(profile.getAge())
                            .introduction(profile.getIntroduction())
                            .imageUrl(profile.getUser().getImageUrl())
                            .area(profile.getArea())
                            .build()
            );
        }
        return ResponseDto.success(profileResponseDto);
    }

    @Transactional
    public ResponseDto<?> getProfile(Long profileId) {
        Profile profile= isPresentProfile(profileId);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필입니다.");
        }

        List<Img> findImgList = imgRepository.findByUser_id(profile.getUser().getId());
        List<String> imgList = new ArrayList<>();
        for (Img img : findImgList) {
            imgList.add(img.getImageUrl());
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .profileId(profile.getId())
                        .imageUrl(profile.getUser().getImageUrl())
                        .nickname(profile.getUser().getNickname())
                        .age(profile.getAge())
                        .mbti(profile.getMbti())
                        .introduction(profile.getIntroduction())
                        .idealType(profile.getIdealType())
                        .job(profile.getJob())
                        .hobby(profile.getHobby())
                        .drink(profile.getDrink())
                        .pet(profile.getPet())
                        .smoke(profile.getSmoke())
                        .likeMovieType(profile.getLikeMovieType())
                        .area(profile.getArea())

                        .build()
        );
    }

    @Transactional
    public ResponseDto<ProfileResponseDto> updateProfile(Long profileId,
                                                         ProfileUpdateRequestDto requestDto, HttpServletRequest request) {
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

        Profile profile = isPresentProfile(profileId);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필입니다.");
        }
        if(profile.validateUser(user)){
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        profile.update(requestDto);
        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .profileId(profile.getId())
                        .age(profile.getAge())
                        .mbti(profile.getMbti())
                        .introduction(profile.getIntroduction())
                        .idealType(profile.getIdealType())
                        .job(profile.getJob())
                        .hobby(profile.getHobby())
                        .drink(profile.getDrink())
                        .pet(profile.getPet())
                        .smoke(profile.getSmoke())
                        .likeMovieType(profile.getLikeMovieType())
                        .area(profile.getArea())
                        .build()
        );
    }

    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }
    @Transactional(readOnly = true)
    public User isPresentUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfile(Long profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        return optionalProfile.orElse(null);
    }
}