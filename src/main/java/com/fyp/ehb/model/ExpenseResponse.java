package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fyp.ehb.domain.Customer;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseResponse {

	private String id;
	private String expenseTitle;
	private String expenseCategory;
	private String expenseDescription;
	private String amount;
	private int duration;
	private String startDate;
	private String endDate;
	private String reminder;
	private String expenseStatus;
	private Customer customer;
	private String remainingDays;
}
