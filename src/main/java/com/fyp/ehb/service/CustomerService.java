package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.model.SignupRequest;
import com.fyp.ehb.model.CustomerResponse;

public interface CustomerService {

    public Customer registerCustomer(SignupRequest signupRequest) throws Exception;
	public CustomerResponse login(String username, String password) throws Exception;

}
