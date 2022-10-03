package com.project.date.repository;

import com.project.date.model.UnLike;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnLikeRepository extends JpaRepository<UnLike, Long> {
    Optional<UnLike> findByUserAndProfileId(User user, Long profileId);
}
