package com.softplan.jpm.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.jpa.repository.CustomJudicialProcessRepository;
import com.softplan.jpm.jpa.repository.JudicialProcessRepository;


@Service
public class JudicialProcessService {

	@Autowired
	private JudicialProcessRepository judicialProcessRepository;
	
	@Autowired
	private CustomJudicialProcessRepository customjudicialProcessRepository;

	public Optional<JudicialProcess> getById(long id) {

		return judicialProcessRepository.findById(id);
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
	
	public Page<JudicialProcess> find(JudicialProcess judicialProcess, LocalDate startDate, LocalDate endDate, String responsableName, int page, int size) {
		
		PageRequest pageRequest = PageRequest.of(	
				page,
				size,
				Sort.Direction.ASC,
				"id");
		
		Page<JudicialProcess> judicialProcessList = customjudicialProcessRepository.findJudicialProcess(judicialProcess, startDate, endDate, responsableName, pageRequest);
		return judicialProcessList;
	}

	public JudicialProcess persist(JudicialProcess judicialProcess) {
		return judicialProcessRepository.save(judicialProcess);
	}
	
	public void delete(JudicialProcess judicialProcess) {
		judicialProcessRepository.delete(judicialProcess);
	}
	
	
	public void deleteById(long id) {
		judicialProcessRepository.deleteById(id);
	}
	
	public JudicialProcess findChild(long id) {						
		JudicialProcess judicialProcess = customjudicialProcessRepository.findJudicialProcessByParentId(id);
		return judicialProcess;
	}
}
