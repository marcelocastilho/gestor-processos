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
public class PersonRepositoryImpl implements PersonRepository{
	
	private EntityManager entityMgrObj = Persistence.createEntityManagerFactory("Person").createEntityManager();
   
    public List<Person> findByName(String name){
      	    
	    CriteriaBuilder cb = entityMgrObj.getCriteriaBuilder();
	
	    CriteriaQuery<Person> q = cb.createQuery(Person.class);
	    Root<Person> c = q.from(Person.class);
	    q.select(c);
	    
	    CriteriaQuery<Person> selectQuery = q.select(c);
        TypedQuery<Person> typedQuery = entityMgrObj.createQuery(selectQuery);
        List<Person> employeeList = typedQuery.getResultList();
	    
	    return employeeList;
    }


	@Override
	public List<Person> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Person> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <S extends Person> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteInBatch(Iterable<Person> entities) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Person getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Page<Person> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Optional<Person> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delete(Person entity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteAll(Iterable<? extends Person> entities) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <S extends Person> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <S extends Person> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public <S extends Person> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<Person> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
