package com.softplan.jpm.exceptionhandling;

import org.springframework.http.HttpStatus;

public class BusinessErrorResponse extends Exception {
		
	private static final long serialVersionUID = 1L;
	
	HttpStatus httpStatus = null;

	public BusinessErrorResponse(String errorMessage, HttpStatus httpStatus) {
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
