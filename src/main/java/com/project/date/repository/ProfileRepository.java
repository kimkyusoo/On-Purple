package com.project.date.repository;

import java.util.List;

import com.project.date.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByOrderByModifiedAtDesc();
}
