package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.SignupRequest;
import com.fyp.ehb.model.CustomerResponse;
import com.fyp.ehb.model.ForgotPwdRequest;

public interface CustomerService {
    public Customer registerCustomer(SignupRequest signupRequest) throws EmpowerHerBizException;
	public CustomerResponse login(String username, String password) throws EmpowerHerBizException;

    public CustomerResponse updateCustomerProfile(String customerId, CustomerResponse customerResponse) throws EmpowerHerBizException;
	public CustomerResponse forgotPwd(ForgotPwdRequest forgotPwdRequest) throws Exception;
}
