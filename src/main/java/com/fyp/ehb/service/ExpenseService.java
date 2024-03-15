package com.fyp.ehb.service;

import java.util.HashMap;
import java.util.List;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.ExpenseResponse;

public interface ExpenseService {

	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest) throws Exception;

	public HashMap<String, String> editExpense(String customerId, String expenseId,
			AddExpenseRequest addExpenseRequest) throws Exception;

	public HashMap<String, String> deleteExpense(String customerId, String expenseId) throws Exception;

	public List<ExpenseResponse> getExpenseList(String customerId) throws Exception;

	public ExpenseResponse getExpenseById(String expenseId) throws Exception;

	public List<ExpenseResponse> searchExpenses(String status, String category, String fromDate, String toDate) throws Exception;

}
