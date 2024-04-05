package com.fyp.ehb.service;

import java.util.HashMap;

import com.fyp.ehb.model.MainDashboard;

public interface DashboardService {

	public HashMap<String, String> createDashboard(MainDashboard dashboard, String customerId) throws Exception;

}
