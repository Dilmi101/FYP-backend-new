package com.fyp.ehb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Dashboard;
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.model.DashboardCustomResponse;
import com.fyp.ehb.model.DashboardD;
import com.fyp.ehb.model.ExpenseResponse;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.MainDashboard;
import com.fyp.ehb.model.RawMaterialResponse;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.DashboardDao;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private DashboardDao dashboardDao;
	
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private GoalService goalService;
    
    @Autowired
    private RawMaterialService rawMaterialService;

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
			dash.setStatus("A");
			
			if(d.getType().equalsIgnoreCase("RAW")) {
				dash.setRawMaterialId(d.getRawMatId());
			} else if(d.getType().equalsIgnoreCase("EXPENSE")) {
				dash.setExpenseId(d.getExpenseId());
			} else if(d.getType().equalsIgnoreCase("GOAL")) {
				dash.setGoalId(d.getGoalId());
			}
			
			dash = dashboardDao.save(dash);
			
		}
		
		hm.put("code", "000");
		hm.put("message", "Items added to dashboard.");
		
		return hm;
	}

	@Override
	public List<DashboardCustomResponse> getDashboardItems(String customerId) throws Exception {

		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			return null;
		}
				
        Query query = new Query();
        query.addCriteria(Criteria.where("customer").is(customer.get()));
        List<Dashboard> dashboardList=  mongoTemplate.find(query, Dashboard.class);
        
        List<DashboardCustomResponse> dash = new ArrayList<>();
        
        for(Dashboard d : dashboardList) {
        	
        	if(!d.getStatus().equalsIgnoreCase("A")) {
        		continue;
        	}
        	
        	DashboardCustomResponse response = new DashboardCustomResponse();
        	response.setDashBrdId(d.getId());
        	
        	if(d.getType().equalsIgnoreCase("EXPENSE")) {
        		
        		ExpenseResponse exRes = expenseService.getExpenseById(d.getExpenseId());
        		
        		if(exRes.getExpenseStatus() != null && !exRes.getExpenseStatus().equalsIgnoreCase("A")) {
        			continue;
        		}
        		response.setAmount(exRes.getAmount());
        		response.setPercentage(exRes.getProgrssPercentage());
        		response.setTitle(exRes.getExpenseTitle());
        		response.setType("EXPENSE");
        		
        	} else if(d.getType().equalsIgnoreCase("GOAL")) {
        		
        		GoalResponse gRes = goalService.getGoalById(d.getGoalId());
        		
        		if(gRes.getGoalStatus() != null && !gRes.getGoalStatus().equalsIgnoreCase("A")) {
        			continue;
        		}
        		response.setAmount(gRes.getTarget());
        		response.setPercentage(gRes.getProgressPercentage());
        		response.setTitle(gRes.getGoalTitle());
        		response.setUnit(gRes.getUnit());
        		response.setType("GOAL");
        		
        	} else if(d.getType().equalsIgnoreCase("RAW")) {
        		
        		RawMaterialResponse rRes = rawMaterialService.getRawMaterialsById(d.getRawMaterialId());
        		
        		if(rRes.getStatus() != null && !rRes.getStatus().equalsIgnoreCase("A")) {
        			continue;
        		}
        		response.setRawMatAvailability(rRes.getAvailability());
        		response.setRemainingStock(rRes.getRemainingStock());
        		response.setTitle(rRes.getName());
        		response.setType("RAW");
        	}
        	
        	dash.add(response);
        }
		return dash;
	}

	@Override
	public HashMap<String, String> deleteDashboardItem(String id) throws Exception {
		
		Optional<Dashboard> existingDash = dashboardDao.findById(id);
		Dashboard dash = existingDash.get();
		
		HashMap<String, String> hm = new HashMap<>();
		
		dash.setStatus("D");
		dash = dashboardDao.save(dash);
		
		if(dash != null) {
			hm.put("code", "000");
			hm.put("message", "Item deleted successfully.");
		}
		return hm;
	}

}
