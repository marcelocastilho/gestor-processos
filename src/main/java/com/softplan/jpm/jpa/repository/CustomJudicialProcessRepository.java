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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.softplan.jdm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcess;

/**
 * Specifies methods used to obtain and modify person related information
 * which is stored in the database.
 * @author Marcelo Castilho
 */
@Repository
public class CustomJudicialProcessRepository{

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOGGER = LoggerFactory.getLogger(JudicialProcessController.class);

	public List<JudicialProcess> findJudicialProcess(JudicialProcess judicialProcess){

		LOGGER.debug("Finding JudicialProcess: " + judicialProcess.toString());

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<JudicialProcess> cq = cb.createQuery(JudicialProcess.class);

		Root<JudicialProcess> rJudicialProcess = cq.from(JudicialProcess.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if(!StringUtils.isEmpty(judicialProcess.getUniqueProcessId())) {			
			LOGGER.debug("Using uniqueProcessIdPredicate with value: " + judicialProcess.getUniqueProcessId() );
			Predicate uniqueProcessIdPredicate = cb.equal(rJudicialProcess.get("unique_process_id"), judicialProcess.getUniqueProcessId());
			predicates.add(uniqueProcessIdPredicate);
		}
		if(judicialProcess.getDistributionDate()!= null ) {
			LOGGER.debug("Using personEmailPredicate with value: " + judicialProcess.getDistributionDate() );
			Predicate distributionDatePredicate = cb.equal(rJudicialProcess.get("distributionDate"), judicialProcess.getDistributionDate());
			predicates.add(distributionDatePredicate);
		}
		if(judicialProcess.getSecret() != null) {
			LOGGER.debug("Using secretPredicate with value: " + judicialProcess.isSecret());			
			Predicate secretPredicate = cb.equal(rJudicialProcess.get("secret"), judicialProcess.isSecret());
			predicates.add(secretPredicate);
		}
		if(judicialProcess.getStatus() != null) {
			LOGGER.debug("Using statusPredicate with value: " + judicialProcess.getStatus());			
			Predicate statusPredicate = cb.equal(rJudicialProcess.get("status"), judicialProcess.getStatus());
			predicates.add(statusPredicate);
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<JudicialProcess> typedQuery = em.createQuery(cq);
		List<JudicialProcess> judicialProcessList = typedQuery.getResultList();

		return judicialProcessList;
	}

	public JudicialProcess findJudicialProcessByParentId(long parentId){

		LOGGER.debug("Finding JudicialProcessByParentId: " + parentId);

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<JudicialProcess> cq = cb.createQuery(JudicialProcess.class);
			Root<JudicialProcess> rJudicialProcess = cq.from(JudicialProcess.class);

			JudicialProcess judicialProcess = new JudicialProcess();
			judicialProcess.setId(parentId);

			List<Predicate> predicates = new ArrayList<Predicate>();

			LOGGER.debug("Using uniqueProcessIdPredicate with value: " + judicialProcess.getId() );
			Predicate parentJudicialProcessPredicate = cb.equal(rJudicialProcess.get("parentJudicialProcess"), judicialProcess);
			predicates.add(parentJudicialProcessPredicate);

			cq.where(predicates.toArray(new Predicate[predicates.size()]));

			TypedQuery<JudicialProcess> typedQuery = em.createQuery(cq);
			JudicialProcess judicialProcessOut = typedQuery.getSingleResult();
			
			return judicialProcessOut;
			
		}catch(javax.persistence.NoResultException e) {
			LOGGER.warn("JudicialProcess not found by parentId: " + parentId);
			return null;
		}
	}

}
