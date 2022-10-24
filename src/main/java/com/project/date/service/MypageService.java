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
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MypageService {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public ResponseDto<?> getMyPage(HttpServletRequest request, Long userId) {
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

        List<Integer> likeList = likeRepository.likeToLikeUserId(userId)
                .stream()
                .distinct()
                .collect(Collectors.toList());
        List<User> getLikeUser = userRepository.matchingUser(likeList);
        List<OtherLikeResponseDto> otherLikeResponseDtoList = new ArrayList<>();

        for (User list : getLikeUser) {
            otherLikeResponseDtoList.add(
                    OtherLikeResponseDto.builder()
                            .userId(list.getId())
                            .imageUrl(list.getImageUrl())
                            .build()
            );
        }

        List<Likes> likeMeList = likeRepository.findByTargetId(userId);
        List<LikedResponseDto> likedResponseDtoList = new ArrayList<>();
        for (Likes list : likeMeList) {
            likedResponseDtoList.add(
                    LikedResponseDto.builder()
                            .userId(list.getUser().getId())
                            .imageUrl(list.getUser().getImageUrl())
                            .build()
            );
        }

        return ResponseDto.success(
                MypageResponseDto.builder()
                        .userId(user.getId())
                        .imageUrl(user.getImageUrl())
                        .age(user.getAge())
                        .mbti(user.getMbti())
                        .introduction(user.getIntroduction())
                        .area(user.getArea())
                        .job(user.getJob())
                        .hobby(user.getHobby())
                        .drink(user.getDrink())
                        .idealType(user.getIdealType())
                        .likeMovieType(user.getLikeMovieType())
                        .pet(user.getPet())
                        .smoke(user.getSmoke())
                        .likedResponseDtoList(likedResponseDtoList)
                        .otherLikeResponseDtoList(otherLikeResponseDtoList)
                        .build());
    }


    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }

}