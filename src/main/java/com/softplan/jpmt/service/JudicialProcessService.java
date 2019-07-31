package com.softplan.jpmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<JudicialProcess> getAll() {
		List<JudicialProcess> judicialProcessList = judicialProcessRepository.findAll();
		return judicialProcessList;
	}
	
	public List<JudicialProcess> find(JudicialProcess judicialProcess) {
		List<JudicialProcess> judicialProcessList = customjudicialProcessRepository.findJudicialProcess(judicialProcess);
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
