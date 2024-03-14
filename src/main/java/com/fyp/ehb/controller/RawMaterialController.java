package com.fyp.ehb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.MainResponse;

@RestController
@RequestMapping("/api/rawMaterial")
public class RawMaterialController {
	
	@PostMapping(value="/{customerId}/add")
	public MainResponse addRawMaterial(@RequestBody AddExpenseRequest addExpenseRequest) {
				
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(mainResponse);
		
		return mainResponse;
	}
	

}
