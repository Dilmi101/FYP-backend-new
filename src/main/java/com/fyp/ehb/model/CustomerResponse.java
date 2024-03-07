package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResponse {

	private String id;
	private String name;
	private String username;
	private String email;
	private String nic;
	private String mobileNo;
}
