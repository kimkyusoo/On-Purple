package com.project.date.repository;

import com.project.date.model.Comment;
import com.project.date.model.Likes;
import com.project.date.model.Post;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndPost(User user, Post post);
}
