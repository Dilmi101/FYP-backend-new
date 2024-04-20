package com.fyp.ehb.service;

public interface SchedulerService {

	public void goalReminder() throws Exception;
	public void expenseReminder() throws Exception;
	public void rowmaterialsReminder() throws Exception;
	public void remindeUnfinishedGoalStatus() throws Exception;
}
