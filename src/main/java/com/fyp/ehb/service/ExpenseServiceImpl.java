package com.fyp.ehb.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.enums.Status;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.ExpenseResponse;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.ExpenseDao;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ExpenseDao expenseDao;
	
    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest) {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
			    
	    String sDate = addExpenseRequest.getStartDate();  
	    Date startDate = new Date();
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	      
	    String eDate = addExpenseRequest.getEndDate();  
	    Date endDate = new Date();
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
		Expense expense = new Expense();
		expense.setAmount(addExpenseRequest.getAmount());
		expense.setDuration(addExpenseRequest.getDuration());
		expense.setEndDate(endDate);
		expense.setExpenseCategory(addExpenseRequest.getExpenseCategory());
		expense.setExpenseDescription(addExpenseRequest.getExpenseDescription());
		expense.setExpenseStatus("A");
		expense.setExpenseTitle(addExpenseRequest.getExpenseTitle());
		expense.setReminder(addExpenseRequest.getReminder());
		expense.setStartDate(startDate);
		
		expense = expenseDao.save(expense);
		
		if(expense == null) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_CREATION_FAILED);
		}
		
		HashMap<String, String> hm = new HashMap<>();
		hm.put("code", "000");
		hm.put("message", "Your expense has been successfully added.");
		
		return hm;
	}

	@Override
	public HashMap<String, String> editExpense(String customerId, String expenseId,
			AddExpenseRequest addExpenseRequest) {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
		
		Optional<Expense> existingExpense = expenseDao.findById(expenseId);
		
		Expense existingEx = existingExpense.get();
		
		if(!existingExpense.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_NOT_FOUND);
		} else {
			
		    String sDate = addExpenseRequest.getStartDate();  
		    Date startDate = new Date();
			try {
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		      
		    String eDate = addExpenseRequest.getEndDate();  
		    Date endDate = new Date();
			try {
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			existingEx.setAmount(addExpenseRequest.getAmount());
			existingEx.setDuration(addExpenseRequest.getDuration());
			existingEx.setEndDate(endDate);
			existingEx.setExpenseCategory(addExpenseRequest.getExpenseCategory());
			existingEx.setExpenseDescription(addExpenseRequest.getExpenseDescription());
			existingEx.setExpenseStatus("A");
			existingEx.setExpenseTitle(addExpenseRequest.getExpenseTitle());
			existingEx.setReminder(addExpenseRequest.getReminder());
			existingEx.setStartDate(startDate);
			
			existingEx = expenseDao.save(existingEx);
		}
		
		if(existingEx == null) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_UPDATE_FAILED);
		}
		
		HashMap<String, String> hm = new HashMap<>();
		hm.put("code", "000");
		hm.put("message", "Expense details have been updated.");
		
		return hm;
	}

	@Override
	public HashMap<String, String> deleteExpense(String customerId, String expenseId) {

		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
		
		Optional<Expense> existingExpense = expenseDao.findById(expenseId);
		
		if(!existingExpense.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_NOT_FOUND);
		}
		
		Expense existingEx = existingExpense.get();
		existingEx.setExpenseStatus(Status.INACTIVE_STATUS.getStatus());
		
		existingEx = expenseDao.save(existingEx);
		
		if(existingEx == null) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_DELETE_FAILED);
		}
		
		HashMap<String, String> hm = new HashMap<>();
		hm.put("code", "000");
		hm.put("message", "Expense details have been deleted.");
		
		return hm;
	}

	@Override
	public List<ExpenseResponse> getExpenseList(String customerId) {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			return new ArrayList<>();
		}
		
        Query query = new Query();
        query.addCriteria(Criteria.where("customer").is(customer.get()));
        List<Expense> expenses=  mongoTemplate.find(query, Expense.class);
        
        List<ExpenseResponse> expenseList = new ArrayList<>();
        
        for(Expense e: expenses) {
        	
        	ExpenseResponse expense = new ExpenseResponse();
        	expense.setAmount(e.getAmount());
        	expense.setCustomer(customer.get());
        	expense.setDuration(e.getDuration());
        	expense.setEndDate(String.valueOf(e.getEndDate()));
        	expense.setExpenseCategory(e.getExpenseCategory());
        	expense.setExpenseDescription(e.getExpenseDescription());
        	expense.setExpenseStatus(e.getExpenseStatus());
        	expense.setExpenseTitle(e.getExpenseTitle());
        	expense.setId(e.getId());
        	expense.setReminder(e.getReminder());
        	expense.setStartDate(String.valueOf(e.getStartDate()));
        	
        	expenseList.add(expense);            
            
        }
        
		return expenseList.stream()
				.sorted(Comparator.comparing(ExpenseResponse :: getStartDate).reversed())
				.collect(Collectors.toList());
	}

	@Override
	public ExpenseResponse getExpenseById(String expenseId) {

		ExpenseResponse response = new ExpenseResponse();
		
		Optional<Expense> expense = expenseDao.findById(expenseId);
		
		if(!expense.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_NOT_FOUND);
		}
		
		Expense existingExpense = expense.get();
		
		Date startDate = existingExpense.getStartDate();  
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-mm-dd");  
		String strDate = dateFormat1.format(startDate);  
		
		Date sendDate = existingExpense.getStartDate();  
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-mm-dd");  
		String eDate = dateFormat2.format(sendDate);  
		
		response.setAmount(existingExpense.getAmount());
		response.setCustomer(existingExpense.getCustomer());
		response.setDuration(existingExpense.getDuration());
		response.setEndDate(eDate);
		response.setExpenseCategory(existingExpense.getExpenseCategory());
		response.setExpenseDescription(existingExpense.getExpenseDescription());
		response.setExpenseStatus(existingExpense.getExpenseStatus());
		response.setExpenseTitle(existingExpense.getExpenseTitle());
		response.setId(existingExpense.getId());
		response.setReminder(existingExpense.getReminder());
		response.setStartDate(strDate);
				
		return response;
	}

}
