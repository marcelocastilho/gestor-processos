package com.softplan.jpm.exception;

import org.springframework.http.HttpStatus;

public class JudicialProcessBusinessErrorException extends Exception {
		
	private static final long serialVersionUID = 1L;
	
	HttpStatus httpStatus = null;

	public JudicialProcessBusinessErrorException(String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.httpStatus = httpStatus;	
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	
}
