package com.fyp.ehb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.ExpenseHistory;
import com.fyp.ehb.domain.GoalHistory;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseResponse {

	private String id;
	private String expenseTitle;
	private String expenseCategory;
	private String expenseDescription;
	private String amount;
	private int duration;
	private String startDate;
	private String endDate;
	private String reminder;
	private String expenseStatus;
	private String customer;
	private String remainingDays;
	private String progrssPercentage;
    private String pendingTarget;
    private String isDashboardItem;
    private String isAchieved;
    private List<ExpenseHistoryMain> historyRecords;
}
