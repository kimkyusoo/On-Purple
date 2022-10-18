package com.project.date.service;

import com.project.date.dto.response.*;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.*;
import com.project.date.repository.LikeRepository;
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
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ResponseDto<?> getMyPage (HttpServletRequest request) {
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

//        List<Likes> likedList = likeRepository.findByTargetId(targetUserId);
//        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();
//        for (Likes likes : likedList) {
//            if(likes == null) {
//                return ResponseDto.fail("LIKE NOT FOUND", "좋아요가 없습니다");
//            }
//
//
//            User targetUser = isPresentTargetUser(targetUserId);
//            if (null == targetUser) {
//                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
//            }
//
//
//
//            likedResponseDtoList.add(
//                    LikedResponseDto.builder()
//                            .imageUrl(likes.getUser().getImageUrl())
//                            .build()
//            );
//        }

//        List<Likes> likeList = likeRepository.findByUser(user);
//        List<LikeResponseDto> likeResponseDtoList = new ArrayList<>();
//        for (Likes likes : likeList) {
//
//            likeResponseDtoList.add(
//                    LikeResponseDto.builder()
//                            .imageUrl(likes.getUser().getImageUrl())
//                            .build()
//            );
//        }
        return ResponseDto.success(
                    MypageResponseDto.builder()
                            .userId(user.getId())
                            .imageUrl(user.getImageUrl())
                            .age(user.getAge())
                            .mbti(user.getMbti())
                            .introduction(user.getIntroduction())
                            .area(user.getArea())
                            .build());
        }


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

}