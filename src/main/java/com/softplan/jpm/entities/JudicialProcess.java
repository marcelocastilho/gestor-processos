package com.softplan.jpm.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JudicialProcess {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="judicialProcess_sequence")
	@SequenceGenerator(name="judicialProcess_sequence", sequenceName="jp_seq")
	private long id;
	
	//@Size(min = 10)
	//@Column(unique=true)
	private String uniqueProcessId;

	private LocalDate distributionDate;

	private boolean secret;

	private String physicalPath;
	
	//@Enumerated(EnumType.STRING)
	//private JudicialProcessStatusEnum status;

	@Column(columnDefinition = "text")
	private String description;

	public long getId() {
		return id;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "judicial_process_id")
	private List<JudicialProcessResponsable> judicialProcessResponsables = new ArrayList<JudicialProcessResponsable>();

	public JudicialProcess(String uniqueProcessId, boolean secret, LocalDate distributionDate, String physicalPath) {		
		this.uniqueProcessId = uniqueProcessId;
		this.secret = secret;
		this.distributionDate = distributionDate;
		this.physicalPath = physicalPath;
	}
	
	public JudicialProcess() {
		
	}
	
	public String getUniqueProcessId() {
		return uniqueProcessId;
	}

	public void setUniqueProcessId(String uniqueProcessId) {
		this.uniqueProcessId = uniqueProcessId;
	}

	public LocalDate getDistributionDate() {
		return distributionDate;
	}

	public void setDistributionDate(LocalDate distributionDate) {
		this.distributionDate = distributionDate;
	}

	public boolean isSecret() {
		return secret;
	}

	public void setSecret(boolean secret) {
		this.secret = secret;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}

	public void setPhysicalPath(String physicalPath) {
		this.physicalPath = physicalPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public void addJudicialProcessResponsables(JudicialProcessResponsable judicialProcessResponsable) {
//		if(this.judicialProcessResponsables == null) {
//			this.judicialProcessResponsables = new ArrayList<JudicialProcessResponsable>();
//		}	
//		judicialProcessResponsables.add(judicialProcessResponsable);		
//	}

//	public List<JudicialProcessResponsable> getJudicialProcessResponsables() {
//		return judicialProcessResponsables;
//	}	

//	@Override
//	public String toString() {
//		return "JudicialProcess{" +
//				"uniqueProcessId=" + uniqueProcessId +
//				", distributionDate='" + distributionDate.toString() + '\'' +
//				", responsables='" + responsables.stream().map(Responsable::getName).collect(Collectors.toList()) + '\'' +
//				'}';
//	}

}