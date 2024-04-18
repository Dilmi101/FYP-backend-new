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
		rawMaterial.setRemainingStock(String.valueOf(addRawMaterialRequest.getLowStockLevel()));
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
			existingRaw.setAvailability(addRawMaterialRequest.getAvailability());
			existingRaw.setLowStockLvl(String.valueOf(addRawMaterialRequest.getLowStockLevel()));
			existingRaw.setName(addRawMaterialRequest.getRawMaterialName());
			existingRaw.setRemainingStock(String.valueOf(addRawMaterialRequest.getLowStockLevel()));
			existingRaw.setReminder(addRawMaterialRequest.getReminder());
			existingRaw.setSupplierEmail(addRawMaterialRequest.getSupplierEmailAddress());
			existingRaw.setSupplierName(addRawMaterialRequest.getSupplierName());
			existingRaw.setUnit(addRawMaterialRequest.getUnit());
			
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
        	
        	RawMaterialResponse response = new RawMaterialResponse();
        	response.setAvailability(availability);
        	response.setName(r.getName());
        	response.setRemainingStock(r.getRemainingStock());
        	response.setRawMateId(r.getId());
        	
        	rawMaterialList.add(response);
        	
        }
    	return rawMaterialList;

	}

	@Override
	public HashMap<String, String> updateProgressById(String id, int unit, String action) throws Exception {
		
		HashMap<String, String> hm = new HashMap<>();
		Optional<RawMaterial> rawMaterial = rawMaterialDao.findById(id);
		
		if(rawMaterial.isPresent()) {
			
			RawMaterial raw = rawMaterial.get();
			int sum = Integer.parseInt(raw.getRemainingStock());
			
			if(unit > sum && action.equalsIgnoreCase("USED")) {
				throw new EmpowerHerBizException(EmpowerHerBizError.YOU_HAVE_REACHED_LOW_STOCK_LEVEL);
			}
			
			if(sum > 0 && action.equalsIgnoreCase("USED")) {
				sum = sum - unit;
			}
			
			if(action.equalsIgnoreCase("REFILL")) {
				sum = sum + unit;
			}
			
			if(Integer.parseInt(raw.getLowStockLvl()) >= sum) {
				throw new EmpowerHerBizException(EmpowerHerBizError.YOU_HAVE_REACHED_LOW_STOCK_LEVEL);
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
            	hm.put("code", "000");
                hm.put("message", "Raw material has been updated.");
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
		
		Optional<RawMaterial> existingRawMaterial = rawMaterialDao.findById(rawMaterialId);
		
		if(existingRawMaterial.isPresent()) {
			
			RawMaterial raw = existingRawMaterial.get();
			
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
				response.setAvailability(raw.getAvailability());
				response.setName(raw.getName());
				response.setRawMateId(raw.getId());
				response.setRemainingStock(raw.getRemainingStock());
				response.setLowStockLvl(raw.getLowStockLvl());
				response.setReminder(raw.getReminder());
				response.setSupplierEmail(raw.getSupplierEmail());
				response.setSupplierName(raw.getSupplierName());
				response.setUnit(raw.getUnit());
			}
		}
		return response;
	}

}
