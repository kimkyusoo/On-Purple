package com.project.date.repository;

import com.project.date.model.Profile;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByUserId(Long userId);
}
