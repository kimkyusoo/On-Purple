package com.project.date.service;

import com.project.date.dto.request.LoginRequestDto;
import com.project.date.dto.request.SignupRequestDto;
import com.project.date.dto.request.TokenDto;
import com.project.date.dto.request.UserUpdateRequestDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.dto.response.UserResponseDto;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.Img;
import com.project.date.model.User;
import com.project.date.repository.ImgRepository;
import com.project.date.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final ImgRepository imgRepository;

    @Transactional
//  회원가입. 유저가 존재하는지, 비밀번호와 비밀번호확인이 일치하는지의 여부를 if문을 통해 확인하고 이를 통과하면 user에 대한 정보를 생성.
    public ResponseDto<UserResponseDto> createUser(SignupRequestDto requestDto, List<String> imgPaths) {
        if (null != isPresentUser(requestDto.getUsername()))
            return ResponseDto.fail("DUPLICATED_USERNAME", "중복된 ID 입니다.");

        if (null != isPresentNickname(requestDto.getNickname()))
            return ResponseDto.fail("DUPLICATED_USERNAME", "중복된 닉네임 입니다.");

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
                    "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .build();
        userRepository.save(user);

        postBlankCheck(imgPaths);

        List<String> imgList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            Img img = new Img(imgUrl, user);
            imgRepository.save(img);
            imgList.add(img.getImgUrl());
        }

        return ResponseDto.success(
                UserResponseDto.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .imgUrl(imgList.get(0))
                        .build()
        );

    }
    private void postBlankCheck(List<String> imgPaths) {
        if(imgPaths == null || imgPaths.isEmpty()){ //.isEmpty()도 되는지 확인해보기
            throw new NullPointerException("이미지를 등록해주세요(Blank Check)");
        }
    }

    @Transactional
//  로그인. 가입할때 사용된 정보를 SignupRequestDto에 보내고 HttpServletResponse에 속한 권한이 확인.
//  사용자의 아이디가 존재하지 않거나 비밀번호확인이 일치하지 않았을 때 오류 메시지를 출력.
//  정상일 경우 tokenProvider를 통하여 유저에게 토큰을 생성하고 이를 헤더에 보낸다.
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        User user = isPresentUser(requestDto.getUsername());
        if (null == user) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!user.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_USER", "비밀번호가 틀렸습니다..");
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(user);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                UserResponseDto.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .build()
        );
    }

    //  로그아웃. HttpServletRequest에 있는 권한을 보내 토큰을 확인하여 일치하지 않거나 유저 정보가 없을 경우 오류 메시지를 출력
    //  정상일 경우 tokenProvider에 유저에게 있는 리프레시토큰 삭제를 진행
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        User user = tokenProvider.getUserFromAuthentication();
        if (null == user) {
            return ResponseDto.fail("USER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(user);
    }

//    public ResponseDto<?> updateUser(HttpServletRequest request, UserUpdateRequestDto requestDto) {
//        User user = validateUser(request);
//        if(null == user)
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//
//        user.update(requestDto);
//        return ResponseDto.success(
//                UserResponseDto.builder()
//                        .nickname(user.getNickname())
//                        .imageUrl(user.getImageUrl())
//                        .build()
//        );
//    }
//
//    @Transactional
//    public User validateUser(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
//            return null;
//        }
//        return tokenProvider.getUserFromAuthentication();
//    }

    @Transactional(readOnly = true)
    public User isPresentUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    @Transactional(readOnly = true)
    public User isPresentNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);
        return optionalUser.orElse(null);
    }

    //  TokenDto와 HttpServletResponse 응답을 헤더에 보낼 경우
    //  권한과 tokenDto에 있는 AccessToken을 추가
    //  Refresh-token을 추가
    //  AccessToken의 유효기간을 추가한다.
    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}
