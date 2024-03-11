package com.fyp.ehb.service;

import java.util.HashMap;
import java.util.List;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.ExpenseResponse;

public interface ExpenseService {

	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest);

	public HashMap<String, String> editExpense(String customerId, String expenseId,
			AddExpenseRequest addExpenseRequest);

	public HashMap<String, String> deleteExpense(String customerId, String expenseId);

	public List<ExpenseResponse> getExpenseList(String customerId);

}
