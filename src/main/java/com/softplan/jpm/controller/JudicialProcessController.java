package com.softplan.jpm.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.service.JudicialProcessService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("judicialprocess")
@EnableSwagger2
public class JudicialProcessController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	@Autowired
	private JudicialProcessService judicialProcessService;

	@PostMapping(path="/", consumes="application/json", produces="application/json")
	public  ResponseEntity<Object> saveJudicialProcces(@Valid @RequestBody JudicialProcess judicialProcess) 
	{		
		try {
			return judicialProcessService.insertNewJudicialProcess(judicialProcess);
		}catch (Exception e) {
			String errorMessage = "Error creating a new Judicial Process. Erro" + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PutMapping(path="/", consumes="application/json", produces="application/json")
	public  ResponseEntity<Object> editJudicialProcces(@Valid @RequestBody JudicialProcess judicialProcess) 
	{					
		try {
			return judicialProcessService.updateJudicialProcess(judicialProcess);
		}catch (Exception e) {
			String errorMessage = "Error updating Judicial Process. Erro" + e.getMessage();
			LOGGER.error("Error updating Judicial Process");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@GetMapping("/")
	public ResponseEntity<Object> findJudicialProcess(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(required = false) String uniqueProcessId, 
			@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate,
			@RequestParam(required = false) Boolean secret,
			@RequestParam(required = false) String physicalPath,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String responsableName){

		try {
			return judicialProcessService.find(page, size, uniqueProcessId, startDate, endDate, secret, physicalPath, status, responsableName);
		}catch (Exception e) {
			String errorMessage = "Error finding a Judicial Process. Erro" + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@GetMapping(path="/{id}", produces = "application/json")
	public ResponseEntity<Object> getByJudicialProcessId(@PathVariable("id") long id) 
	{
		try {
			return judicialProcessService.getById(id);
		}catch (Exception e) {
			String errorMessage = "Error finding a Judicial Process by id: " + id + ". Erro" + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@DeleteMapping(path= "/{id}",  produces = "application/json")
	public ResponseEntity<Object> deleteJudicialProcessById(@PathVariable("id") long id) 
	{
		try {			
			return judicialProcessService.deleteById(id);					
		}catch (Exception e) {
			String errorMessage = "Error deleting a Judicial Process by id: " + id + ". Erro" + e.getMessage(); 
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}	

	@PostMapping(path= "/responsable", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> saveJudicialProcessResponsable(@Valid @RequestBody JudicialProcessResponsable judicialProcessResponsable) 
	{
		try {
			return judicialProcessService.saveJudicialProcessResponsable(judicialProcessResponsable);
		}catch (Exception e) {
			String errorMessage = "Error persisting a Judicial Process Responsable: " + judicialProcessResponsable.toString() + ". Erro" + e.getMessage(); 
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@DeleteMapping(path= "/responsable/{id}",  produces = "application/json")
	public ResponseEntity<Object> deleteJudicialProcessResponsable(@PathVariable("id") long id) 
	{
		try {
			return judicialProcessService.deleteJudicialProcessResponsable(id);
		}catch (Exception e) {
			String errorMessage = "Error deleting a JudicialProcessResponsable by id " + id + ". Error: " + e.getMessage(); 
			LOGGER.error(errorMessage);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}	
}