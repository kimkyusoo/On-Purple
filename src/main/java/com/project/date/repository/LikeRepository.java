package com.project.date.repository;

import com.project.date.model.Likes;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndPostId(User user, Long postId);

    Optional<Likes> findByUserAndCommentId(User user, Long commentId);

    Optional<Likes> findByUserAndTargetId(User user, Long targetId);

    //나를 좋아요 한 회원
    List<Likes> findByTargetId(Long targetId);

    //내가 좋아요 한 회원
    List<Likes> findByUser(User user);

    Optional<Likes> findAllByUserAndTargetId(User user, Long targetId);

}