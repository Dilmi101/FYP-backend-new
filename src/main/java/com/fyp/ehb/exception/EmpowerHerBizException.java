package com.fyp.ehb.exception;

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
		
	public EmpowerHerBizException(EmpowerHerBizError applicationError) {
		this.errorCode = applicationError.getErrorCode();
		this.errorMessage = applicationError.getErrorMessage();
	}

	public EmpowerHerBizException(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
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
