package com.fyp.ehb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "expenses")
public class Expense {

	@Id
	private String expenseId;
	
	private String expenseTitle;
	
	private String expenseCategory;
	private String expenseDescription;
	private String amount;
	private int duration;
	private String startDate;
	private String endDate;
	private String reminder;
	private String expenseStatus;
	
}
