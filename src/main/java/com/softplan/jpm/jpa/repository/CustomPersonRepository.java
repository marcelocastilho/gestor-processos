package com.softplan.jpm.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.Person;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
@Repository
public class CustomPersonRepository{

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	public Page<Person> findPerson(Person person, long idJudicialProject, Pageable pageable){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		
		Root<Person> rPerson = cq.from(Person.class);
		
		cq.orderBy(cb.asc(rPerson.get("id")));

		List<Predicate> predicates = new ArrayList<Predicate>();


		if(!StringUtils.isEmpty(person.getName())) {			
			LOGGER.debug("Using personNamePredicate with value: " + person.getName() );
			Predicate personNamePredicate = cb.like(rPerson.get("name"), "%" + person.getName() + "%");
			predicates.add(personNamePredicate);
		}
		if(!StringUtils.isEmpty(person.getEmail())) {
			LOGGER.debug("Using personEmailPredicate with value: " + person.getEmail() );
			Predicate personEmailPredicate = cb.equal(rPerson.get("email"), person.getEmail());
			predicates.add(personEmailPredicate);
		}
		if(!StringUtils.isEmpty(person.getDocument())) {
			LOGGER.debug("Using personDocumentPredicate with value: " + person.getDocument());			
			Predicate personDocumentPredicate = cb.equal(rPerson.get("document"), person.getDocument());
			predicates.add(personDocumentPredicate);
		}
		if(idJudicialProject > 0) {
			LOGGER.debug("Using idJudicialProjectPredicate with value: " + idJudicialProject);		
			Join<Person, JudicialProcessResponsable > join = rPerson.join("judicialProcessResponsables");
			Path<Long> campoProcessId = join.get("judicialProcessId");
			Predicate idJudicialProjectPredicate = cb.isTrue(campoProcessId.in(idJudicialProject));
			predicates.add(idJudicialProjectPredicate);
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		
		TypedQuery<Person> typedQuery = em.createQuery(cq);
		int totalRows = typedQuery.getResultList().size();
		
		//pageflow control
		//typedQuery.setFirstResult(pageable.getPageNumber());		
		//typedQuery.setMaxResults(pageable.getPageSize());
		
		Page<Person> personList  = new PageImpl<Person>(typedQuery.getResultList(), pageable, totalRows);
				
		//List<Person> personList = typedQuery. getResultList();

		return personList;
	}	
}
