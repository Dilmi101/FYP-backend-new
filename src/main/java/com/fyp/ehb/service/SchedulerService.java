package com.fyp.ehb.service;

import com.fyp.ehb.model.ExpenseResponse;

import java.util.List;

public interface SchedulerService {

	public void goalReminder() throws Exception;
	public void expenseReminder() throws Exception;
	public void rowmaterialsReminder() throws Exception;
	public void remindeUnfinishedGoalStatus() throws Exception;
	public List<ExpenseResponse> expenseReminderListGet() throws Exception;
}
