
package com.project.date.service;

import com.project.date.dto.response.ProfileResponseDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.model.*;
import com.project.date.repository.ImgRepository;
import com.project.date.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ImgRepository imgRepository;


//    메인 페이지 조회
@Transactional(readOnly = true)
public ResponseDto<?> getAllProfile() {
    List<Profile> profileList = profileRepository.findAllByOrderByModifiedAtDesc();
    List<ProfileResponseDto> profileResponseDto = new ArrayList<>();

    for (Profile profile : profileList) {
        List<Img> findImgList = imgRepository.findByProfile_id(profile.getId());
        List<String> imgList = new ArrayList<>();
        for (Img img : findImgList) {
            imgList.add(img.getImageUrl());
        }
        profileResponseDto.add(
                ProfileResponseDto.builder()
                        .profileId(profile.getId())
                        .nickname(profile.getUser().getNickname())
                        .age(profile.getUserInfo().getAge())
                        .imgUrl(imgList.get(0))
                        .introduction(profile.getUserInfo().getIntroduction())
                        .build()
        );
    }

    return ResponseDto.success(profileResponseDto);

}
    //    디테일 페이지 조회
    @Transactional
    public ResponseDto<?> getProfile(Long profileId) {
        Profile profile = isPresentProfile(profileId);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필입니다.");
        }
        List<Profile> profileList = profileRepository.findAllByUser(profile);
        List<ProfileResponseDto> profileResponseDtoList = new ArrayList<>();
        List<Img> findImgList = imgRepository.findByProfile_id(profile.getId());
        List<String> imgList = new ArrayList<>();
        for (Img img : findImgList) {
            imgList.add(img.getImageUrl());
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .profileId(profile.getId())
                        .imgUrl(imgList.get(0))
                        .nickname(profile.getUser().getNickname())
                        .age(profile.getUserInfo().getAge())
                        .mbti(profile.getUserInfo().getMbti())
                        .introduction(profile.getUserInfo().getIntroduction())
                        .idealType(profile.getUserInfo().getIdealType())
                        .job(profile.getUserInfo().getJob())
                        .hobby(profile.getUserInfo().getHobby())
                        .pet(profile.getUserInfo().getPet())
                        .smoke(profile.getUserInfo().getSmoke())
                        .likeMovieType(profile.getUserInfo().getLikeMovieType())
                        .area(profile.getUserInfo().getArea())

                        .build()
        );
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfile(Long Id) {
        Optional<Profile> optionalProfile = profileRepository.findById(Id);
        return optionalProfile.orElse(null);
    }
}

