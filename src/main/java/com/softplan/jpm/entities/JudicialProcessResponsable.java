package com.softplan.jpm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"judicial_process_id", "person_id"})
	}) 
public class JudicialProcessResponsable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="judicialprocessresponsable_sequence")
	@SequenceGenerator(name="judicialprocessresponsable_sequence", sequenceName="jpr_seq")
	private long id;

	@Column(name="judicial_process_id")
	private long judicialProcessId;

	@Column(name="person_id")
	private long personId;

	public long getId() {
		return id;
	}

	public JudicialProcessResponsable() {		
	}

	public JudicialProcessResponsable(long judicialProcessId, long pessoaId) {		
		this.judicialProcessId = judicialProcessId;
		this.personId = pessoaId;		
	}

	public long getJudicialProcessId() {
		return judicialProcessId;
	}

	public void setJudicialProcessId(long judicialProcessId) {
		this.judicialProcessId = judicialProcessId;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long pessoaId) {
		this.personId = pessoaId;
	}	

	@Override
	public String toString() {
		return "JudicialProcessResponsable{" +
				"id=" + id +
				"judicialProcessId=" + judicialProcessId +
				",personId ='" + personId + '\'' +				
				'}';
	}

}