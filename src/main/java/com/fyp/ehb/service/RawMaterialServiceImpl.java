package com.fyp.ehb.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
import com.fyp.ehb.domain.RawMaterial;
import com.fyp.ehb.domain.RawMaterialHistory;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddRawMaterialRequest;
import com.fyp.ehb.model.ExpenseResponse;
import com.fyp.ehb.model.RawMaterialHistoryMain;
import com.fyp.ehb.model.RawMaterialResponse;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.RawMaterialHistoryDao;
import com.fyp.ehb.repository.RwMaterialDao;

@Service
public class RawMaterialServiceImpl implements RawMaterialService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private RwMaterialDao rawMaterialDao;
	
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private RawMaterialHistoryDao rawMaterialHistoryDao;

	@Autowired
	private NotificationService notificationService;
	
	@Override
	public HashMap<String, String> addRawMaterial(AddRawMaterialRequest addRawMaterialRequest, String customerId)
			throws EmpowerHerBizException {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
		
		RawMaterial rawMaterial = new RawMaterial();
		rawMaterial.setAvailability(addRawMaterialRequest.getAvailability());
		rawMaterial.setCustomer(customer.get());
		rawMaterial.setLowStockLvl(String.valueOf(addRawMaterialRequest.getLowStockLevel()));
		rawMaterial.setName(addRawMaterialRequest.getRawMaterialName());
		rawMaterial.setRemainingStock(String.valueOf(addRawMaterialRequest.getStockRemaining()));
		rawMaterial.setReminder(addRawMaterialRequest.getReminder());
		rawMaterial.setSupplierEmail(addRawMaterialRequest.getSupplierEmailAddress());
		rawMaterial.setSupplierName(addRawMaterialRequest.getSupplierName());
		rawMaterial.setUnit(addRawMaterialRequest.getUnit());
		rawMaterial.setStatus("A");
		
		rawMaterial = rawMaterialDao.save(rawMaterial);
		
		if(rawMaterial == null) {
			throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_CREATION_FAILED);
		}
		
		HashMap<String, String> hm = new HashMap<>();
		hm.put("code", "000");
		hm.put("message", "Raw material has been successfully added.");
		
		return hm;
	}

	@Override
	public HashMap<String, String> updateRawMaterial(AddRawMaterialRequest addRawMaterialRequest, String id)
			throws EmpowerHerBizException {

		Optional<RawMaterial> existingRawMaterial = rawMaterialDao.findById(id);
		
		RawMaterial existingRaw = existingRawMaterial.get();
		
		if(!existingRawMaterial.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_CANNOT_FOUND);
		} else {
			
			if(addRawMaterialRequest.getAvailability() != null && !addRawMaterialRequest.getAvailability().isEmpty()) {
				existingRaw.setAvailability(addRawMaterialRequest.getAvailability());
			}
			
			if(addRawMaterialRequest.getLowStockLevel() > 0) {
				existingRaw.setLowStockLvl(String.valueOf(addRawMaterialRequest.getLowStockLevel()));
			}
			
			if(addRawMaterialRequest.getRawMaterialName() != null && !addRawMaterialRequest.getRawMaterialName().isEmpty()) {
				existingRaw.setName(addRawMaterialRequest.getRawMaterialName());
			}
			
			if(addRawMaterialRequest.getStockRemaining() > 0) {
				existingRaw.setRemainingStock(String.valueOf(addRawMaterialRequest.getStockRemaining()));
			}
			
			if(addRawMaterialRequest.getReminder() != null && !addRawMaterialRequest.getReminder().isEmpty()) {
				existingRaw.setReminder(addRawMaterialRequest.getReminder());
			}
			
			if(addRawMaterialRequest.getSupplierEmailAddress() != null && !addRawMaterialRequest.getSupplierEmailAddress().isEmpty()) {
				existingRaw.setSupplierEmail(addRawMaterialRequest.getSupplierEmailAddress());
			}
			
			if(addRawMaterialRequest.getSupplierName() != null && !addRawMaterialRequest.getSupplierName().isEmpty()) {
				existingRaw.setSupplierName(addRawMaterialRequest.getSupplierName());
			}
			
			if(addRawMaterialRequest.getUnit() != null && !addRawMaterialRequest.getUnit().isEmpty()) {
				existingRaw.setUnit(addRawMaterialRequest.getUnit());
			}
						
			existingRaw = rawMaterialDao.save(existingRaw);
			
			if(existingRaw == null) {
				throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_UPDATE_FAILED);
			}
			
			HashMap<String, String> hm = new HashMap<>();
			hm.put("code", "000");
			hm.put("message", "Raw material details have been updated.");
			
			return hm;
		}
	}

	@Override
	public HashMap<String, String> deleteRawMaterial(String customerId, String id) throws EmpowerHerBizException {
		
		Optional<RawMaterial> existingRawMaterial = rawMaterialDao.findById(id);
		
		RawMaterial existingRaw = existingRawMaterial.get();
		
		if(!existingRawMaterial.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_CANNOT_FOUND);
		} else {
			existingRaw.setStatus("D");
			existingRaw = rawMaterialDao.save(existingRaw);
			
			if(existingRaw == null) {
				throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_UPDATE_FAILED);
			}
			
			HashMap<String, String> hm = new HashMap<>();
			hm.put("code", "000");
			hm.put("message", "Raw material details have been deleted.");
			
			return hm;
		}
	}

	@Override
	public List<RawMaterialResponse> getRawMaterials(String customerId) throws EmpowerHerBizException {

		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			return new ArrayList<>();
		}
		
        Query query = new Query();
        query.addCriteria(Criteria.where("customer").is(customer.get()));
        List<RawMaterial> rawMaterials=  mongoTemplate.find(query, RawMaterial.class);
        
        List<RawMaterialResponse> rawMaterialList = new ArrayList<>();
        
        for(RawMaterial r : rawMaterials) {
        	
        	if(!r.getStatus().equalsIgnoreCase("A")) {
        		continue;
        	}
        	
        	String availability = "N";
        	
        	if(Integer.parseInt(r.getRemainingStock()) > 0) {
        		availability = "Y";
        	}
        	
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("rawMaterialId").is(r.getId()));
            query2.addCriteria(Criteria.where("status").is("A"));
            Dashboard isDashboardItem =  mongoTemplate.findOne(query2, Dashboard.class);
        	
        	RawMaterialResponse response = new RawMaterialResponse();
        	response.setAvailability(availability);
        	response.setName(r.getName());
        	response.setRemainingStock(r.getRemainingStock());
        	response.setRawMateId(r.getId());
			response.setLowStockLvl(r.getLowStockLvl());
			response.setReminder(r.getReminder());
			response.setSupplierEmail(r.getSupplierEmail());
			response.setSupplierName(r.getSupplierName());
			response.setUnit(r.getUnit());
			
        	if(isDashboardItem != null && isDashboardItem.getStatus().equalsIgnoreCase("A")) {
        		response.setIsDashboardItem("Y");
        	} else {
        		response.setIsDashboardItem("N");
        	}
        	
        	rawMaterialList.add(response);
        	
        }
    	return rawMaterialList;

	}

	@Override
	public HashMap<String, String> updateProgressById(String id, int unit, String action) throws Exception {
		
		HashMap<String, String> hm = new HashMap<>();
		Optional<RawMaterial> rawMaterial = rawMaterialDao.findById(id);
		String isStockReached = "N";
		
		if(rawMaterial.isPresent()) {
			
			RawMaterial raw = rawMaterial.get();
			int sum = Integer.parseInt(raw.getRemainingStock());
			
			if(unit > Integer.parseInt(raw.getLowStockLvl()) && action.equalsIgnoreCase("USED")) {				
				isStockReached = "Y";
			}
			
			if(unit > sum && action.equalsIgnoreCase("USED")) {				
				isStockReached = "Y";
			}
			
			if(sum > 0 && action.equalsIgnoreCase("USED")) {
				
				isStockReached = "Y";
				
				sum = sum - unit;
				
				if(sum < 0) {
					sum = 0;
				}
			}
			
			if(action.equalsIgnoreCase("REFILL")) {
				sum = sum + unit;
			}

			if(Integer.parseInt(raw.getLowStockLvl()) >= sum) {
				try {
					if(raw.getSupplierEmail() != null && !raw.getSupplierEmail().isEmpty()){
						notificationService.sendSimpleMail(raw.getSupplierEmail(), raw.getSupplierName(), raw.getName(), raw.getCustomer().getBusiness().getBusinessName());
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			
			
			if(Integer.parseInt(raw.getLowStockLvl()) >= sum) {
				isStockReached = "Y";
			}
			
			RawMaterialHistory record = new RawMaterialHistory();
			record.setAction(action);
			record.setCount(unit);
			record.setRawMaterial(rawMaterial.get());
			record.setCreatedDate(new Date());
			record = rawMaterialHistoryDao.save(record);
			
			raw.setRemainingStock(String.valueOf(sum));
			raw = rawMaterialDao.save(raw);
			
			if(record != null){
				
				if(isStockReached.equalsIgnoreCase("Y")) {
					hm.put("code", "999");
					hm.put("message", "You have reached the low stock level. The respective supplier has been notified.");
					hm.put("isStockReached", "Y");
				} else {
	            	hm.put("code", "000");
	                hm.put("message", "Raw material has been updated.");
	                hm.put("isStockReached", "N");
				}

            }
            else{
            	hm.put("code", "999");
                hm.put("message", "Failed");
            }
			
		} else {
			throw new EmpowerHerBizException(EmpowerHerBizError.RAW_MATERIAL_CANNOT_FOUND);
		}
		return hm;
	}

	@Override
	public RawMaterialResponse getRawMaterialsById(String rawMaterialId) throws Exception {
		
		RawMaterialResponse response = new RawMaterialResponse();
		List<RawMaterialHistoryMain> rawHistoryList = new ArrayList<>();
		
    	String availability = "N";
		
		Optional<RawMaterial> existingRawMaterial = rawMaterialDao.findById(rawMaterialId);
		
		if(existingRawMaterial.isPresent()) {
			
			RawMaterial raw = existingRawMaterial.get();
	    	
	    	if(Integer.parseInt(raw.getRemainingStock()) > 0) {
	    		availability = "Y";
	    	}
			
			List<RawMaterialHistory> history = rawMaterialHistoryDao.getRawMaterialsById(rawMaterialId);
			
			if(history != null && !history.isEmpty()) {

				for(RawMaterialHistory h : history) {
					
	        		Date createdD = h.getCreatedDate();  
	        		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");  
	        		String cd = dtFormat.format(createdD); 
					
					RawMaterialHistoryMain rm = new RawMaterialHistoryMain();
					rm.setAction(h.getAction().equalsIgnoreCase("REFILL") ? "Re-fill" : "Used");
					rm.setCreatedDate(cd);
					rm.setValue(String.valueOf(h.getCount()));
					rawHistoryList.add(rm);
					
				}
				
				response.setRawHistoryList(rawHistoryList);
				response.setAvailability(availability);
				response.setName(raw.getName());
				response.setRawMateId(raw.getId());
				response.setRemainingStock(raw.getRemainingStock());
				response.setLowStockLvl(raw.getLowStockLvl());
				response.setReminder(raw.getReminder());
				response.setSupplierEmail(raw.getSupplierEmail());
				response.setSupplierName(raw.getSupplierName());
				response.setUnit(raw.getUnit());
				response.setStatus(raw.getStatus());
				
			} else {
				
				response.setAvailability(availability);
				response.setName(raw.getName());
				response.setRawMateId(raw.getId());
				response.setRemainingStock(raw.getRemainingStock());
				response.setLowStockLvl(raw.getLowStockLvl());
				response.setReminder(raw.getReminder());
				response.setSupplierEmail(raw.getSupplierEmail());
				response.setSupplierName(raw.getSupplierName());
				response.setUnit(raw.getUnit());
				response.setStatus(raw.getStatus());
			}
		}
		return response;
	}

}
