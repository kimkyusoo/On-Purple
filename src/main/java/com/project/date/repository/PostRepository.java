package com.project.date.repository;


import com.project.date.model.Post;
import com.project.date.repository.post.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

}
