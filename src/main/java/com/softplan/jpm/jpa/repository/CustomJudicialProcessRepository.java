package com.softplan.jpm.jpa.repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.softplan.jpm.controller.JudicialProcessController;
import com.softplan.jpm.entities.JudicialProcess;
import com.softplan.jpm.entities.JudicialProcessResponsable;
import com.softplan.jpm.entities.Person;

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

	public Page<JudicialProcess> findJudicialProcess(JudicialProcess judicialProcess, LocalDate startDate, LocalDate endDate, String reponsableName, Pageable pageable){

		LOGGER.debug("Finding JudicialProcess: " + judicialProcess.toString());

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<JudicialProcess> cq = cb.createQuery(JudicialProcess.class);

		Root<JudicialProcess> rJudicialProcess = cq.from(JudicialProcess.class);
		cq.orderBy(cb.asc(rJudicialProcess.get("id")));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if(!StringUtils.isEmpty(judicialProcess.getUniqueProcessId())) {			
			LOGGER.debug("Using uniqueProcessIdPredicate with value: " + judicialProcess.getUniqueProcessId() );
			Predicate uniqueProcessIdPredicate = cb.equal(rJudicialProcess.get("unique_process_id"), judicialProcess.getUniqueProcessId());
			predicates.add(uniqueProcessIdPredicate);
		}
		if(startDate != null) {
			LOGGER.debug("Using DistributionDatePredicate with value: " + judicialProcess.getDistributionDate() );
			LocalDate localdate = LocalDate.now();
//			Predicate onStart = cb.greaterThanOrEqualTo(rJudicialProcess.get("distributionDate"), startDate);
//			Predicate onEnd = cb.lessThanOrEqualTo(rJudicialProcess.get("distributionDate"), endDate != null ? endDate : Date.from(localdate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
//			
//			Predicate distributionDatePredicate = cb.between(rJudicialProcess.get("distributionDate"), onStart, onEnd);
			
			Predicate distributionDatePredicate =  cb.between(rJudicialProcess.get("distributionDate"), startDate,  endDate != null ? endDate : localdate);
		  	
			predicates.add(distributionDatePredicate);
		}
		if(judicialProcess.getSecret() != null) {
			LOGGER.debug("Using secretPredicate with value: " + judicialProcess.isSecret());			
			Predicate secretPredicate = cb.equal(rJudicialProcess.get("secret"), judicialProcess.isSecret());
			predicates.add(secretPredicate);
		}
		if(!StringUtils.isEmpty(judicialProcess.getPhysicalPath())) {			
			LOGGER.debug("Using physicalPathPredicate with value: " + judicialProcess.getPhysicalPath() );
			Predicate physicalPathPredicate = cb.like(rJudicialProcess.get("physicalPath"), "%" + judicialProcess.getPhysicalPath() + "%");
			predicates.add(physicalPathPredicate);
		}	
		if(judicialProcess.getStatus() != null) {
			LOGGER.debug("Using statusPredicate with value: " + judicialProcess.getStatus());			
			Predicate statusPredicate = cb.equal(rJudicialProcess.get("status"), judicialProcess.getStatus());
			predicates.add(statusPredicate);
		}
		if(!StringUtils.isEmpty(reponsableName)) {
			LOGGER.debug("Using reponsableNamePredicate with value: " + reponsableName);		
			Join<JudicialProcess, JudicialProcessResponsable > join = rJudicialProcess.join("judicialProcessResponsable");
			Path<Long> campoProcessId = join.get("judicialProcessId");
			Predicate reponsableNamePredicate = cb.isTrue(campoProcessId.in(reponsableName));
			predicates.add(reponsableNamePredicate);
		}
		
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<JudicialProcess> typedQuery = em.createQuery(cq);
		int totalRows = typedQuery.getResultList().size();
			
		Page<JudicialProcess> judicialProcessList  = new PageImpl<JudicialProcess>(typedQuery.getResultList(), pageable, totalRows);

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
			Predicate childJudicialProcessPredicate = cb.equal(rJudicialProcess.get("childJudicialProcess"), judicialProcess);
			predicates.add(childJudicialProcessPredicate);

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
