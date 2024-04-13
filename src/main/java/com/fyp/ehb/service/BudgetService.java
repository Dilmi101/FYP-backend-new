package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.model.BudgetCreateRequest;

public interface BudgetService {

	public HashMap<String, String> createBudget(String customerId, BudgetCreateRequest budgetCreateRequest) throws Exception;

}
