package com.softplan.jpm.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JudicialProcessResponsable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2428462941751714079L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="judicialProcessResponsable_sequence")
	@SequenceGenerator(name="judicialProcessResponsable_sequence", sequenceName="jpr_seq")
	private long id;

	@Column(name="judicial_process_id")
	private long judicialProcessId;

	@Column(name="people_id")
	private long peopleId;

	public long getId() {
		return id;
	}

	public JudicialProcessResponsable() {		
	}

	public JudicialProcessResponsable(long judicialProcessId, long pessoaId) {		
		this.judicialProcessId = judicialProcessId;
		this.peopleId = pessoaId;		
	}

	public long getJudicialProcessId() {
		return judicialProcessId;
	}

	public void setJudicialProcessId(long judicialProcessId) {
		this.judicialProcessId = judicialProcessId;
	}

	public long getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(long pessoaId) {
		this.peopleId = pessoaId;
	}	

	@Override
	public String toString() {
		return "JudicialProcessResponsable{" +
				"id=" + id +
				"judicialProcessId=" + judicialProcessId +
				",peopleId ='" + peopleId + '\'' +				
				'}';
	}

}