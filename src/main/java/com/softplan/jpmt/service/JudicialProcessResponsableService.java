package com.softplan.jpmt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.jpa.repository.CustomJudicialProcessResponsableRepository;
import com.softplan.jpm.jpa.repository.JudicialProcessResponsableRepository;

@Service
public class JudicialProcessResponsableService {

	@Autowired
	private JudicialProcessResponsableRepository judicialProcessResponsableRepository;
	
	@Autowired
	private CustomJudicialProcessResponsableRepository customJudicialProcessResponsableRepository;
	
	public Optional<JudicialProcessResponsable> getById(long id) {
		return judicialProcessResponsableRepository.findById(id);		
	}
	
	public List<JudicialProcessResponsable> getAll() {
		List<JudicialProcessResponsable> responsable = judicialProcessResponsableRepository.findAll();
		return responsable;
	}

	public List<JudicialProcessResponsable> find(JudicialProcessResponsable judicialProcessResponsable) {
		List<JudicialProcessResponsable> judicialProcessResponsables = customJudicialProcessResponsableRepository.findJudicialProcessResponsable(judicialProcessResponsable);
		return judicialProcessResponsables;
	}
	
	public JudicialProcessResponsable persist(JudicialProcessResponsable judicialProcessResponsable) {
		
		return judicialProcessResponsableRepository.save(judicialProcessResponsable);
	}

	public void delete(JudicialProcessResponsable judicialProcessResponsable) {
		judicialProcessResponsableRepository.delete(judicialProcessResponsable);
	}	
	
	public void deleteById(long id) {
		judicialProcessResponsableRepository.deleteById(id);
	}	
}
