package com.softplan.jpm.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.softplan.jpm.controller.PersonController;
import com.softplan.jpm.entities.Person;
import com.softplan.jpm.jpa.repository.CustomPersonRepository;
import com.softplan.jpm.jpa.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CustomPersonRepository customPersonRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

	public  ResponseEntity<Object> savePerson(Person person) {
		try {

			LOGGER.info("Starting Business rules");

			//Cant insert with same id
			if( personRepository.findById(person.getId()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists Person with this id.");
			}

			//Unique document will be validated by JPA Entity
			//Valid e-mail will be validated by JPA Entity

			LOGGER.info("Finished Business rules");

			person = persist(person);

			return ResponseEntity.status(HttpStatus.CREATED).body(person);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error inserting a Responsable: " + person.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	public  ResponseEntity<Object> editPerson(Person person) {
		try {

			if(!personRepository.findById(person.getId()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + person.getId());
			}
			person = persist(person);

			return ResponseEntity.status(HttpStatus.CREATED).body(person);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error inserting a Responsable" + person.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	public ResponseEntity<Object> getById(long id) {
		try {
			Optional<Person> person = personRepository.findById(id);
			if(person.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(person.get());
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + id);
			}			
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error finding Responsable. Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}		
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

	public ResponseEntity<Object> find(int page, int size, String name, String document, String email, long idProcesso) {		

		try {
			Person person = null;
			long requestidProcesso = idProcesso;
			Page<Person> persons = null;			

			if(StringUtils.isNotBlank(name) || StringUtils.isNotBlank(document) || StringUtils.isNotBlank(email) || requestidProcesso > 0) {
				person = new Person(name, email,document);

				PageRequest pageRequest = PageRequest.of(	
						page,
						size,
						Sort.Direction.ASC,
						"id");

				persons = customPersonRepository.findPerson(person, requestidProcesso, pageRequest);

			}else {
				persons = getAll( page, size);
			}

			return ResponseEntity.status(HttpStatus.OK).body(persons);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error finding Responsable. Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}

	}
	
	public  ResponseEntity<Object> deletePerson(long id) {
		try {
			Optional<Person> person = personRepository.findById(id);

			if(person.isPresent()) {
				if(!person.get().getJudicialProcessResponsables().isEmpty()) {
					StringBuffer reponsableFor = new StringBuffer();
					person.get().getJudicialProcessResponsables().forEach(jpr -> reponsableFor.append(jpr.getJudicialProcess().getId() + " "));
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this person. There are link with judicial Projects: " + reponsableFor);
				}else {
					delete(person.get());
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + id);
			}			
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error deleting Responsable id: " + id + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
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
	
	public Optional<Person> getPersonById(long id) {
		return personRepository.findById(id);
	}
}
