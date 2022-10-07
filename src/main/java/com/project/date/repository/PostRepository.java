package com.project.date.repository;


import com.project.date.model.Post;
import com.project.date.repository.post.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

//    List<Post> findAllByCategoryOrderByCreatedAtDesc(String category);
}
