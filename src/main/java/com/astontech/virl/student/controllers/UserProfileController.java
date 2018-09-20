package com.astontech.virl.student.controllers;


import com.astontech.virl.student.domain.UserProfile;
import com.astontech.virl.student.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static com.astontech.virl.student.configuration.CustomSuccessHandler.removeEmailSignature;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getAllProfiles(@PathVariable String username) {
        return profileSearch(username);
    }

    @GetMapping("/")
    public ResponseEntity<UserProfile> getSessionProfile(HttpSession session) {
        if(session.getAttribute("username") != null) {
            String username = removeEmailSignature(session.getAttribute("username").toString());
            if(username == null) {
                logger.error("Error getting username from Session.");
                return ResponseEntity.status(403).body(null);
            } else {
                return profileSearch(username);
            }
        } else {
            logger.error("Error no Session!");
            return ResponseEntity.status(420).body(null);
        }
    }

    public ResponseEntity<UserProfile> profileSearch(String username) {
        Optional<UserProfile> searchedProfile = userProfileService.findByUsername(username);
        if (!searchedProfile.isPresent()) {
            logger.info("USER PROFILE NOT FOUND " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .body(searchedProfile.get());
    }

    @PostMapping("/")
    public ResponseEntity<UserProfile> saveProfile(@RequestBody UserProfile userProfile) {
        UserProfile savedProfile = userProfileService.saveProfile(userProfile);
        if (savedProfile.getId() == null) {
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(userProfile);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }
}
