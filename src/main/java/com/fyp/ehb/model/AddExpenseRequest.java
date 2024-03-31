package com.fyp.ehb.model;

import lombok.Data;

@Data
public class AddExpenseRequest {
	
	private String expenseTitle;
	private String expenseCategory;
	private String expenseDescription;
	private String startDate;
	private String endDate;
	private String amount;
	private String reminder;
	private String customerId;

}
