package com.project.date.controller;

import com.project.date.dto.response.ResponseDto;
import com.project.date.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @GetMapping("/main")
    public ResponseDto<?> getAllProfiles() {

        return profileService.getAllProfile();
    }


    @GetMapping( "/profile/{profileId}")
    public ResponseDto<?> getProfile(@PathVariable Long profileId) {
        return profileService.getProfile(profileId);
    }
}