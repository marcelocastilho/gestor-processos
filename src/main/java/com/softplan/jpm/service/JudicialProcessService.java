package com.softplan.jpm.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.softplan.jpm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.Person;
import com.softplan.jpm.enun.JudicialProcessStatusEnum;
import com.softplan.jpm.exceptionhandling.RestBusinessValidation;
import com.softplan.jpm.jpa.constant.EmailMessageConstants;
import com.softplan.jpm.jpa.repository.CustomJudicialProcessRepository;
import com.softplan.jpm.jpa.repository.JudicialProcessRepository;

@Service
public class JudicialProcessService {

	@Autowired
	private JudicialProcessRepository judicialProcessRepository;

	@Autowired
	private CustomJudicialProcessRepository customjudicialProcessRepository;

	@Autowired
	private JudicialProcessResponsableService judicialProcessResponsableService;

	@Autowired
	private PersonService personService;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	public ResponseEntity<Object> insertNewJudicialProcess(JudicialProcess judicialProcess){
		try {
			LOGGER.info("Starting Business rules");

			//Cant insert with same id
			if( judicialProcessRepository.findById(judicialProcess.getId()).isPresent()) {
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
				if(!personService.getPersonById(judicialProcessResponsable.getPersonId()).isPresent()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Person does not exists: " + judicialProcessResponsable.getPersonId());
				}
			}

			//Validate data <= now
			if(judicialProcess.getDistributionDate().isAfter(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The DistributionDate must be before actual Date.");
			}

			LOGGER.info("Finished Business rules");			

			judicialProcess = persist(judicialProcess);

			//send e-mail
			for (JudicialProcessResponsable judicialProcessResponsable : judicialProcess.getJudicialProcessResponsable()) {			
				Person responsable = personService.getPersonById(judicialProcessResponsable.getPersonId()).get();
				LOGGER.info("Sending email to: " + responsable.getEmail());
				EmailService.sendMail(responsable, EmailMessageConstants.NEW_RESPONSABLE_NEW_PROJECT +  judicialProcess.getUniqueProcessId());
			}

			return ResponseEntity.status(HttpStatus.CREATED).body(judicialProcess);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error inserting a Judicial Process" + judicialProcess.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	public ResponseEntity<Object> updateJudicialProcess(JudicialProcess judicialProcessIn){

		try {

			Optional<JudicialProcess> persistedJudicialProcess = judicialProcessRepository.findById(judicialProcessIn.getId());
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
					Optional<Person> person =  personService.getPersonById(mergedJudicialProcessResponsable.getPersonId());
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
			JudicialProcess updatedJudicialProcess = persist(judicialProcessIn);

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
			String errorMessage = "Error updating a Judicial Process: " + judicialProcessIn.toString() + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getStackTrace());
		}
	}

	public ResponseEntity<Object> getById(long id) {

		try{ Optional<JudicialProcess> judicialProcess = judicialProcessRepository.findById(id);

		if(!judicialProcess.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess with id " + id);
		}

		return ResponseEntity.status(HttpStatus.OK).body(judicialProcess.get());
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error deleting a Judicial Process by id: " + id +". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	public Page<JudicialProcess> getAll(int page, int size) {

		PageRequest pageRequest = PageRequest.of(
				page,
				size,
				Sort.Direction.ASC,
				"name");
		return new PageImpl<>(
				judicialProcessRepository.findAll(), 
				pageRequest, size);
	}

	public ResponseEntity<Object> find(int page, int size, String uniqueProcessId, LocalDate startDate, LocalDate endDate, Boolean secret, String physicalPath, String status, String responsableName) {

		Page<JudicialProcess> judicialProcessList;

		try {
			//Verify request parameters != null or else findAll
			if(uniqueProcessId != null || startDate != null ||
					endDate != null || secret != null ||
					status != null || physicalPath != null ||  
					responsableName != null ) {

				Optional<JudicialProcessStatusEnum> optEnumStatus = Arrays.stream(JudicialProcessStatusEnum.values()).filter(l -> l.getStatus().equals(status)).findFirst();
				JudicialProcessStatusEnum enumStatus = null;
				if(optEnumStatus.isPresent()) {
					enumStatus = optEnumStatus.get();
				}
				JudicialProcess judicialProcess = new JudicialProcess(uniqueProcessId, secret, null, enumStatus, physicalPath);

				PageRequest pageRequest = PageRequest.of(	
						page,
						size,
						Sort.Direction.ASC,
						"id");

				judicialProcessList = customjudicialProcessRepository.findJudicialProcess(judicialProcess, startDate, endDate, responsableName, pageRequest);
			}else {
				judicialProcessList = getAll( page, size);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(judicialProcessList);
			
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error finding Judicial Process. Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	public JudicialProcess persist(JudicialProcess judicialProcess) {
		return judicialProcessRepository.save(judicialProcess);
	}

	public ResponseEntity<Object> deleteById(long id) {

		try {
			Optional<JudicialProcess> judicialProcess = judicialProcessRepository.findById(id) ;

			if(judicialProcess.isPresent()) {

				JudicialProcess judicialProcessChild = judicialProcess.get().getChildJudicialProcess();

				//validate if exists child or is in closed status
				if(judicialProcess.get().getStatus().isClosed() || judicialProcessChild != null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this JudicialProcess. There JudicialProcess is Closed or has child");
				}
				judicialProcessRepository.deleteById(id);

			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot find JudicialProcess with id " + id);
			}
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {				
			String errorMessage = "Error deleting a Judicial Process id " + id + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	public ResponseEntity<Object> saveJudicialProcessResponsable(JudicialProcessResponsable judicialProcessResponsable){
		try {

			LOGGER.info("Starting Business rules");

			if(judicialProcessResponsable.getJudicialProcess().getId() > 0 && judicialProcessResponsable.getPersonId() > 0) {
				Optional<JudicialProcess> judicialProcess = judicialProcessRepository.findById(judicialProcessResponsable.getJudicialProcess().getId());
				//Validate if there are minus than 3 responsables
				if(judicialProcess.isPresent() && judicialProcess.get().getJudicialProcessResponsable().size() >= RestBusinessValidation.MAX_RESPONSABLE_NUMBER) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already exists max number of Responsables.");
				}
			}else{ 
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).
						body("You cannot persist JudicialProcessResponsable with '0' parameters."); 
			}
			LOGGER.info("Finished Business rules");

			judicialProcessResponsable = judicialProcessResponsableService.persist(judicialProcessResponsable); 

			return ResponseEntity.status(HttpStatus.CREATED).body(judicialProcessResponsable);

		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error persiting a JudicialProcessResponsable" + judicialProcessResponsable + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	public ResponseEntity<Object> deleteJudicialProcessResponsable(long id){
		try {

			LOGGER.info("Starting Business rules");
			Optional<JudicialProcessResponsable> judicialProcessResponsable = judicialProcessResponsableService.getById(id);

			if(judicialProcessResponsable.isPresent()) {

				Optional<JudicialProcess> judicialProcess = judicialProcessRepository.findById(judicialProcessResponsable.get().getJudicialProcess().getId());

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
		}catch (DataIntegrityViolationException e) {			
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Data validation exception: " + e.getMessage());
		}catch (Exception e) {
			String errorMessage = "Error deleting a JudicialProcessResponsable by id " + id + ". Error: " + e.getMessage();
			LOGGER.error(errorMessage);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMessage);
		}
	}

	public JudicialProcess findChild(long id) {						
		JudicialProcess judicialProcess = customjudicialProcessRepository.findJudicialProcessByParentId(id);
		return judicialProcess;
	}
}
