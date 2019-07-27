package com.softplan.jpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.jpa.repository.JudicialProcessRepository;

@Service
public class JudicialProcessService {

	@Autowired
	private JudicialProcessRepository judicialProcessRepository;

	public List<JudicialProcess> getAllJudicialProcess() {
		List<JudicialProcess> judicialProcess = judicialProcessRepository.findAll();
		return judicialProcess;
	}
	
	public JudicialProcess persistJudicialProcess(JudicialProcess judicialProcess) {
		
		return judicialProcessRepository.save(judicialProcess);
	}

}
