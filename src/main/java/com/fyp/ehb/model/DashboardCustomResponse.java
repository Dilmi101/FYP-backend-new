package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashboardCustomResponse {

	private String type;
	private String title;
	private String amount;
	private String percentage;
}
