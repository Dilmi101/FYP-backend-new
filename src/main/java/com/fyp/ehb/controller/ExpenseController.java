package com.fyp.ehb.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.ExpenseService;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@PostMapping(value="/add")
	public MainResponse addExpense(@RequestBody AddExpenseRequest addExpenseRequest) {
	
		HashMap<String, String> response = expenseService.addExpense(addExpenseRequest);
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(mainResponse);
		
		return mainResponse;
	}
}
