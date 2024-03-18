package com.fyp.ehb.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.AddRawMaterialRequest;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.RawMaterialService;

@RestController
@RequestMapping("/api/rawMaterial")
public class RawMaterialController {
	
//	@Autowired
//	private RawMaterialService rawMaterialService;
	
	@PostMapping(value="/{customerId}/add")
	public MainResponse addRawMaterial(@RequestBody AddRawMaterialRequest addRawMaterialRequest) {
				
	//	HashMap<String, String> hm = rawMaterialService.addRawMaterial(addExpenseRequest);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(mainResponse);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/update/{id}")
	public MainResponse updateRawMaterial(@RequestBody AddRawMaterialRequest addRawMaterialRequest) {
				
//		HashMap<String, String> hm = rawMaterialService.updateRawMaterial(addExpenseRequest);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(mainResponse);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/delete/{id}")
	public MainResponse deleteRawMaterial(@RequestBody AddExpenseRequest addExpenseRequest) {
				
//		HashMap<String, String> hm = rawMaterialService.deleteRawMaterial(addExpenseRequest);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(mainResponse);
		
		return mainResponse;
	}

}
