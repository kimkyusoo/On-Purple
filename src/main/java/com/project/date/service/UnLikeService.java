package com.project.date.service;

import com.project.date.jwt.TokenProvider;
import com.project.date.model.Profile;
import com.project.date.model.User;
import com.project.date.repository.ProfileRepository;
import com.project.date.repository.UnLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnLikeService {

    private final UnLikeRepository unLikeRepository;
    private final TokenProvider tokenProvider;
    private final ProfileRepository profileRepository;




















    @Transactional(readOnly = true)
    public Profile isPresentProfile(Long profileId) {
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        return optionalProfile.orElse(null);
    }




    public User validateUser(HttpServletRequest request){
        if(!tokenProvider.validateToken(request.getHeader("RefreshToken"))){
            return null;
        }return tokenProvider.getUserFromAuthentication();
    }
}
