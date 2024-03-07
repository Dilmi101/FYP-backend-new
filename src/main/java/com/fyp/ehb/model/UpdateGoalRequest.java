package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fyp.ehb.domain.Customer;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGoalRequest {

	private String goalTitle;
	private String goalDescription;
	private String startDate;
	private String endDate;
	private String unit;
	private int target;
	private String priority;
	private String reminder;
	
}