package com.fyp.ehb.model;

import lombok.Data;

@Data
public class ForgotPwdRequest {

	private String mobileNo;
	private String password;
}
