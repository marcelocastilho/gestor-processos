package com.softplan.jpm.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.jpm.entities.Person;
import com.softplan.jpm.service.PersonService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("person")
@EnableSwagger2
public class PersonController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	private PersonService personService;

	@PostMapping(path= "/", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> savePerson(@Valid @RequestBody Person person) {
		try {

			LOGGER.info("Starting Business rules");
			//Cant insert with same id
			if( personService.getById(person.getId()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists Person with this id.");
			}
			//Unique document will be validated by JPA Entity
			//Valid e-mail will be validated by JPA Entity

			LOGGER.info("Finished Business rules");

			person = personService.persist(person);

			return ResponseEntity.status(HttpStatus.CREATED).body(person);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}

	}

	@PutMapping(path= "/", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> editPerson(@Valid @RequestBody Person person) {
		try {

			if(!personService.getById(person.getId()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + person.getId());
			}
			
			person = personService.persist(person);

			return ResponseEntity.status(HttpStatus.CREATED).body(person);
			
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			e.getStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}

	}

	@GetMapping(path= "/", produces = "application/json")
	public  ResponseEntity<Page<Person>> findPerson(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(required = false) String name, 
			@RequestParam(required = false) String document, 
			@RequestParam(required = false) String email, 
			@RequestParam(required = false, defaultValue = "0") long idProcesso) {
		try {

			Person requestPerson = null;
			long requestidProcesso = idProcesso;
			Page<Person> persons = null;			
		
			if(StringUtils.isNotBlank(name) || StringUtils.isNotBlank(document) || StringUtils.isNotBlank(email) || requestidProcesso > 0) {
				requestPerson = new Person(name, email,document);
				persons = personService.find(requestPerson, requestidProcesso, page, size);
			}else {
				persons = personService.getAll( page, size);
			}

			return ResponseEntity.status(HttpStatus.OK).body(persons);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@GetMapping(path= "/{id}", produces = "application/json")
	public  ResponseEntity<Object> getPersonById(@PathVariable("id") long id) {
		try {
			Optional<Person> person = personService.getById(id);
			if(person.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(person.get());
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + id);
			}	

		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping(path= "/{id}", produces = "application/json")
	public  ResponseEntity<Object> deletePerson(@PathVariable("id") long id) {
		try {
			Optional<Person> person = personService.getById(id);

			if(person.isPresent()) {
				if(!person.get().getJudicialProcessResponsables().isEmpty()) {
					StringBuffer reponsableFor = new StringBuffer();
					person.get().getJudicialProcessResponsables().forEach(jpr -> reponsableFor.append(jpr.getJudicialProcess().getId() + " "));
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this person. There are link with judicial Projects: " + reponsableFor);
				}else {
					personService.delete(person.get());
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find Person with id " + id);
			}			
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}