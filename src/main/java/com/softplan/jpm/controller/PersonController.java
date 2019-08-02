package com.softplan.jpm.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
			return personService.savePerson(person);
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error inserting a Responsable" + person.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PutMapping(path= "/", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> editPerson(@Valid @RequestBody Person person) {
		try {		
			return personService.editPerson(person);
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error inserting a Responsable" + person.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	@GetMapping(path= "/", produces = "application/json")
	public  ResponseEntity<Object> findPerson(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(required = false) String name, 
			@RequestParam(required = false) String document, 
			@RequestParam(required = false) String email, 
			@RequestParam(required = false, defaultValue = "0") long idProcesso) {
		
		try {		
			return personService.find(page, size, name, document, email, idProcesso);		
		}catch (Exception e) {
			String errorMessage = "Error finding a Responsable. Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@GetMapping(path= "/{id}", produces = "application/json")
	public  ResponseEntity<Object> getPersonById(@PathVariable("id") long id) {
		try {		
			return personService.getById(id);		
		}catch (Exception e) {
			String errorMessage = "Error finding a Responsable by id: " + id + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@DeleteMapping(path= "/{id}", produces = "application/json")
	public  ResponseEntity<Object> deletePerson(@PathVariable("id") long id) {
		try {
			return personService.deletePerson(id);		
		}catch (Exception e) {
			String errorMessage = "Error deleting a Responsable by id: " + id + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
}