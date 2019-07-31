package com.softplan.jpmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.Person;
import com.softplan.jpm.jpa.repository.CustomPersonRepository;
import com.softplan.jpm.jpa.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private CustomPersonRepository customPersonRepository;

	public Optional<Person> getById(long id) {		
		return personRepository.findById(id);
	}
	
	public List<Person> getAll() {
		List<Person> responsable = personRepository.findAll();
		return responsable;
	}
	
	public List<Person> find(Person person, long requestidProcesso) {		
		
		return customPersonRepository.findPerson(person, requestidProcesso);
	}
	
	public Person persist(Person person) {		
		return personRepository.save(person);
	}
	
	public void delete(Person person) {
		personRepository.delete(person);
	}	
	
	public void deleteById(long id) {
		personRepository.deleteById(id);
	}	
}
