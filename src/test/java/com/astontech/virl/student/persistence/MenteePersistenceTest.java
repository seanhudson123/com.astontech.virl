package com.astontech.virl.student.persistence;


import com.astontech.virl.student.Application;
import com.astontech.virl.student.domain.Mentee;
import com.astontech.virl.student.repositories.MenteeRepository;
import com.astontech.virl.student.services.MenteeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MenteePersistenceTest {

    @Autowired
    private MenteeRepository menteeRepository;

    @Autowired
    private MenteeService menteeService;

    @Test
    public void testMenteeRepository() {

        Mentee mentee = new Mentee();
        mentee.setName("bipin");
        mentee.setBu("cisco");
        mentee.setSite("CA");
        mentee.setAssignedVirlInstance("virl01");

        // INSERT
        Mentee savedMentee = menteeRepository.save(mentee);
        Assert.assertNotNull(savedMentee.getId());

        // RETRIEVE BY ID
        Mentee foundMentee = menteeRepository.findById(savedMentee.getId()).orElse(null);
        Assert.assertNotNull(foundMentee);

        // RETRIEVE BY NAME
        Mentee foundByName = menteeRepository.findByName("bipin");
        Assert.assertEquals("bipin", foundByName.getName());

        // UPDATE
        foundMentee.setSite("VA");
        Mentee updatedMentee = menteeRepository.save(foundMentee);
        Assert.assertEquals("VA", updatedMentee.getSite());

        // DELETE
        String idToDelete = updatedMentee.getId();
        menteeRepository.deleteById(idToDelete);
        Assert.assertEquals(Optional.empty(), menteeRepository.findById(idToDelete));

//        System.out.println(menteeRepository.findAll().toString());


    }


    @Test
    public void testMenteeService() {

        Mentee mentee = new Mentee();
        mentee.setName("tony");
        mentee.setBu("cisco");
        mentee.setSite("MN");
        mentee.setAssignedVirlInstance("virl02");

        menteeService.saveMentee(mentee);

        System.out.println(menteeService.findAllMentees());
    }
}
