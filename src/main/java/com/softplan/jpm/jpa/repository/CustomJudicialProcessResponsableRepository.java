package com.softplan.jpm.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcessResponsable;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
@Repository
public class CustomJudicialProcessResponsableRepository{

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	public List<JudicialProcessResponsable> findJudicialProcessResponsable(JudicialProcessResponsable judicialProcessResponsable){
		
		LOGGER.debug("Finding: " + judicialProcessResponsable.toString());
		
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<JudicialProcessResponsable> cq = cb.createQuery(JudicialProcessResponsable.class);
		
		Root<JudicialProcessResponsable> rResponsable = cq.from(JudicialProcessResponsable.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if(judicialProcessResponsable.getJudicialProcess().getId() > 0) {
			LOGGER.debug("Using personNamePredicate with value: " + judicialProcessResponsable.getJudicialProcess().getId());
			Predicate judicialProcessPredicate = cb.equal(rResponsable.get("judicialProcessId"), judicialProcessResponsable.getJudicialProcess().getId());
			predicates.add(judicialProcessPredicate);
		}
		if(judicialProcessResponsable.getPersonId() > 0) {
			LOGGER.debug("Using personNamePredicate with value: " + judicialProcessResponsable.getPersonId());
			Predicate personIdPredicate = cb.equal(rResponsable.get("personId"), judicialProcessResponsable.getPersonId());
			predicates.add(personIdPredicate);
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		
		TypedQuery<JudicialProcessResponsable> typedQuery = em.createQuery(cq);
		List<JudicialProcessResponsable> jpResponsableList = typedQuery.getResultList();

		return jpResponsableList;
	}	
}
