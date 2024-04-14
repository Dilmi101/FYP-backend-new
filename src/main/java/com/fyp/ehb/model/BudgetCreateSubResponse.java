package com.fyp.ehb.model;

import java.util.List;

import com.fyp.ehb.model.BudgetCreateRequest.BudgetCreateSubRequest;

import lombok.Data;

@Data
public class BudgetCreateSubResponse {

	private String totalBudget;
	private List<BudgetCreateSubRequest> expenseList;
}
