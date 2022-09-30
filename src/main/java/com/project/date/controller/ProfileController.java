package com.project.date.controller;

import com.project.date.dto.request.ProfileRequestDto;
import com.project.date.dto.response.ResponseDto;
import com.project.date.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

   @PostMapping("/profile")
   public ResponseDto<?> createProfile(@RequestBody ProfileRequestDto requestDto, HttpServletRequest request) {
       return profileService.createProfile(requestDto, request);
   }

    @GetMapping("/main")
    public ResponseDto<?> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping( "/profile/{profileId}")
    public ResponseDto<?> getProfile(@PathVariable Long profileId) {
        return profileService.getProfile(profileId);
    }
}
