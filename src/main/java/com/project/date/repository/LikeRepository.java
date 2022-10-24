package com.project.date.repository;

import com.project.date.dto.response.LikesResponseDto;
import com.project.date.model.Likes;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndPostId(User user, Long postId);

    Optional<Likes> findByUserAndCommentId(User user, Long commentId);

    Optional<Likes> findByUserAndTargetId(User user, Long targetId);

    //나를 좋아요 한 회원
    List<Likes> findByTargetId(Long userId);

    //내가 좋아요 한 회원
    List<Likes> findAllByUser(User user);

    @Query(value ="select l.user.id from Likes l where l.user.id in(select l.target.id from Likes l where l.user.id =:userId)")
    List<Integer> likeToLikeUserId(Long userId);

//    내가 좋아요한 사람 List
    @Query(value="select targat_id from likes WHERE user_id =:userId", nativeQuery = true)
    List<Likes> likeList(Long userId);

//    나를 좋아요한 사람 List
    @Query(value="select user_id from likes WHERE target_id =:targetId", nativeQuery = true)
    List<Likes> likeMeList(Long targetId);
}