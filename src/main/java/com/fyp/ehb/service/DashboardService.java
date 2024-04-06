package com.fyp.ehb.service;

import java.util.HashMap;
import java.util.List;

import com.fyp.ehb.model.DashboardCustomResponse;
import com.fyp.ehb.model.MainDashboard;

public interface DashboardService {

	public HashMap<String, String> createDashboard(MainDashboard dashboard, String customerId) throws Exception;

	public List<DashboardCustomResponse> getDashboardItems(String customerId) throws Exception;

	public HashMap<String, String> deleteDashboardItem(String id) throws Exception;

}
