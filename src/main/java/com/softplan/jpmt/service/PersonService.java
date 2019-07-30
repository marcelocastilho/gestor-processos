package com.softplan.jpmt.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
	
	EntityManager em;
	
	private EntityManager entityMgrPerson= null;

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
	
	public List<Person> findPerson(Person person, long requestidProcesso) {
		
		return customPersonRepository.findPerson(person, requestidProcesso);
	}
	
}
