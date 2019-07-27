package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.JudicialProcess;

@Repository
public interface JudicialProcessRepository extends JpaRepository<JudicialProcess, Long> {

    List<JudicialProcess> findAll();

}
