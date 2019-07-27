package com.softplan.jdm.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.Responsable;
import com.softplan.jpmt.service.JudicialProcessService;
import com.softplan.jpmt.service.ResponsableService;

@RestController
public class JudicialProcessController
{
	@Autowired
	private JudicialProcessService judicialProcessService;
	
	@Autowired
	private ResponsableService responsableService;

	@GetMapping("/judicialprocess")
	public List<JudicialProcess> getAllJudicialProcess() 
	{

		List<JudicialProcess> judicialProcessList = new ArrayList<JudicialProcess>();

		JudicialProcess judicialProcess = new JudicialProcess();
		judicialProcess.setDistributionDate(LocalDate.now());
		judicialProcess.setId(01);
		
		judicialProcessList.add(judicialProcess);
		judicialProcessService.getAllJudicialProcess();

		return judicialProcessList;
	}

	@PostMapping(path= "/judicialprocess", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> test(@RequestBody JudicialProcess judicialProcess) 
	{
		
//		judicialProcess.addResponsable(new Responsable("Responsavel 1"));
//		judicialProcess.addResponsable(new Responsable("Responsavel 2"));

		//Validate the data
		judicialProcess = judicialProcessService.persistJudicialProcess(judicialProcess);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/judicialprocess/{id}")
				.buildAndExpand(judicialProcess.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
	
	@GetMapping("/responsable")
	public List<Responsable> getAllResponsables() 
	{
		return responsableService.getAllResponsable();
	}
	
	@PostMapping(path= "/responsable", consumes = "application/json", produces = "application/json")
	public  ResponseEntity<Object> test(@RequestBody Responsable responsable) 
	{
		responsable = responsableService.persistResponsable(responsable);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/judicialprocess/{id}")
				.buildAndExpand(responsable.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}
}