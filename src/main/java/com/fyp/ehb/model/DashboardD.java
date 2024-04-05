package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardD {

	private String type;
	private String goalId;
	private String expenseId;
	private String rawMatId;
	
}
