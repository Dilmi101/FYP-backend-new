package com.fyp.ehb.enums;

public enum EmpowerHerBizError {

	INVALID_USERNAME("1", "Please re-check your username."),
	INVALID_PASSWORD("2", "Please re-check your password."),
	CUSTOMER_NOT_FOUND("3", "Customer does not exist."),
	GOAL_NOT_CREATED("4", "Goal creation failed."),
	GOAL_NOT_FOUND("5", "Goal does not exist."),
	GOAL_NOT_UPDATED("6", "Goal updating failed."),
	USERNAME_ALREADY_EXISTS("7", "Username already exists. Please enter another username.");
	
	private String errorCode;
	private String errorMessage;
	
	private EmpowerHerBizError(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
