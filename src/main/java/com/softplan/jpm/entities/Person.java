package com.softplan.jpm.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="person")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_sequence")
	@SequenceGenerator(name="person_sequence", sequenceName="person_sequence")
	private long id;
	
	private String name;
	
	private String email;
	
	private String document;
	
	//TODO implementar foto
	//private byte[] picture;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	private List<JudicialProcessResponsable> judicialProcessResponsables = new ArrayList<JudicialProcessResponsable>();

	public Person() {

	}

	public Person(long id, String name) {
		this.id = id;
		this.name = name;		
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	
//	@Override
//	public String toString() {
//		return "Responsable{" +
//				"id=" + id +
//				", name='" + name + '\'' +
//				'}';
//	}

}