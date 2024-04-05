package com.fyp.ehb.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyp.ehb.model.MainDashboard;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;
	
	@PostMapping(value="/{customerId}/create")
	public MainResponse createDashboard(@RequestBody MainDashboard dashboard, @PathVariable(value = "customerId") String customerId) throws Exception {
		
		HashMap<String, String> hm = dashboardService.createDashboard(dashboard, customerId);
		
		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(hm);
		
		return mainResponse;
	}
}
