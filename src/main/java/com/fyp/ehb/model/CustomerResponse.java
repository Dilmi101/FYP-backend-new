package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fyp.ehb.domain.Business;

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
	private Business business;
	private String pushToken;
}
