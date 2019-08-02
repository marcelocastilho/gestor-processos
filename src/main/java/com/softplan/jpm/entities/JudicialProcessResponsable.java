package com.softplan.jpm.entities;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	
	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "judicial_process_id")
    private JudicialProcess judicialProcess;
	
	@Column(name="person_id", nullable = false)
	private long personId;

	public long getId() {
		return id;
	}

	public JudicialProcessResponsable() {		
	}

	public JudicialProcessResponsable(JudicialProcess judicialProcess, long pessoaId) {		
		this.judicialProcess = judicialProcess;
		this.personId = pessoaId;		
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long pessoaId) {
		this.personId = pessoaId;
	}
	
	public JudicialProcess getJudicialProcess() {
		return judicialProcess;
	}

	public void setJudicialProcess(JudicialProcess judicialProcess) {
		this.judicialProcess = judicialProcess;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	@Override
	public String toString() {
		return "JudicialProcessResponsable{" +
				"id=" + id +
				"judicialProcess=" + judicialProcess.toString() +
				",personId ='" + personId + '\'' +				
				'}';
	}
}