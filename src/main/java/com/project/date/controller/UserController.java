package com.project.date.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.date.dto.request.*;
import com.project.date.dto.response.ResponseDto;
import com.project.date.service.KakaoService;
import com.project.date.service.UserService;
import com.project.date.util.AwsS3UploadService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final AwsS3UploadService s3Service;

    private final KakaoService kakaoService;

    // POST방식 회원가입 API UserRequestDto에서 표현한 정규표현식을 따른 정보를 받아 UserService에서 정의한 createUser메소드에 따라 아이디와 비밀번호 확인을 거치고 이를 만족시키면 아이디 비밀번호를 생성.

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestPart(value = "info",required = false) @Valid SignupRequestDto requestDto,
                                 @RequestPart(value = "userInfo", required = false) UserInfoRequestDto userInfoRequestDto,
                                 @RequestPart(value = "imageUrl", required = false) List<MultipartFile> multipartFiles, HttpServletResponse response){
        if (multipartFiles == null) {
            throw new NullPointerException("사진을 업로드해주세요");
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);

        return userService.createUser(requestDto, userInfoRequestDto, imgPaths, response);
    }

    // POST방식 로그인 API SignupRequestDto에서 정보를 받아 권한인증을 거치고 이를 UserService에서 정의한 login메소드에 따라 아이디와 비밀번호 확인을 거치고 이를 만족시키면 토큰을 발행. 로그인에 성공한 후 작업처리를 진행한다.
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
                                HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @PostMapping("/user/idCheck/{username}")
    public ResponseDto<?> checkUser(@PathVariable String username) {
        return userService.checkUser(username);
    }

    @PostMapping("/user/nicknameCheck/{nickname}")
    public ResponseDto<?> checkNickname(@PathVariable String nickname) {
        return userService.checkNickname(nickname);
    }

    @RequestMapping(value = "/mypage/password", method = RequestMethod.PUT)
    public ResponseDto<?> passwordUpdate(@RequestBody UserUpdateRequestDto requestDto,
                                     HttpServletRequest request) {


        return userService.updatePassword(requestDto, request);
    }

    @RequestMapping(value = "/mypage/image", method = RequestMethod.PUT)
    public ResponseDto<?> imageUpdate(ImageUpdateRequestDto requestDto, @RequestPart("imageUrl")List<MultipartFile> multipartFiles,
                                      HttpServletRequest request){
        if (multipartFiles == null) {
            throw new NullPointerException("사진을 업로드해주세요");
        }
        List<String> imgPaths = s3Service.upload(multipartFiles);
        return userService.updateImage(request, imgPaths, requestDto);
    }


    @RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public ResponseDto<?> getUser(HttpServletRequest request) {
        return userService.getUser(request);
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request) {
        return userService.logout(request);

    }

    @RequestMapping(value = "/user/kakaoLogin", method = RequestMethod.GET)
    public KakaoUserRequestDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        log.info(code);
        return kakaoService.kakaoLogin(code, response);
    }
}

