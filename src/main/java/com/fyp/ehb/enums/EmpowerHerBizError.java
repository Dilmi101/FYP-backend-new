package com.fyp.ehb.enums;

import org.springframework.http.HttpStatus;

public enum EmpowerHerBizError {

	INVALID_USERNAME("1", "Please re-check your username.", HttpStatus.UNAUTHORIZED),
	INVALID_PASSWORD("2", "Please re-check your password.", HttpStatus.UNAUTHORIZED),
	CUSTOMER_NOT_FOUND("3", "Customer does not exist.", HttpStatus.BAD_REQUEST),
	GOAL_NOT_CREATED("4", "Goal creation failed.", HttpStatus.BAD_REQUEST),
	GOAL_NOT_FOUND("5", "Goal does not exist.", HttpStatus.BAD_REQUEST),
	GOAL_NOT_UPDATED("6", "Goal updating failed.", HttpStatus.BAD_REQUEST),
	USERNAME_ALREADY_EXISTS("7", "Username already exists. Please enter another username.", HttpStatus.BAD_REQUEST),
	GOALS_NOT_FOUND("8", "No any goals found.", HttpStatus.BAD_REQUEST),
	INVALID_AMOUNT("9", "Invalid amount.", HttpStatus.BAD_REQUEST),
	EXPENSE_CREATION_FAILED("10", "Expense creation failed.", HttpStatus.BAD_REQUEST),
	EXPENSE_NOT_FOUND("11", "No such record for this ID.", HttpStatus.BAD_REQUEST),
	EXPENSE_UPDATE_FAILED("12", "Expense update failed.", HttpStatus.BAD_REQUEST),
	EXPENSE_DELETE_FAILED("13", "Expense delete failed.", HttpStatus.BAD_REQUEST),
	RAW_MATERIAL_CREATION_FAILED("14", "Cannot add raw material.", HttpStatus.BAD_REQUEST),
	RAW_MATERIAL_CANNOT_FOUND("15", "Raw material cannot be found.", HttpStatus.BAD_REQUEST),
	RAW_MATERIAL_UPDATE_FAILED("16", "Cannot update raw material", HttpStatus.BAD_REQUEST),
	YOU_HAVE_REACHED_LOW_STOCK_LEVEL("17", "You have reached the low stock level. The respective supplier has been notified.", HttpStatus.BAD_REQUEST),
	GOAL_PROGRESS_UPDATE_FAILED("18", "Goal progress update failed.", HttpStatus.BAD_REQUEST),
	REACHED_TARGET_EXPENSE("19", "You have exceeded your budget for this expense.", HttpStatus.BAD_REQUEST),
	INVALID_MOBILE("20", "No customer can be found for this mobile number. Please re-check.", HttpStatus.BAD_REQUEST),
	PWD_UPDATE_FAILED("21", "Password update failed.", HttpStatus.BAD_REQUEST),
	CUSTOMER_DETAILS_UPDATE_FAILED("22", "Customer details update failed.", HttpStatus.BAD_REQUEST),
	GOAL_TARGET_REACHED("23", "You have already reached your goal target.", HttpStatus.BAD_REQUEST);

	private String errorCode;
	private String errorMessage;
	private HttpStatus status;
	
	private EmpowerHerBizError(String errorCode, String errorMessage, HttpStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public String errorCode() {
		return errorCode;
	}

	public String errorMessage() {
		return errorMessage;
	}

	public HttpStatus status() { return status; }

}
