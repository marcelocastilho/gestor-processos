package com.softplan.jpm.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Person{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_sequence")
	@SequenceGenerator(name="person_sequence", sequenceName="person_sequence")
	private long id;
	
	@NotBlank(message = "{person.name.not.blank}")
	private String name;
	
	@NotNull
	@NotBlank(message = "{person.email.not.blank}")
    @Email(message="{person.email.not.valid}")
    @Size(max = 100, message="{person.email.size.not.valid}")
	private String email;
	
	@NotBlank(message = "{person.document.not.blank}")
	@Column(unique=true)
	@Pattern(regexp = "([0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}|[0-9]{11})",message="{person.document.not.valid}")
	private String document;
	
	//TODO implementar foto
	//private byte[] picture;
	
	//@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "person_id")
	private List<JudicialProcessResponsable> judicialProcessResponsables = new ArrayList<JudicialProcessResponsable>();

	public Person() {
	}

	public Person(String name, String email, String document) {
		this.name = name;		
		this.email = email;
		this.document = document;
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

	public List<JudicialProcessResponsable> getJudicialProcessResponsables() {		
		return judicialProcessResponsables;
	}	
	
	@Override
	public String toString() {
		return "Responsable{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}

}