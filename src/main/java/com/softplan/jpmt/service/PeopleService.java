package com.softplan.jpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softplan.jpm.entities.People;
import com.softplan.jpm.jpa.repository.PeopleRepository;

@Service
public class PeopleService {

	@Autowired
	private PeopleRepository peopleRepository;

	public List<People> getAllPeople() {
		List<People> responsable = peopleRepository.findAll();
		return responsable;
	}
	
	public People persistPeople(People people) {
		
		return peopleRepository.save(people);
	}
	
	public People getPeopleById(long id) {
		
		return peopleRepository.getOne(id);
	}

}
