package com.fyp.ehb.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "expenses")
public class Expense {

	@Id
	private String id;
	
	@Field("expense_title")
	private String expenseTitle;
	
	@Field("expense_category")
	private String expenseCategory;
	
	@Field("expense_description")
	private String expenseDescription;
	
	private String amount;
	
	private int duration;
	
	@Field("start_date")
	private Date startDate;
	
	@Field("end_date")
	private Date endDate;
	
	private String reminder;
	
	@Field("expense_status")
	private String expenseStatus;
	
	@DBRef
	private Customer customer;
	
}
