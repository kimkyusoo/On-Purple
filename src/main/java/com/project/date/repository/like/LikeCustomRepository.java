package com.project.date.repository.like;

import com.project.date.model.Likes;

import java.util.List;

public interface LikeCustomRepository {

    public List<Likes> findLikeToLike(Long userId);
}
