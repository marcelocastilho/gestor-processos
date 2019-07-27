package com.softplan.jpm.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Responsable{
	
	private long id;
	private String name;
	
//	public Responsable(String name) {
//		this.name = name;
//		this.id = getId();
//	}	
	
	@ManyToMany(mappedBy = "responsables")
	private Set<JudicialProcess> judicialProcess;
		
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)    
    public long getId() {
        return id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
}