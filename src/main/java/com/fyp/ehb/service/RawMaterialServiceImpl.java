package com.fyp.ehb.service;

import java.time.Duration;
import java.time.Instant;
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
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.domain.RawMaterial;
import com.fyp.ehb.domain.RawMaterialHistory;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddRawMaterialRequest;
import com.fyp.ehb.model.ExpenseResponse;
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
        	
        	RawMaterialResponse response = new RawMaterialResponse();
        	response.setAvailability(r.getAvailability());
        	response.setLowStockLvl(r.getLowStockLvl());
        	response.setName(r.getName());
        	response.setRemainingStock(r.getRemainingStock());
        	response.setReminder(r.getReminder());
        	response.setStatus(r.getStatus());
        	response.setSupplierEmail(r.getSupplierEmail());
        	response.setSupplierName(r.getSupplierName());
        	response.setUnit(r.getUnit());
        	response.setCustomerId(customerId);
        	
  //      	List<RawMaterialHistory> rawHistory = rawMaterialHistoryDao.getRawMaterialsById(r.getId());
        	
        }
    	return null;

	}

}
