package com.fyp.ehb.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fyp.ehb.enums.EmpowerHerBizError;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpowerHerBizException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMessage;
	private HttpStatus status;
		
	public EmpowerHerBizException(EmpowerHerBizError applicationError) {
		super();
		this.errorCode = applicationError.errorCode();
		this.errorMessage = applicationError.errorMessage();
		this.status = applicationError.status();
	}	

	public EmpowerHerBizException(EmpowerHerBizError applicationError, String errorMessage, HttpStatus status) {
		super();
		this.errorCode = applicationError.errorCode();
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public EmpowerHerBizException(EmpowerHerBizError applicationError, String errorMessage) {
		super();
		this.errorCode = applicationError.errorCode();
		this.errorMessage = errorMessage;
		this.status = applicationError.status();
	}
	
	public EmpowerHerBizException(String errorCode, String errorMessage, HttpStatus status) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
