package com.astontech.virl.student.services;

import com.astontech.virl.student.domain.UserProfile;

import java.util.Optional;

public interface UserProfileService {

    Optional<UserProfile> findByUsername(String username);

    UserProfile saveProfile(UserProfile userProfile);

}
