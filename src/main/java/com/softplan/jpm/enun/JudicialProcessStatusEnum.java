package com.softplan.jpm.enun;

import com.fasterxml.jackson.annotation.JsonValue;

public enum JudicialProcessStatusEnum {

	EM_ANDAMENTO("Em andamento", false),
	DESMEMBRADO("Desmembrado", false),
	EM_RECURSO("Em recurso", false ),
	FINALIZADO("Finalizado", true),
	ARQUIVADO("Arquivado", true);

	private String status;
	private boolean closed;

	JudicialProcessStatusEnum(String status, boolean closed) {
		this.status = status;
		this.closed = closed;
	}

	@JsonValue
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	@Override
	public String toString() {
		return getStatus();
	}
}
