package com.fyp.ehb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "budgets")
public class Budget {

	@Id
	private String id;
	private String expenseId;
	private String amount;
	private String status;
	private String month;
	private String title;
	
	@DBRef
	private Customer customer;
}
