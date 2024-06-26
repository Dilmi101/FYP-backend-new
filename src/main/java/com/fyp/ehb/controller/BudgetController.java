package com.fyp.ehb.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.BudgetCreateRequest;
import com.fyp.ehb.model.BudgetCreateRequest.BudgetCreateSubRequest;
import com.fyp.ehb.model.BudgetCreateSubResponse;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.BudgetService;


@RestController
@RequestMapping("/api/budget")
public class BudgetController {
	
	@Autowired
	private BudgetService budgetService;

	@PostMapping(value="/{customerId}/create")
	public MainResponse createBudget(@RequestBody BudgetCreateRequest budgetCreateRequest,
			@PathVariable("customerId") String customerId) throws Exception {
		
		HashMap<String, String> hm = budgetService.createBudget(customerId, budgetCreateRequest);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(hm);
		
		return mainResponse;
		
		
	}
	
	@GetMapping(value="/{customerId}/budgetList")
	public MainResponse getBudgetList(
			@RequestParam(name = "type", required = true, defaultValue = "PLANNED") String type,
			@RequestParam(name = "month", required = true) String month,
			@PathVariable(value = "customerId") String customerId) throws Exception {
		
		BudgetCreateSubResponse response = budgetService.getBudgetList(customerId, month, type);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
}
