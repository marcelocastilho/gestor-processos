package com.softplan.jdm.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpmt.service.JudicialProcessResponsableService;
import com.softplan.jpmt.service.JudicialProcessService;

@RestController
@RequestMapping("judicialprocess")
public class JudicialProcessController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	@Autowired
	private JudicialProcessService judicialProcessService;

	@Autowired
	private JudicialProcessResponsableService judicialProcessResponsableService;
	
	@PostMapping(path="/", consumes="application/json", produces="application/json")
	public  ResponseEntity<Object> saveJudicialProcces(@RequestBody JudicialProcess judicialProcess) 
	{
		//Validate the data
		try {
			judicialProcess = judicialProcessService.persist(judicialProcess);

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

	@GetMapping("/")
	public List<JudicialProcess>findJudicialProcess() 
	{

		List<JudicialProcess> judicialProcessList = judicialProcessService.getAll();
		return judicialProcessList;

	}

	@GetMapping(path="/{id}", produces = "application/json")
	public ResponseEntity<Object> getByJudicialProcessId(@PathVariable("id") long id) 
	{
		try {
			Optional<JudicialProcess> judicialProcess = judicialProcessService.getById(id);
			if(judicialProcess.isPresent()) {
					
				return ResponseEntity.status(HttpStatus.OK).body(judicialProcess.get());
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess with id " + id);
			}	

		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	@DeleteMapping(path= "/{id}",  produces = "application/json")
	public ResponseEntity<Object> deleteJudicialProcessById(@PathVariable("id") long id) 
	{
		try {
			
			Optional<JudicialProcess> judicialProcess = judicialProcessService.getById(id) ;
			
			if(judicialProcess.isPresent()) {
				
				JudicialProcess judicialProcessChild = judicialProcessService.findChild(judicialProcess.get().getId());
				
				//validate if exists child or is in closed status
				if(judicialProcess.get().getStatus().isClosed() || judicialProcessChild != null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this JudicialProcess. There JudicialProcess is Closed or has child");
				}else {
					judicialProcessService.delete(judicialProcess.get());			
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}
				
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess with id " + id);
			}			
		}catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping(path= "/responsable", produces = "application/json")
	public  ResponseEntity<Object> findJudicialProcessResponsable(
			@RequestParam(required = false, defaultValue = "0") long judicialProcessId,
			@RequestParam(required = false, defaultValue = "0") long personId) 
	{
		try {

			List<JudicialProcessResponsable> judicialProcessResponsables = null;

			if(judicialProcessId > 0 || personId > 0) {
				JudicialProcessResponsable requestJudicialProcessResponsable = new JudicialProcessResponsable(judicialProcessId, personId);
				judicialProcessResponsables = judicialProcessResponsableService.find(requestJudicialProcessResponsable);
			}else {
				judicialProcessResponsables = judicialProcessResponsableService.getAll();
			}

			return ResponseEntity.status(HttpStatus.OK).body(judicialProcessResponsables);
		}catch (Exception e) {
			System.out.println(e.getStackTrace());

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	@PostMapping(path= "/responsable", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> saveJudicialProcessResponsable(@RequestBody JudicialProcessResponsable judicialProcessResponsable) 
	{
		try {	
			if(judicialProcessResponsable.getJudicialProcessId() > 0 && judicialProcessResponsable.getPersonId() > 0) {				
				judicialProcessResponsable = judicialProcessResponsableService.persist(judicialProcessResponsable);
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot persist JudicialProcessResponsable with '0' parameters.");
			}

			return ResponseEntity.status(HttpStatus.OK).body(judicialProcessResponsable);
		}catch (Exception e) {
				System.out.println(e.getStackTrace());

				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(e.getStackTrace());
			}
		}
	
	@DeleteMapping(path= "/responsable/{id}",  produces = "application/json")
	public ResponseEntity<Object> deleteJudicialProcessResponsable(@PathVariable("id") long id) 
		{
			try {
				Optional<JudicialProcessResponsable> judicialProcessResponsable = judicialProcessResponsableService.getById(id);
				
				if(judicialProcessResponsable.isPresent()) {
					
					Optional<JudicialProcess> judicialProcess = judicialProcessService.getById(judicialProcessResponsable.get().getJudicialProcessId());
										
					if(judicialProcess.isPresent() && judicialProcess.get().getStatus().isClosed()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this Responsable. There are closed JudicialProcess linked to this");
					}else {
						judicialProcessResponsableService.deleteById(id);
						return ResponseEntity.status(HttpStatus.OK).body(null);
					}					
				}else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess responsable with id " + id);					
				}
			}catch (Exception e) {
				e.printStackTrace();

				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
	}	
}