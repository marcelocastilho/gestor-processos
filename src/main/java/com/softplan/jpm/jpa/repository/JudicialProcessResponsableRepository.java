package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.JudicialProcessResponsable;

@Repository
public interface JudicialProcessResponsableRepository extends JpaRepository<JudicialProcessResponsable, Long> {
	
	 List<JudicialProcessResponsable> findAll();

}
