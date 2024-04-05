package com.fyp.ehb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Dashboard;
import com.fyp.ehb.model.DashboardD;
import com.fyp.ehb.model.MainDashboard;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.DashboardDao;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private DashboardDao dashboardDao;

	@Override
	public HashMap<String, String> createDashboard(MainDashboard dashboard, String customerId) throws Exception {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			return null;
		}
		
		HashMap<String, String> hm = new HashMap<>();
		
		for(DashboardD d : dashboard.getDashboardList()) {
			
			Dashboard dash = new Dashboard();
			dash.setCustomer(customer.get());
			dash.setType(d.getType());
			
			if(d.getType().equalsIgnoreCase("RAW")) {
				dash.setRawMaterialId(d.getRawMatId());
			} else if(d.getType().equalsIgnoreCase("EXPENSE")) {
				dash.setExpenseId(d.getExpenseId());
			} else if(d.getType().equalsIgnoreCase("GOAL")) {
				dash.setExpenseId(d.getGoalId());
			}
			
			dash = dashboardDao.save(dash);
			
		}
		
		hm.put("code", "000");
		hm.put("message", "Items added to dashboard.");
		
		return hm;
	}

}
