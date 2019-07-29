package com.softplan.jpm.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.JudicialProcess;

/**
 * Specifies methods used to obtain and modify JudicialProcess related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
@Repository
public interface JudicialProcessRepository extends JpaRepository<JudicialProcess, Long> {

	//@Query("select judicialProcess from JudicialProcess judicialProcess left join item.responsables")
    List<JudicialProcess> findAll();
}
