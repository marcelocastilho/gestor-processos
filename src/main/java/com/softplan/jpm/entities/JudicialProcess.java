package com.softplan.jpm.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softplan.jpm.enun.JudicialProcessStatusEnum;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JudicialProcess {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="judicialprocess_sequence")
	@SequenceGenerator(name="judicialprocess_sequence", sequenceName="jp_seq")
	private long id;
	
	//@Size(min = 10)
	//@Column(unique=true)
	private String uniqueProcessId;

	@Temporal(TemporalType.DATE)
	private Date distributionDate;

	private Boolean secret;

	@NotBlank(message = "Enter a physicalPath ")
	private String physicalPath;
	
	@Enumerated(EnumType.STRING)
	private JudicialProcessStatusEnum status;

	@Column(columnDefinition = "text")
	private String description;

	public long getId() {
		return id;
	}

	//@Fetch(FetchMode.SUBSELECT)
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "judicial_process_id")
	private List<JudicialProcessResponsable> judicialProcessResponsables = new ArrayList<JudicialProcessResponsable>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    @JoinColumn(name="parentJudicialProcess", unique=true)
    private JudicialProcess parentJudicialProcess;
	
	public JudicialProcess(String uniqueProcessId, boolean secret, Date distributionDate, String physicalPath) {		
		this.uniqueProcessId = uniqueProcessId;
		this.secret = secret;
		this.distributionDate = distributionDate;
		this.physicalPath = physicalPath;
	}
	
	public JudicialProcess() {
		
	}
		
	public void setId(long id) {
		this.id = id;
	}

	public String getUniqueProcessId() {
		return uniqueProcessId;
	}

	public void setUniqueProcessId(String uniqueProcessId) {
		this.uniqueProcessId = uniqueProcessId;
	}

	public Date getDistributionDate() {
		return distributionDate;
	}

	public void setDistributionDate(Date distributionDate) {
		this.distributionDate = distributionDate;
	}

	public Boolean getSecret() {
		return secret;
	}
	
	public boolean isSecret() {
		return secret.booleanValue();
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

	public JudicialProcessStatusEnum getStatus() {
		return status;
	}

	public void setStatus(JudicialProcessStatusEnum status) {
		this.status = status;
	}

	public JudicialProcess getParentJudicialProcess() {
		return parentJudicialProcess;
	}

	public void setParentJudicialProcess(JudicialProcess parentJudicialProcess) {
		this.parentJudicialProcess = parentJudicialProcess;
	}
	
	@Override
	public String toString() {
		return "JudicialProcess{" +
				"Id=" + id +
				", uniqueProcessId=" + uniqueProcessId +
				", distributionDate='" + distributionDate.toString() + '\'' +
				", secret=" + secret +
				", physicalPath='" + physicalPath + '\'' +
				", status='" + status.getStatus() + '\'' +
				", description='" + description + '\'' +
				", responsables='" + judicialProcessResponsables.stream().map(JudicialProcessResponsable::getJudicialProcessId).collect(Collectors.toList()) + '\'' +
				'}';
	}

}