package com.astontech.virl.student.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("mentee")
public class Mentee {

    @Id
    private String id;

    @Indexed
    String name;

    String site;
    String bu;
    String assignedVirlInstance;

}
