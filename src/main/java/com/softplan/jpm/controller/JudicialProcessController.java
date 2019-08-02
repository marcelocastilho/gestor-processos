package com.softplan.jpm.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
import com.softplan.jpm.entities.Person;
import com.softplan.jpm.enun.JudicialProcessStatusEnum;
import com.softplan.jpm.exceptionhandling.RestBusinessValidation;
import com.softplan.jpm.jpa.constant.EmailMessageConstants;
import com.softplan.jpm.service.EmailService;
import com.softplan.jpm.service.JudicialProcessResponsableService;
import com.softplan.jpm.service.JudicialProcessService;
import com.softplan.jpm.service.PersonService;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("judicialprocess")
@EnableSwagger2
public class JudicialProcessController
{

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	@Autowired
	private JudicialProcessService judicialProcessService;

	@Autowired
	private JudicialProcessResponsableService judicialProcessResponsableService;

	@Autowired
	private PersonService personService;

	@PostMapping(path="/", consumes="application/json", produces="application/json")
	public  ResponseEntity<Object> saveJudicialProcces(@Valid @RequestBody JudicialProcess judicialProcess) 
	{
		//Validate the data
		try {

			LOGGER.info("Starting Business rules");
			//Cant insert with same id
			if( judicialProcessService.getById(judicialProcess.getId()).isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists Judicial Process with this id.");
			}
			//UniqueId will be validated by JPA Entity
			//Non nullable status will be validated by JPA Entity

			//Validate if Judicial Process has at least one responsable
			if(judicialProcess.getJudicialProcessResponsable() == null || judicialProcess.getJudicialProcessResponsable().size() < 1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There are no responsable. You need to insert at least one.");	
			}

			//Validate if Judicial Process has more than 3 responsables
			if( judicialProcess.getJudicialProcessResponsable().size() > RestBusinessValidation.MAX_RESPONSABLE_NUMBER) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Max number of responsables are 3");
			}

			//Validate if the person linked exists
			for (JudicialProcessResponsable judicialProcessResponsable : judicialProcess.getJudicialProcessResponsable()) {								
				if(!personService.getById(judicialProcessResponsable.getPersonId()).isPresent()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person does not exists: " + judicialProcessResponsable.getPersonId());
				}
			}

			//Validate data <= now
			if(judicialProcess.getDistributionDate().isAfter(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The DistributionDate must be before actual Date.");
			}

			LOGGER.info("Finished Business rules");			

			judicialProcess = judicialProcessService.persist(judicialProcess);

			//send e-mail
			for (JudicialProcessResponsable judicialProcessResponsable : judicialProcess.getJudicialProcessResponsable()) {			
				Person responsable = personService.getById(judicialProcessResponsable.getPersonId()).get();
				LOGGER.info("Sending email to: " + responsable.getEmail());
				EmailService.sendMail(responsable, EmailMessageConstants.NEW_RESPONSABLE_NEW_PROJECT +  judicialProcess.getUniqueProcessId());
			}

			return ResponseEntity.status(HttpStatus.CREATED).body(judicialProcess);
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

	@PutMapping(path="/", consumes="application/json", produces="application/json")
	public  ResponseEntity<Object> editJudicialProcces(@Valid @RequestBody JudicialProcess judicialProcessIn) 
	{
		//Validate the data
		try {

			Optional<JudicialProcess> persistedJudicialProcess = judicialProcessService.getById(judicialProcessIn.getId());
			if(!persistedJudicialProcess.isPresent()) {			
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess with id " + judicialProcessIn.getId());
			}

			LOGGER.info("Starting Business rules");
			//Cant change closed projects
			if(persistedJudicialProcess.get().getStatus().isClosed()){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot edit closed JudicialProcess.");
			}

			//Validate data <= now
			if(judicialProcessIn.getDistributionDate().isAfter(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The DistributionDate must be before actual Date.");
			}
			//Validate if Judicial Process has more than constant MAX_RESPONSABLE_NUMBER

			//merge responsables
			
			//remove Responsable already saved
			for (JudicialProcessResponsable actualJudicialProcessResponsable : persistedJudicialProcess.get().getJudicialProcessResponsable()) {
				for (JudicialProcessResponsable judicialProcessResponsableIn : judicialProcessIn.getJudicialProcessResponsable()) {
					if(judicialProcessResponsableIn.getPersonId() == actualJudicialProcessResponsable.getPersonId()) {
						judicialProcessIn.removeJudicialProcessResponsable(judicialProcessResponsableIn);
						break;
					}
				}
			}
			
			List<JudicialProcessResponsable> mergedResponsables = new ArrayList<JudicialProcessResponsable>();
			mergedResponsables.addAll(persistedJudicialProcess.get().getJudicialProcessResponsable());
			mergedResponsables.addAll(judicialProcessIn.getJudicialProcessResponsable());

			mergedResponsables.stream().filter(JudicialProcessResponsable.distinctByKey(JudicialProcessResponsable::getPersonId));

			//TODO validate if all received responsables are valids
			int actualNumberOfResponsables = mergedResponsables.size();				

			if( actualNumberOfResponsables < 1) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There are no responsable. You need to insert at least one!");
			}
			//UniqueProcessId is validated by JPA Entity

			if( actualNumberOfResponsables > RestBusinessValidation.MAX_RESPONSABLE_NUMBER) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Max number of responsables are 3");
			}

			//find new responsables			
			List<Person> sendMailList = new ArrayList<Person>();
			//List<JudicialProcessResponsable> newResponsableslist = new ArrayList<JudicialProcessResponsable>
			for (JudicialProcessResponsable mergedJudicialProcessResponsable : mergedResponsables) {
				boolean newResponsable = true;

				for (JudicialProcessResponsable actualJudicialProcessResponsable : persistedJudicialProcess.get().getJudicialProcessResponsable()) {
					if(actualJudicialProcessResponsable.getPersonId() == mergedJudicialProcessResponsable.getPersonId()) {
						newResponsable = false;
						break;
					}
				}

				if(newResponsable) {
					Optional<Person> person =  personService.getById(mergedJudicialProcessResponsable.getPersonId());
					//Validate if new Responsable is valid Person and add to sendMailList
					if(person.isPresent()) {
						persistedJudicialProcess.get().addJudicialProcessResponsable(mergedJudicialProcessResponsable);
						sendMailList.add(person.get());
					}else{
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person does not exists: " + mergedJudicialProcessResponsable.getPersonId());
					}
				}				
			}
			LOGGER.info("Finished Business rules");

			//change old responsables for new responsables
			judicialProcessIn.setJudicialProcessResponsable(persistedJudicialProcess.get().getJudicialProcessResponsable());			
			
			//update JudicialProcess
			JudicialProcess updatedJudicialProcess = judicialProcessService.persist(judicialProcessIn);
			
			//SendMail
			for (Person person : sendMailList) {
				LOGGER.info("Sending email to: " + person.getEmail());							
				EmailService.sendMail(person, EmailMessageConstants.NEW_RESPONSABLE_OLD_PROJECT +  judicialProcessIn.getUniqueProcessId());				
			}
			
			return ResponseEntity.ok().body(updatedJudicialProcess);			
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

	@GetMapping("/")
	public ResponseEntity<Page<JudicialProcess>> findJudicialProcess(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@RequestParam(required = false) String uniqueProcessId, 
			@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate,
			@RequestParam(required = false) Boolean secret,
			@RequestParam(required = false) String physicalPath,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String responsableName){

		Page<JudicialProcess> judicialProcessList;

		if(uniqueProcessId != null || startDate != null ||
				endDate != null || secret != null ||
				status != null || physicalPath != null ||  
				responsableName != null ) {

			Optional<JudicialProcessStatusEnum> optEnumStatus = Arrays.stream(JudicialProcessStatusEnum.values()).filter(l -> l.getStatus().equals(status)).findFirst();
			JudicialProcessStatusEnum enumStatus = null;
			if(optEnumStatus.isPresent()) {
				enumStatus = optEnumStatus.get();
			}
			JudicialProcess judicialProcessRequest = new JudicialProcess(uniqueProcessId, secret, null, enumStatus, physicalPath);

			judicialProcessList = judicialProcessService.find(judicialProcessRequest, startDate, endDate, responsableName, page, size);
		}else {
			judicialProcessList = judicialProcessService.getAll( page, size);			
		}

		return ResponseEntity.status(HttpStatus.OK).body(judicialProcessList);

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

				JudicialProcess judicialProcessChild = judicialProcess.get().getChildJudicialProcess();

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

//Removed
//	@GetMapping(path= "/responsable", produces = "application/json")
//	public  ResponseEntity<Object> findJudicialProcessResponsable(
//			@RequestParam(required = false, defaultValue = "0") long judicialProcessId,
//			@RequestParam(required = false, defaultValue = "0") long personId) 
//	{
//		try {
//
//			List<JudicialProcessResponsable> judicialProcessResponsables = null;
//
//			if(judicialProcessId > 0 || personId > 0) {
//				JudicialProcess jp = new JudicialProcess();
//				jp.setId(judicialProcessId);
//				JudicialProcessResponsable requestJudicialProcessResponsable = new JudicialProcessResponsable(jp, personId);
//				judicialProcessResponsables = judicialProcessResponsableService.find(requestJudicialProcessResponsable);
//			}else {
//				judicialProcessResponsables = judicialProcessResponsableService.getAll();
//			}
//
//			return ResponseEntity.status(HttpStatus.OK).body(judicialProcessResponsables);
//		}catch (Exception e) {
//			e.getStackTrace();
//
//			return ResponseEntity
//					.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(e.getStackTrace());
//		}
//	}

	@PostMapping(path= "/responsable", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> saveJudicialProcessResponsable(@Valid @RequestBody JudicialProcessResponsable judicialProcessResponsable) 
	{
		try {

			LOGGER.info("Starting Business rules");

			if(judicialProcessResponsable.getJudicialProcess().getId() > 0 && judicialProcessResponsable.getPersonId() > 0) {
				Optional<JudicialProcess> judicialProcess = judicialProcessService.getById(judicialProcessResponsable.getJudicialProcess().getId());
				//Validate if there are minus than 3 responsables
				if(judicialProcess.isPresent() && judicialProcess.get().getJudicialProcessResponsable().size() >= RestBusinessValidation.MAX_RESPONSABLE_NUMBER) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists 3 Responsables, impossible add more!");
				}
			}else{ 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).
						body("You cannot persist JudicialProcessResponsable with '0' parameters."); 
			}
			LOGGER.info("Finished Business rules");

			judicialProcessResponsable = judicialProcessResponsableService.persist(judicialProcessResponsable); 

			return ResponseEntity.status(HttpStatus.CREATED).body(judicialProcessResponsable);

		}catch (Exception e) {
			e.getStackTrace();

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

				Optional<JudicialProcess> judicialProcess = judicialProcessService.getById(judicialProcessResponsable.get().getJudicialProcess().getId());

				LOGGER.info("Starting Business rules");
				if(judicialProcess.isPresent() && judicialProcess.get().getStatus().isClosed()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this Responsable. There are closed JudicialProcess linked to this");
				}
				LOGGER.info("Finished Business rules");


				judicialProcessResponsableService.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK).body(null);

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