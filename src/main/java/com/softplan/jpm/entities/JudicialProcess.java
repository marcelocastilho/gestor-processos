package com.softplan.jpm.entities;

import java.time.LocalDate;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;


@Entity
public class JudicialProcess {
	
	private long id;
	@Size(min = 10)
	private String uniqueProcessId;
	private LocalDate distributionDate;
	private boolean secret;
	private String physicalPath;
	
	@Column(columnDefinition = "text")
	private String description;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)    
    public long getId() {
        return id;
    }	
	
	@ManyToMany
	@JoinTable(name = "joinTable", 
			   joinColumns= {@JoinColumn(name="JudicialProcessId")}, 
			   inverseJoinColumns= {@JoinColumn(name="ResponsableId")}
	)
	private HashSet<Responsable> responsables;

	public String getUniqueProcessId() {
		return uniqueProcessId;
	}

	public void setUniqueProcessId(String uniqueProcessId) {
		this.uniqueProcessId = uniqueProcessId;
	}

	public LocalDate getDistributionDate() {
		return distributionDate;
	}

	public void setDistributionDate(LocalDate distributionDate) {
		this.distributionDate = distributionDate;
	}

	public boolean isSecret() {
		return secret;
	}

	public void setSecret(boolean secret) {
		this.secret = secret;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}

	public void setPhysicalPath(String physicalPath) {
		this.physicalPath = physicalPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void addResponsable(Responsable responsable) {
		if(this.responsables == null) {
			this.responsables = new HashSet<Responsable>();
		}	
		responsables.add(responsable);		
	}

	public HashSet<Responsable> getResponsables() {
		return responsables;
	}

	public void setResponsables(HashSet<Responsable> responsables) {
		this.responsables = responsables;
	}
	
}