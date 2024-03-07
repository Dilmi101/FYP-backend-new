package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.model.AddExpenseRequest;

public interface ExpenseService {

	public HashMap<String, String> addExpense(AddExpenseRequest addExpenseRequest);

}
