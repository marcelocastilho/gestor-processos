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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.jdm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.Person;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
@Repository
public class CustonPersonRepository{

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	public List<Person> findPerson(Person person, long idJudicialProject){

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		Root<Person> rPerson = cq.from(Person.class);

		List<Predicate> predicates = new ArrayList<Predicate>();


		if(!StringUtils.isEmpty(person.getName())) {
			LOGGER.debug("Using email to find: " + person.getName());
			Predicate personNamePredicate = cb.like(rPerson.get("name"), "%" + person.getName() + "%");
			predicates.add(personNamePredicate);
		}
		if(!StringUtils.isEmpty(person.getEmail())) {
			LOGGER.debug("Using email to find: " + person.getEmail() );
			Predicate personEmailPredicate = cb.equal(rPerson.get("email"), person.getEmail());
			predicates.add(personEmailPredicate);
		}
		if(!StringUtils.isEmpty(person.getDocument())) {
			LOGGER.debug("Using document to find: " + person.getDocument() );
			Predicate personDocumentPredicate = cb.equal(rPerson.get("document"), person.getDocument());
			predicates.add(personDocumentPredicate);
		}
		if(idJudicialProject > 0) {
			Join<Person, JudicialProcessResponsable > join = rPerson.join("judicialProcessResponsables");
			Path<Long> campoPetId = join.get("id");
			Predicate predPets = cb.isTrue(campoPetId.in(idJudicialProject));
			predicates.add(predPets);
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		//CriteriaQuery<Person> selectQuery = q.select(c);

		TypedQuery<Person> typedQuery = em.createQuery(cq);
		List<Person> employeeList = typedQuery.getResultList();

		return employeeList;
	}

}
