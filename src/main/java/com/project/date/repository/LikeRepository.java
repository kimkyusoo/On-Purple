package com.project.date.repository;

import com.project.date.model.Likes;
import com.project.date.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPostId(User user, Long postId);
    Optional<Likes> findByUserAndCommentId(User user, Long commentId);
    Optional<Likes> findByUserAndProfileId(User user, Long profileId);
    //나를 좋아요 한 회원
    List<Likes> findByProfileId(Long profileId);
    //내가 좋아요 한 회원
    List<Likes> findByUser(User user);

    Optional<Likes>findAllByUserAndProfileId(User user, Long profileId);
}
