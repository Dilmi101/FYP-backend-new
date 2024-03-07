package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGoalRequest {

	private String goalTitle;
	private String goalDescription;
	private String startDate;
	private String endDate;
	private String unit;
	private String target;
	private String priority;
	private String reminder;
	
}
