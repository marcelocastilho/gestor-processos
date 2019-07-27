package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.Responsable;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {

    List<Responsable> findAll();

}
