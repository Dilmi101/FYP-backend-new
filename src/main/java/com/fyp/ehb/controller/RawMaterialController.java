package com.fyp.ehb.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.AddRawMaterialRequest;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.model.RawMaterialResponse;
import com.fyp.ehb.service.RawMaterialService;

@RestController
@RequestMapping("/api/rawMaterial")
public class RawMaterialController {
	
	@Autowired
	private RawMaterialService rawMaterialService;
	
	@PostMapping(value="/{customerId}/add")
	public MainResponse addRawMaterial(@PathVariable(value = "customerId") String customerId, 
			@RequestBody AddRawMaterialRequest addRawMaterialRequest) {
				
		HashMap<String, String> hm = rawMaterialService.addRawMaterial(addRawMaterialRequest, customerId);
		
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
	
	@GetMapping(value="/{customerId}/rawMaterials")
	public MainResponse getRawMaterials(@PathVariable("customerId") String customerId) throws Exception {

		List<RawMaterialResponse> goals = rawMaterialService.getRawMaterials(customerId);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goals);

		return mainResponse;
	}
	
	@PutMapping(value="/{id}/updateProgress")
	public MainResponse updateProgressById(
			@PathVariable("id") String id,
			@RequestParam(name = "count") int count,
			@RequestParam(name = "action") String action) throws Exception {

		HashMap<String, String> response = rawMaterialService.updateProgressById(id, count, action);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);

		return mainResponse;
	}
	
	@GetMapping(value="/{rawMaterialId}")
	public MainResponse getRawMaterialsById(@PathVariable("rawMaterialId") String rawMaterialId) throws Exception {

		RawMaterialResponse rawMaterial = rawMaterialService.getRawMaterialsById(rawMaterialId);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(rawMaterial);

		return mainResponse;
	}

}
