package com.project.date.repository;

import com.project.date.dto.response.LikeResponseDto;
import com.project.date.model.Likes;
import com.project.date.model.User;
import com.project.date.repository.like.LikeCustomRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long>, LikeCustomRepository {

    Optional<Likes> findByUserAndPostId(User user, Long postId);

    Optional<Likes> findByUserAndCommentId(User user, Long commentId);

    Optional<Likes> findByUserAndTargetId(User user, Long targetId);

    //나를 좋아요 한 회원
    List<Likes> findByTargetId(Long targetId);

    //내가 좋아요 한 회원
    List<Likes> findByUser(User user);

    Optional<Likes> findAllByUserAndTargetId(User user, Long targetId);

    @Query(value = "SELECT DISTINCT l.user.id FROM Likes l WHERE l.user.id IN(SELECT l.target.id FROM Likes l where l.user.id = :userId)")
    List<Likes> findUserJPQLToLike(User user, @Param(value = "userId") Long userId);



}