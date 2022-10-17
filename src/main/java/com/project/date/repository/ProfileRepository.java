package com.project.date.repository;

import java.util.List;

import com.project.date.model.Profile;
import com.project.date.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByOrderByModifiedAtDesc();

    List<Profile> findByUser(User user);

}