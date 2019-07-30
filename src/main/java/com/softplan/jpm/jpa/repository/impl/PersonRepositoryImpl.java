package com.softplan.jpm.jpa.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.entities.Person;
import com.softplan.jpm.jpa.repository.PersonRepository;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
public class PersonRepositoryImpl {//implements PersonRepository{
	
	//private EntityManager entityMgrObj = Persistence.createEntityManagerFactory("Person").createEntityManager();
   
	EntityManager em;
	
    public List<Person> findPerson(Person person){
    	
    	em.refresh(person);
      	    
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	
	    CriteriaQuery<Person> q = cb.createQuery(Person.class);
	    Root<Person> c = q.from(Person.class);
	   	    
	    CriteriaQuery<Person> selectQuery = q.select(c);
	    
        TypedQuery<Person> typedQuery = em.createQuery(selectQuery);
        List<Person> employeeList = typedQuery.getResultList();
	    
	    return employeeList;
    }



}
