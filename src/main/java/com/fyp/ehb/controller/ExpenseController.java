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

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.ExpenseResponse;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.ExpenseService;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@PostMapping(value="/{customerId}/add")
	public MainResponse addExpense(@RequestBody AddExpenseRequest addExpenseRequest, @PathVariable(value = "customerId") String customerId) throws Exception {
	
		HashMap<String, String> response = expenseService.addExpense(customerId, addExpenseRequest);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/update/{expenseId}")
	public MainResponse editExpense(@RequestBody AddExpenseRequest addExpenseRequest, @PathVariable(value = "customerId") String customerId,
			@PathVariable("expenseId") String expenseId) throws Exception  {
	
		HashMap<String, String> response = expenseService.editExpense(customerId, expenseId, addExpenseRequest);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
	
	@PutMapping(value="/{customerId}/delete/{expenseId}")
	public MainResponse deleteExpense(@PathVariable(value = "customerId") String customerId,
			@PathVariable("expenseId") String expenseId) throws Exception {
	
		HashMap<String, String> response = expenseService.deleteExpense(customerId, expenseId);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
	
	@GetMapping(value="/{customerId}/expenseList")
	public MainResponse getExpenseList(@PathVariable(value = "customerId") String customerId) throws Exception {
	
		List<ExpenseResponse> response = expenseService.getExpenseList(customerId);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
	
	@GetMapping(value="/{expenseId}")
	public MainResponse getExpenseById(@PathVariable(value = "expenseId") String expenseId) throws Exception {
	
		ExpenseResponse response = expenseService.getExpenseById(expenseId);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);
		
		return mainResponse;
	}
	
	@GetMapping(value="/search")
	public MainResponse searchExpense(
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate
	) throws Exception {

		List<ExpenseResponse> goals = expenseService.searchExpenses(status, category, fromDate, toDate);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goals);

		return mainResponse;
	}
}
