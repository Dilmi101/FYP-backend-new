package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.SignupRequest;
import com.fyp.ehb.model.CustomerResponse;

public interface CustomerService {
    public Customer registerCustomer(SignupRequest signupRequest) throws EmpowerHerBizException;
	public CustomerResponse login(String username, String password) throws EmpowerHerBizException;

    public Customer updateCustomerProfile(String customerId, CustomerResponse customerResponse) throws EmpowerHerBizException;
}
