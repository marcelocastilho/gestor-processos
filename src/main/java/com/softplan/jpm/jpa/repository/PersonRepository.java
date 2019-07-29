package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softplan.jpm.entities.Person;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAll();
    
    List<Person> findByName(String name);
}
