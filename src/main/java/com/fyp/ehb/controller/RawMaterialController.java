package com.fyp.ehb.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.AddRawMaterialRequest;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.RawMaterialService;

@RestController
@RequestMapping("/api/rawMaterial")
public class RawMaterialController {
	
	@Autowired
	private RawMaterialService rawMaterialService;
	
	@PostMapping(value="/{customerId}/add")
	public MainResponse addRawMaterial(@RequestBody AddRawMaterialRequest addRawMaterialRequest) {
				
		HashMap<String, String> hm = rawMaterialService.addRawMaterial(addRawMaterialRequest);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(hm);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/update/{id}")
	public MainResponse updateRawMaterial(@PathVariable(value = "id") String id,
			@RequestBody AddRawMaterialRequest addRawMaterialRequest) {
				
		HashMap<String, String> hm = rawMaterialService.updateRawMaterial(addRawMaterialRequest, id);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(hm);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/delete/{id}")
	public MainResponse deleteRawMaterial(@PathVariable(value = "customerId") String customerId,
			@PathVariable("id") String id) {
			
		HashMap<String, String> hm = rawMaterialService.deleteRawMaterial(customerId, id);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(hm);
		
		return mainResponse;
	}

}
