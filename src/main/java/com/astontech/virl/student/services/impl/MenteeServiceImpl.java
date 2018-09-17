package com.astontech.virl.student.services.impl;

import com.astontech.virl.student.domain.Mentee;
import com.astontech.virl.student.repositories.MenteeRepository;
import com.astontech.virl.student.services.MenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenteeServiceImpl implements MenteeService {

    private MenteeRepository menteeRepository;

    public MenteeServiceImpl(MenteeRepository menteeRepository) {
        this.menteeRepository = menteeRepository;
    }

    @Override
    public Mentee saveMentee(Mentee mentee) {
        return menteeRepository.save(mentee);
    }

    @Override
    public Mentee findMenteeById(String id) {
        return menteeRepository.findById(id).orElse(null);
    }

    @Override
    public Mentee findMenteeByName(String name) {
        return menteeRepository.findByName(name);
    }

    @Override
    public List<Mentee> findAllMentees() {
        List<Mentee> menteeList = new ArrayList<>();
        menteeRepository.findAll().forEach(menteeList::add);
        return menteeList;
    }

    @Override
    public void deleteMentee(String id) {
        menteeRepository.deleteById(id);
    }
}
