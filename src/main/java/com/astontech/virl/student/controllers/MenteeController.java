package com.astontech.virl.student.controllers;

import com.astontech.virl.student.domain.Mentee;
import com.astontech.virl.student.services.MenteeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mentee")
public class MenteeController {

    private static final Logger log = LoggerFactory.getLogger(MenteeController.class);

    MenteeService menteeService;

    public MenteeController(MenteeService menteeService) {
        this.menteeService = menteeService;
    }

    @GetMapping("/")
    public List<Mentee> getAllMentees() {
        return menteeService.findAllMentees();
    }

    @GetMapping("/{id}")
    public Mentee getMenteeById(@PathVariable String id) {
        return menteeService.findMenteeById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Mentee> getMenteeByName(@PathVariable String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Company", "Aston Technologies");
        headers.add("Business-Unit", "Software Development");

        Mentee searchedMentee = menteeService.findMenteeByName(name);
        if (searchedMentee == null) {
            log.info("Mentee " + name + " Not Found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .headers(headers)
                            .body(null);
        }

        return ResponseEntity.ok()
                        .headers(headers)
                        .body(searchedMentee);
    }

    @PostMapping("/")
    public ResponseEntity<Mentee> saveMentee(@RequestBody Mentee mentee) {
        Mentee savedMentee = menteeService.saveMentee(mentee);
        if(savedMentee.getId() == null) {
            // HTTP STATUS CODE 422
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(savedMentee);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMentee);

    }

    @DeleteMapping("/{id}")
    public void deleteMentee(@PathVariable String id) {
        menteeService.deleteMentee(id);
    }

}
