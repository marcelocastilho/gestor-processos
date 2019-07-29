package com.softplan.jdm.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.Person;
import com.softplan.jpmt.service.JudicialProcessResponsableService;
import com.softplan.jpmt.service.JudicialProcessService;
import com.softplan.jpmt.service.PersonService;

@RestController
public class JudicialProcessController
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);
	
	
	@Autowired
	private JudicialProcessService judicialProcessService;

	@Autowired
	private JudicialProcessResponsableService judicialProcessResponsableService;

	@Autowired
	private PersonService personService;

	@PostMapping(path= "/judicialprocess", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> saveJudicialProcces(@RequestBody JudicialProcess judicialProcess) 
	{
		//Validate the data
		try {
			judicialProcess = judicialProcessService.persistJudicialProcess(judicialProcess);

			return ResponseEntity.ok().body(judicialProcess);
		}catch (javax.validation.ConstraintViolationException e) {
			e.printStackTrace();

			return ResponseEntity 	
					.status(HttpStatus.BAD_REQUEST)
					.body("Error Message");
		}catch (Exception e) {
			System.out.println(e.getStackTrace());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	@GetMapping("/judicialprocess")
	public List<JudicialProcess> getAllJudicialProcess() 
	{

		List<JudicialProcess> judicialProcessList = new ArrayList<JudicialProcess>();
		return judicialProcessService.getAllJudicialProcess();
		//Judicial 1
		//JudicialProcess judicialProcess = new JudicialProcess();
		//judicialProcess.setDistributionDate(LocalDate.now());
		//judicialProcess.setId(01);
		//judicialProcess.setUniqueProcessId("aaaa9999");
		//judicialProcess.setDescription("Desc1");
		//judicialProcess.setPhysicalPath("physicalPath1");
		//
		//JudicialProcessResponsable judicialProcessResponsable = new JudicialProcessResponsable();
		//judicialProcessResponsable.setJudicialProcessId(01);
		//judicialProcessResponsable.setPessoaId(01);
		//
		//judicialProcess.addJudicialProcessResponsables(judicialProcessResponsable);
		//
		////Judicial 2
		//JudicialProcess judicialProcess2 = new JudicialProcess();
		//judicialProcess2.setDistributionDate(LocalDate.now());
		//judicialProcess2.setId(02);
		//judicialProcess2.setUniqueProcessId("bbbb0000");
		//judicialProcess2.setDescription("Desc2");
		//judicialProcess2.setPhysicalPath("physicalPath2");
		//
		//JudicialProcessResponsable judicialProcessResponsable2 = new JudicialProcessResponsable();
		//judicialProcessResponsable2.setJudicialProcessId(01);
		//judicialProcessResponsable2.setPessoaId(01);
		//
		//judicialProcess2.addJudicialProcessResponsables(judicialProcessResponsable2);
		//
		//
		////Add all to test
		//judicialProcessList.add(judicialProcess);
		//judicialProcessList.add(judicialProcess2);
		//return judicialProcessList;
	}

	@GetMapping(path="/judicialprocess/{id}", produces = "application/json")
	public ResponseEntity<Object> getAllJudicialProcess(@PathVariable("id") long id) 
	{
		try {
			JudicialProcess judicialProcess = judicialProcessService.getById(id);

			return ResponseEntity.status(HttpStatus.OK).body(judicialProcess);

		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	@PostMapping(path= "/judicialprocess/responsable", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> saveJudicialProcessResponsable(@RequestBody JudicialProcessResponsable judicialProcessResponsable) 
	{
		try {
			judicialProcessResponsable = judicialProcessResponsableService.persistJudicialProcess(judicialProcessResponsable);
			return ResponseEntity.ok().body(judicialProcessResponsable);			
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	@GetMapping(path= "/judicialprocess/{id}/responsable",  produces = "application/json")
	public ResponseEntity<JudicialProcessResponsable> getResponsablesByJudicialProcessId(@PathVariable("id") long id) 
	{
		try {
			JudicialProcessResponsable judicialProcessResponsable = judicialProcessResponsableService.getJudicialProcessResponsableByJudicialProcessId(id);

			return ResponseEntity.status(HttpStatus.OK).body(judicialProcessResponsable);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@PostMapping(path= "/person", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> savePerson(@RequestBody Person person) 
	{
		try {
			person = personService.persistPerson(person);
			
			return ResponseEntity.ok().body(person);		
			
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}

	}

	@GetMapping(path= "/person", produces = "application/json")
	public  ResponseEntity<Object> findPerson(@RequestParam(required = false) String name, @RequestParam(required = false) String document, @RequestParam(required = false) String email) 
	{
		try {
			
			LOGGER.debug("name = " + name);
			LOGGER.debug("document = " + document);
			LOGGER.debug("email = " + email);
			
			List<Person> persons = null;
			
			if(StringUtils.isNotBlank(name) || StringUtils.isNotBlank(document) || StringUtils.isNotBlank(email)) {
				persons = personService.findByName(name, null, null);
			}else {
				persons = personService.getAllPerson();
			}

			return ResponseEntity.status(HttpStatus.OK).body(persons);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
	@GetMapping(path= "/person/{id}", produces = "application/json")
	public  ResponseEntity<Object> getPersonById(@PathVariable("id") long id) 
	{
		try {
			Person person = personService.getPersonById(id);

			return ResponseEntity.status(HttpStatus.OK).body(person);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}