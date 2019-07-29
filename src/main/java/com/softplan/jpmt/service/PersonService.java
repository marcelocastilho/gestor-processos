package com.softplan.jpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.Person;
import com.softplan.jpm.jpa.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	public List<Person> getAllPerson() {
		List<Person> responsable = personRepository.findAll();
		return responsable;
	}
	
	public Person persistPerson(Person person) {
		
		return personRepository.save(person);
	}
	
	public Person getPersonById(long id) {
		
		return personRepository.getOne(id);
	}
	
	public List<Person> findByName(String name, String document, String judicialProcessId) {
		List<Person> responsable = personRepository.findByName(name);
		return responsable;
	}
}
