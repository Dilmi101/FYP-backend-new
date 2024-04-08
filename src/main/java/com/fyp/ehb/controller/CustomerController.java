package com.fyp.ehb.controller;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fyp.ehb.model.CustomerResponse;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

	//registration
    @PostMapping(value = "/signup")
    public MainResponse registerCustomer(@Valid @RequestBody SignupRequest signupRequest) throws EmpowerHerBizException {

        Customer customer = customerService.registerCustomer(signupRequest);

        MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(customer);

		return mainResponse;
    }

	//login
	@GetMapping(value="/login")
	public MainResponse login(
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="password", required=true) String password
			) throws EmpowerHerBizException {

		CustomerResponse customerResp = customerService.login(username, password);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(customerResp);

		return mainResponse;

	}

	@PutMapping(value="/{customerId}/updateProfile")
	public MainResponse updateProfile(@PathVariable(value = "customerId") String customerId,
									   @RequestBody CustomerResponse customerResponse) {

		Customer customer = customerService.updateCustomerProfile(customerId,customerResponse);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(customer);

		return mainResponse;
	}
}
