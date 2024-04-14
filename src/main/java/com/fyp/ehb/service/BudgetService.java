package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.model.BudgetCreateRequest;
import com.fyp.ehb.model.BudgetCreateSubResponse;

public interface BudgetService {

	public HashMap<String, String> createBudget(String customerId, BudgetCreateRequest budgetCreateRequest) throws Exception;

	public BudgetCreateSubResponse getBudgetList(String customerId, String month, String type) throws Exception;

}
