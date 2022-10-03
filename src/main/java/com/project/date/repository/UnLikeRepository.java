package com.project.date.repository;

import com.project.date.model.Likes;
import com.project.date.model.User;

import java.util.Optional;

public interface UnLikeRepository {
    Optional<Likes> findByUserAndProfileId(User user, Long profileId);
}
