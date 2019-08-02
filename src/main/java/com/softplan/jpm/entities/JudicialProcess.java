package com.softplan.jpm.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.softplan.jpm.enun.JudicialProcessStatusEnum;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JudicialProcess {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="judicialprocess_sequence")
	@SequenceGenerator(name="judicialprocess_sequence", sequenceName="jp_seq")
	private long id;

	@NotBlank(message="{judicialprocess.uniqueProcessId.not.blank}")
	@Size(min=20, max=20, message="{judicialprocess.uniqueProcessId.size.not.valid}")
	@Column(unique=true)
	private String uniqueProcessId;
	
	@NotNull(message = "{judicialprocess.distributionDate.not.null}")
	@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate distributionDate;

	@NotNull(message = "{judicialprocess.secret.not.null}")
	private Boolean secret;

	@NotBlank(message = "{judicialprocess.physicalPath.not.blank}")
	private String physicalPath;

	@NotNull(message = "{judicialprocess.status.not.null}")
	@Enumerated(EnumType.STRING)
	private JudicialProcessStatusEnum status;

	@NotBlank(message = "{judicialprocess.description.not.blank}")
	@Column(columnDefinition = "text")
	private String description;
		
	public long getId() {
		return this.id;
	}
		
	public void setId(long id) {
		this.id = id;
	}

	@JsonInclude
	@NotNull(message = "{judicialprocess.responsable.not.null}")
	@OneToMany( mappedBy = "judicialProcess", cascade = CascadeType.ALL, orphanRemoval = true)	
	private List<JudicialProcessResponsable> judicialProcessResponsable;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="childJudicialProcess", unique=true)
	private JudicialProcess childJudicialProcess;

	public JudicialProcess(String uniqueProcessId, Boolean secret, LocalDate distributionDate, JudicialProcessStatusEnum status, String physicalPath) {		
		this.uniqueProcessId = uniqueProcessId;
		this.secret = secret;		
		this.status= status;
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
	
	@Transient
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

	public JudicialProcess getChildJudicialProcess() {
		return childJudicialProcess;
	}

	public void setChildJudicialProcess(JudicialProcess childJudicialProcess) {
		this.childJudicialProcess = childJudicialProcess;
	}

	public List<JudicialProcessResponsable> getJudicialProcessResponsable() {
		return judicialProcessResponsable;
	}

	public void setJudicialProcessResponsable(List<JudicialProcessResponsable> judicialProcessResponsable) {
		this.judicialProcessResponsable = judicialProcessResponsable;
		
		for (JudicialProcessResponsable judicialProcessResponsable2 : judicialProcessResponsable) {
			judicialProcessResponsable2.setJudicialProcess(this);
		}		
	}

	public void addJudicialProcessResponsable(JudicialProcessResponsable judicialProcessResponsable) {
		if (judicialProcessResponsable == null) {
			this.judicialProcessResponsable = new ArrayList<JudicialProcessResponsable>();
		}
		this.judicialProcessResponsable.add(judicialProcessResponsable);
		judicialProcessResponsable.setJudicialProcess(this);
	}

	public void removeJudicialProcessResponsable(JudicialProcessResponsable judicialProcessResponsable) {
		if (judicialProcessResponsable != null) {
			this.judicialProcessResponsable.remove(judicialProcessResponsable);
			judicialProcessResponsable.setJudicialProcess(null);
		}
	}

	@Override
	public String toString() {
		return "JudicialProcess{" +
				"Id=" + id +
				", uniqueProcessId=" + uniqueProcessId +
				//", distributionDate='" + distributionDate.toString() + '\'' +
				", secret=" + secret +
				", physicalPath='" + physicalPath + '\'' +
				//", status='" + status.getStatus() + '\'' +
				", description='" + description + '\'' +
				//", responsables='" + judicialProcessResponsable.stream().map(JudicialProcessResponsable::getJudicialProcess).collect(Collectors.toList()) + '\'' +
				'}';
	}

}