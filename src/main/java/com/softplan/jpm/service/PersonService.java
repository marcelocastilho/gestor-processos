package com.softplan.jpm.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	public Page<Person> getAll(int page, int size) {
				
		PageRequest pageRequest = PageRequest.of(
				page,
				size,
				Sort.Direction.ASC,
				"name");
		return new PageImpl<>(
				personRepository.findAll(), 
				pageRequest, size);
	}

	public Page<Person> find(Person person, long requestidProcesso, int page, int size) {		

		PageRequest pageRequest = PageRequest.of(	
				page,
				size,
				Sort.Direction.ASC,
				"id");

		return customPersonRepository.findPerson(person, requestidProcesso, pageRequest);

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
