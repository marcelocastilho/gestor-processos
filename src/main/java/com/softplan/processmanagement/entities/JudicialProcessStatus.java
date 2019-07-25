package com.softplan.processmanagement.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JudicialProcessStatus {

	private long id;
	private String uniqueProcessId;
	private Date distributionDate;
	private boolean secret;
	private String physicalPath;
	private List<Responsable> responsables;	
	private String description;
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pessoa_id")
    public long getId() {
        return id;
    }
	
		
	
}