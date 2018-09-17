package com.astontech.virl.student.repositories;

import com.astontech.virl.student.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {

    UserProfile findByUsername(String username);
}
