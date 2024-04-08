package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawMaterialHistoryMain {

	private String createdDate;
	private String value;
	private String action;
}
