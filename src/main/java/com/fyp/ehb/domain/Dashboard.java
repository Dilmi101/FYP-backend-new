package com.fyp.ehb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "dashboards")
public class Dashboard {

	@Id
	private String id;
	
	@DBRef
	private Customer customer;
	
	private String type;
	
	private String expenseId;
	
	private String goalId;
	
	private String rawMaterialId;
	
}
