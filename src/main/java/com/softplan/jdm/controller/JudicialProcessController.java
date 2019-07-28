package com.softplan.jdm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.People;
import com.softplan.jpmt.service.JudicialProcessResponsableService;
import com.softplan.jpmt.service.JudicialProcessService;
import com.softplan.jpmt.service.PeopleService;

@RestController
public class JudicialProcessController
{
	@Autowired
	private JudicialProcessService judicialProcessService;

	@Autowired
	private JudicialProcessResponsableService judicialProcessResponsableService;

	@Autowired
	private PeopleService peopleService;

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

	@GetMapping(path= "/judicialprocess/{id}/responsables",  produces = "application/json")
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

	@PostMapping(path= "/people", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> savePeople(@RequestBody People people) 
	{
		try {
			people = peopleService.persistPeople(people);
			
			return ResponseEntity.ok().body(people);		
			
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}

	}

	@GetMapping(path= "/people", produces = "application/json")
	public  ResponseEntity<Object> getAllPeople() 
	{
		try {
			List<People> peoples = peopleService.getAllPeople();

			return ResponseEntity.status(HttpStatus.OK).body(peoples);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
	@GetMapping(path= "/people/{id}", produces = "application/json")
	public  ResponseEntity<Object> getPeopleById(@PathVariable("id") long id) 
	{
		try {
			People people = peopleService.getPeopleById(id);

			return ResponseEntity.status(HttpStatus.OK).body(people);
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}