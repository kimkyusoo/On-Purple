package com.project.date.repository;


import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByModifiedAtDesc();

    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);
}