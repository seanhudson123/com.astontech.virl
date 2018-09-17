package com.astontech.virl.student.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@RedisHash("userprofile")
public class UserProfile {

    @Id
    private String id;

    @Indexed
    String username;

    String role;
    String landing;

    public UserProfile() {}

    public UserProfile(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
