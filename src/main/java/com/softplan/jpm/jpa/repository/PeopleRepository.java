package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.People;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {

    List<People> findAll();

}
