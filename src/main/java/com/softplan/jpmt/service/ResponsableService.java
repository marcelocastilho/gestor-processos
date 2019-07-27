package com.softplan.jpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.Responsable;
import com.softplan.jpm.jpa.repository.ResponsableRepository;

@Service
public class ResponsableService {

	@Autowired
	private ResponsableRepository responsableRepository;

	public List<Responsable> getAllResponsable() {
		List<Responsable> responsable = responsableRepository.findAll();
		return responsable;
	}
	
	public Responsable persistResponsable(Responsable responsable) {
		
		return responsableRepository.save(responsable);
	}

}
