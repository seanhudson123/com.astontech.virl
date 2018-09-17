package com.astontech.virl.student.repositories;

import com.astontech.virl.student.domain.Mentee;
import org.springframework.data.repository.CrudRepository;

public interface MenteeRepository extends CrudRepository<Mentee, String> {

    Mentee findByName(String name);

}
