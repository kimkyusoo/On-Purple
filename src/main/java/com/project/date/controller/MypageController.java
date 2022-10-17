package com.project.date.controller;

import com.project.date.dto.response.ResponseDto;
import com.project.date.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;



    @GetMapping("/mypage/{profileId}")
    public ResponseDto<?> getMypage(HttpServletRequest request, @PathVariable Long profileId){

        return mypageService.getMyPage(request, profileId);
    }

    //나를 좋아요
//    @GetMapping("/mypage/like/{profileId}")
//    public ResponseDto<?> getAllLikeUser(@PathVariable Long profileId, HttpServletRequest request) {
//        return mypageService.getAllLikeUser(profileId, request);
//    }

//    @GetMapping("/mypage/tolike/{profileId}")
//    public ResponseDto<?>toLikeUser(@PathVariable Long profileId, HttpServletRequest request){
//        return mypageService.toLikeUser(profileId,request);
//    }

}
