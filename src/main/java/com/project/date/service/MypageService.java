package com.project.date.service;

import com.project.date.dto.response.*;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.LikeRepository;
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
public class MypageService {
    private final TokenProvider tokenProvider;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

// 1. Profile, profile 를 추가하여 return 값에 profile.getId 와 같은 형태로 받아오는 방법
// No serializer found for class com.project.date.dto.response.ProfileResponseDto$ProfileResponseDtoBuilder and no properties discovered to create BeanSerializer
//
// 2. user.getProfile().getId() 형태로 받아오는 방법
// Cannot invoke "com.project.date.model.Profile.getId()" because the return value of "com.project.date.model.User.getProfile()" is null

    @Transactional
    public ResponseDto<?> getMyPage (HttpServletRequest request, Long targetUserId) {
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

        List<Likes> likedList = likeRepository.findByTargetId(targetUserId);
        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();
        for (Likes likes : likedList) {
            if(likes == null) {
                return ResponseDto.fail("LIKE NOT FOUND", "좋아요가 없습니다");
            }


            User targetUser = isPresentTargetUser(targetUserId);
            if (null == targetUser) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
            }



            likedResponseDtoList.add(
                    LikedResponseDto.builder()
                            .imageUrl(likes.getUser().getImageUrl())
                            .build()
            );
        }

        List<Likes> likeList = likeRepository.findByUser(user);
        List<LikeResponseDto> likeResponseDtoList = new ArrayList<>();
        for (Likes likes : likeList) {

            likeResponseDtoList.add(
                    LikeResponseDto.builder()
                            .imageUrl(likes.getUser().getImageUrl())
                            .build()
            );
        }

        List<Profile> profileList = profileRepository.findByUser(user);
        List<MypageResponseDto> mypageResponseDto = new ArrayList<>();
        for (Profile profile : profileList) {
            mypageResponseDto.add(
                    MypageResponseDto.builder()
                            .profileId(profile.getId())
                            .imageUrl(profile.getUser().getImageUrl())
                            .age(profile.getAge())
                            .mbti(profile.getMbti())
                            .introduction(profile.getIntroduction())
                            .area(profile.getArea())
                            .likeResponseDtoList(likeResponseDtoList)
                            .likedResponseDtoList(likedResponseDtoList)
                            .build());
        }

        return ResponseDto.success(mypageResponseDto);
    }

//    @Transactional
//    public ResponseDto<?> getAllLikeUser(Long profileId, HttpServletRequest request) {
//
//        if (null == request.getHeader("RefreshToken")) {
//            return ResponseDto.fail("USER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("USER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        User user = validateUser(request);
//        if (null == user) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        Profile profile = isPresentProfile(profileId);
//        if (null == profile) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
//        }
//
//        if (profile.validateUser(user)) {
//            return ResponseDto.fail("BAD_REQUEST", "본인 정보만 조회 할 수 있습니다.");
//        }
//
//        List<Likes> likeList = likeRepository.findByProfileId(profileId);
//        List<LikeResponseDto> likeResponseDto = new ArrayList<>();
//        for (Likes likes : likeList) {
//            if(likes == null) {
//                return ResponseDto.fail("LIKE NOT FOUND", "좋아요가 없습니다");
//            }
//
//            likeResponseDto.add(
//                    LikeResponseDto.builder()
//                            .imageUrl(likes.getUser().getImageUrl())
//                            .build()
//            );
//        }
//
//        return ResponseDto.success(likeResponseDto);
//
//    }

//    @Transactional
//    public ResponseDto<?> toLikeUser(Long profileId, HttpServletRequest request) {
//
//
//        if (null == request.getHeader("RefreshToken")) {
//            return ResponseDto.fail("USER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("USER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        User user = validateUser(request);
//        if (null == user) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        Profile profile = isPresentProfile(profileId);
//        if (null == profile) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필입니다.");
//        }
//
//        if (profile.validateUser(user)) {
//            return ResponseDto.fail("BAD_REQUEST", "본인 정보만 조회 할 수 있습니다.");
//        }
//
//        List<Likes> likedList = likeRepository.findByUser(user);
//        List<LikeResponseDto> likeResponseDto = new ArrayList<>();
//        for (Likes likes : likedList) {
//
//            likeResponseDto.add(
//                    LikeResponseDto.builder()
//                            .imageUrl(likes.getUser().getImageUrl())
//                            .build()
//            );
//        }
//        return ResponseDto.success(likeResponseDto);
//    }

    @Transactional(readOnly = true)
    public User isPresentTargetUser(Long targetUserId) {
        Optional<User> optionalTargetUser = userRepository.findById(targetUserId);
        return optionalTargetUser.orElse(null);
    }


    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfile(Long profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        return optionalProfile.orElse(null);
    }

}