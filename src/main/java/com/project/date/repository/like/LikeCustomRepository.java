package com.project.date.repository.like;

import com.project.date.dto.response.LikeResponseDto;
import com.project.date.model.Likes;

import java.util.List;

public interface LikeCustomRepository {

    List<Likes> findLikeToLike(Long userId);

    List<LikeResponseDto> findUserLikeToLike(Long userId);
}
