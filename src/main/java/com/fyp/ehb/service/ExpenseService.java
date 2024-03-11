package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.model.AddExpenseRequest;

public interface ExpenseService {

	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest);

	public HashMap<String, String> editExpense(String customerId, String expenseId,
			AddExpenseRequest addExpenseRequest);

}
