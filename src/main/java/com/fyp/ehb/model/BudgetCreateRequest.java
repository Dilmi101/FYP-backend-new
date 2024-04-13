package com.fyp.ehb.model;

import java.util.List;

import lombok.Data;

@Data
public class BudgetCreateRequest {

	private List<BudgetCreateSubRequest> expenses;
	
	@Data
	public static class BudgetCreateSubRequest {
		
		private String expenseId;
		private String actualAmt;
		private String title;
		private String month;
		
	}
}
