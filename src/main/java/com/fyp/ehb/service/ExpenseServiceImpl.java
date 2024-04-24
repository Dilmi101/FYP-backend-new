package com.fyp.ehb.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.domain.ExpenseHistory;
import com.fyp.ehb.domain.Goal;
import com.fyp.ehb.domain.GoalHistory;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.enums.Status;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.ExpenseHistoryMain;
import com.fyp.ehb.model.ExpenseResponse;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.ExpenseDao;
import com.fyp.ehb.repository.ExpenseHistoryDao;
import com.fyp.ehb.util.EhbUtil;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ExpenseDao expenseDao;
	
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ExpenseHistoryDao expenseHistoryDao;

	@Override
	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest) throws Exception {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
			    
		Customer cust = customer.get();
		
	    String sDate = addExpenseRequest.getStartDate();  
	    Date startDate = null;
		try {
			startDate = EhbUtil.dateFromString(sDate, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	      
	    String eDate = addExpenseRequest.getEndDate();  
	    Date endDate = new Date();
		try {
			endDate = EhbUtil.dateFromString(eDate, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
		String formattedAmout = formatCurrencyDecimalPoints(addExpenseRequest.getAmount());
		
		Expense expense = new Expense();
		expense.setAmount(formattedAmout);
		expense.setEndDate(endDate);
		expense.setExpenseCategory(addExpenseRequest.getExpenseCategory());
		expense.setExpenseDescription(addExpenseRequest.getExpenseDescription());
		expense.setExpenseStatus("A");
		expense.setExpenseTitle(addExpenseRequest.getExpenseTitle());
		expense.setReminder(addExpenseRequest.getReminder());
		expense.setStartDate(startDate);
		expense.setCustomer(cust);
		expense.setActualAmount("0");
		
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
			AddExpenseRequest addExpenseRequest) throws Exception {
		
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
				startDate = EhbUtil.dateFromString(sDate, "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		      
		    String eDate = addExpenseRequest.getEndDate();  
		    Date endDate = new Date();
			try {
				endDate = EhbUtil.dateFromString(eDate, "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String formattedAmout = formatCurrencyDecimalPoints(addExpenseRequest.getAmount());
			
			existingEx.setAmount(formattedAmout);
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
	public HashMap<String, String> deleteExpense(String customerId, String expenseId) throws Exception {

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
	public List<ExpenseResponse> getExpenseList(String customerId) throws Exception{
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			return new ArrayList<>();
		}
		
        Query query = new Query();
        query.addCriteria(Criteria.where("customer").is(customer.get()));
        List<Expense> expenses=  mongoTemplate.find(query, Expense.class);
        
        List<ExpenseResponse> expenseList = new ArrayList<>();
        
        for(Expense e: expenses) {
        	
        	if(!e.getExpenseStatus().equalsIgnoreCase("A")) {
        		continue;
        	}
        	
    		Date stdate = e.getStartDate();  
    		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    		String sd = dtFormat.format(stdate);
    		
    		Date endate = e.getEndDate();  
    		SimpleDateFormat edtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    		String ed = edtFormat.format(endate); 
    		
    		String formattedAmout = formatCurrencyDecimalPoints(e.getAmount());
        	
        	ExpenseResponse expense = new ExpenseResponse();
        	expense.setAmount(formattedAmout);
        	expense.setCustomer(customer.get().getId());
        	expense.setDuration(e.getDuration());
        	expense.setEndDate(ed);
        	expense.setExpenseCategory(e.getExpenseCategory());
        	expense.setExpenseDescription(e.getExpenseDescription());
        	expense.setExpenseStatus(e.getExpenseStatus());
        	expense.setExpenseTitle(e.getExpenseTitle());
        	expense.setId(e.getId());
        	expense.setReminder(e.getReminder());
        	expense.setStartDate(sd);
        	        	
        	Instant currentDate = Instant.now();
        	Instant endD = e.getEndDate().toInstant();

        	Duration duration = Duration.between(currentDate, endD);
        	long days = duration.toDays();
        	
            if(days < 0){
                continue;
            }
            
            expense.setRemainingDays(String.valueOf(days));
            
            List<ExpenseHistory> records = expenseHistoryDao.getExpenseHistoryById(e.getId());

            BigDecimal sum = new BigDecimal("0.00");

            if(!records.isEmpty()){
                for(ExpenseHistory record : records){
                    BigDecimal achieved = new BigDecimal(record.getAchievedAmount());
                    sum = sum.add(achieved);
                }
                
                MathContext mc = new MathContext(3);
                BigDecimal target = new BigDecimal(e.getAmount());
                BigDecimal remaining = target.subtract(sum);//target - sum;
                BigDecimal percentage = sum.divide(target, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

                expense.setProgrssPercentage(percentage.toString());
                expense.setPendingTarget(String.valueOf(remaining));
            }
            else{
            	expense.setProgrssPercentage("0");
            	expense.setPendingTarget(e.getAmount());
            }
            
        	expenseList.add(expense);          

        }
        
		return expenseList.stream()
				.sorted(Comparator.comparing(ExpenseResponse :: getStartDate).reversed())
				.collect(Collectors.toList());
	}

	@Override
	public ExpenseResponse getExpenseById(String expenseId) throws Exception{

		ExpenseResponse response = new ExpenseResponse();
		
		Optional<Expense> expense = expenseDao.findById(expenseId);
		
		if(!expense.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_NOT_FOUND);
		}
		
		Expense existingExpense = expense.get();
		
		Date startDate = existingExpense.getStartDate();  
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String strDate = dateFormat1.format(startDate);  
		
		Date endDate = existingExpense.getEndDate();  
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String eDate = dateFormat2.format(endDate);  
		
		response.setAmount(existingExpense.getAmount());
		response.setCustomer(existingExpense.getCustomer().getId());
		response.setDuration(existingExpense.getDuration());
		response.setEndDate(eDate);
		response.setExpenseCategory(existingExpense.getExpenseCategory());
		response.setExpenseDescription(existingExpense.getExpenseDescription());
		response.setExpenseStatus(existingExpense.getExpenseStatus());
		response.setExpenseTitle(existingExpense.getExpenseTitle());
		response.setId(existingExpense.getId());
		response.setReminder(existingExpense.getReminder());
		response.setStartDate(strDate);
		
        List<ExpenseHistory> records = expenseHistoryDao.getExpenseHistoryById(existingExpense.getId());

        List<ExpenseHistoryMain> exArr = new ArrayList<>();
        
        BigDecimal sum = new BigDecimal("0.00");

        if(!records.isEmpty()){

            for(ExpenseHistory record : records){
                BigDecimal achieved = new BigDecimal(record.getAchievedAmount());
                sum = sum.add(achieved);
                
        		Date expCreatedD = record.getCreatedDate();  
        		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        		String exD = dtFormat.format(expCreatedD); 
        		
                ExpenseHistoryMain exmainRes = new ExpenseHistoryMain();
                exmainRes.setAmount(record.getAchievedAmount());
                exmainRes.setExpenseHistIds(record.getId());
                exmainRes.setCreatedDate(exD);
                
                exArr.add(exmainRes);
            }
            
        	Instant currentDate = Instant.now();
        	Instant endD = existingExpense.getEndDate().toInstant();

        	Duration duration = Duration.between(currentDate, endD);
        	long days = duration.toDays();
        	
            response.setHistoryRecords(exArr);
            response.setRemainingDays(String.valueOf(days));
            
            if(days < 0){
            	response.setRemainingDays(String.valueOf(0));
            }

            MathContext mc = new MathContext(3);
            BigDecimal target = new BigDecimal(existingExpense.getAmount());
            BigDecimal remaining = target.subtract(sum);//target - sum;
            BigDecimal percentage = sum.divide(target, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

            response.setProgrssPercentage(percentage.toString());
            response.setPendingTarget(String.valueOf(remaining));
        }
        else{
        	response.setPendingTarget(existingExpense.getAmount());
        	response.setProgrssPercentage("0");
        }
				
		return response;
	}

	@Override
	public List<ExpenseResponse> searchExpenses(String status, String category, String fromDate, String toDate)
			throws Exception {
		
		List<ExpenseResponse> exList = new ArrayList<>();
		
	    String sDate = fromDate;  
	    Date startDate = new Date();
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	      
	    String eDate = toDate;  
	    Date endDate = new Date();
		try {
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
        Query query = new Query();

        if(status != null && !status.isEmpty()) {
            query.addCriteria(Criteria.where("expenseStatus").is(status));
        }
        if(category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("expenseCategory").is(category));
        }
        if(fromDate != null && !fromDate.isEmpty()) {
            query.addCriteria(Criteria.where("startDate").gte(startDate));
        }
        if(toDate != null && !toDate.isEmpty()) {
            query.addCriteria(Criteria.where("endDate").lte(endDate));
        }

        List<Expense> expenseList =  mongoTemplate.find(query, Expense.class);
        
        if(!expenseList.isEmpty() && expenseList != null) {
        	
        	for(Expense e : expenseList) {
        		
        		if(!e.getExpenseStatus().equalsIgnoreCase("A")) {
        			continue;
        		}
        		
        		ExpenseResponse expenseResponse = new ExpenseResponse();
        		        		
        		Date sd = e.getStartDate();  
        		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        		String strDate = dateFormat1.format(sd);  
        		
        		Date endD = e.getStartDate();  
        		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        		String ed = dateFormat2.format(endD);  
        		
        		expenseResponse.setAmount(e.getAmount());
        		expenseResponse.setCustomer(e.getCustomer().getId());
        		expenseResponse.setDuration(e.getDuration());
        		expenseResponse.setEndDate(ed);
        		expenseResponse.setExpenseCategory(e.getExpenseCategory());
        		expenseResponse.setExpenseDescription(e.getExpenseDescription());
        		expenseResponse.setExpenseStatus(e.getExpenseStatus());
        		expenseResponse.setExpenseTitle(e.getExpenseTitle());
        		expenseResponse.setId(e.getId());
        		expenseResponse.setReminder(e.getReminder());
        		expenseResponse.setStartDate(strDate);
        		
            	Instant currentDate = Instant.now();
            	Instant endDt = e.getEndDate().toInstant();

            	Duration duration = Duration.between(currentDate, endDt);
            	long days = duration.toDays();
            	
                if(days < 0){
                    continue;
                }
                
                expenseResponse.setRemainingDays(String.valueOf(days));
                
                List<ExpenseHistory> records = expenseHistoryDao.getExpenseHistoryById(e.getId());

                BigDecimal sum = new BigDecimal("0.00");

                if(!records.isEmpty()){
                    for(ExpenseHistory record : records){
                        BigDecimal achieved = new BigDecimal(record.getAchievedAmount());
                        sum = sum.add(achieved);
                    }
                    
                    MathContext mc = new MathContext(3);
                    BigDecimal target = new BigDecimal(e.getAmount());
                    BigDecimal remaining = target.subtract(sum);//target - sum;
                    BigDecimal percentage = sum.divide(target, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

                    expenseResponse.setProgrssPercentage(percentage.toString());
                    expenseResponse.setPendingTarget(String.valueOf(remaining));
                }
                else{
                	expenseResponse.setProgrssPercentage("0");
                	expenseResponse.setPendingTarget(e.getAmount());
                }
        		
        		exList.add(expenseResponse);
        	}
        } else {
        	new ArrayList<>();
        }
        
		return exList;
	}

	@Override
	@Transactional
	public HashMap<String, String> updateExpenseProgressById(String id, String amount) throws Exception {

		Optional<Expense> expense = expenseDao.findById(id);
		
		if(expense.isPresent()) {
			if(Double.parseDouble(amount) > 0) {
				
				Expense ex = expense.get();
				
				if(new BigDecimal(amount).compareTo(new BigDecimal(ex.getAmount())) == 1) {
					throw new EmpowerHerBizException(EmpowerHerBizError.REACHED_TARGET_EXPENSE);
				}
				
				BigDecimal sum = new BigDecimal(0);
				
                List<ExpenseHistory> records = expenseHistoryDao.getExpenseHistoryById(ex.getId());
                
                if(records != null && !records.isEmpty()) {
                    for(ExpenseHistory eh : records) {
                    	if(eh.getAchievedAmount() != null) {
                        	sum = sum.add(new BigDecimal(eh.getAchievedAmount()));
                    	}
                    }
                }
                
                BigDecimal sum1 = sum;
                sum = sum1.add(new BigDecimal(amount));
                
				if(sum.compareTo(new BigDecimal(ex.getAmount())) == 1) {
					throw new EmpowerHerBizException(EmpowerHerBizError.REACHED_TARGET_EXPENSE);
				}
                
				if(sum.compareTo(new BigDecimal(ex.getAmount())) == 0) {
					ex.setExpenseStatus("C");
					expenseDao.save(ex);
				}
				
				ExpenseHistory record = new ExpenseHistory();
				record.setExpense(expense.get());
				record.setAchievedAmount(amount);
				record.setCreatedDate(new Date());
				
				record = expenseHistoryDao.save(record);
				
				ex.setActualAmount(String.valueOf(sum));
				expenseDao.save(ex);
				
                HashMap<String, String> hm = new HashMap<>();

                if(record != null){
                	hm.put("code", "000");
                    hm.put("message", "Your expense has been successfully recorded.");
                }
                else{
                	hm.put("code", "999");
                    hm.put("message", "Failed");
                }
                return hm;
			} else{
                throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_AMOUNT);
            }
		} else{
            throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_NOT_FOUND);
        }

	}

	public static synchronized String formatCurrencyDecimalPoints(String value){
		
		try {
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(Double.parseDouble(value));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return value;
		}
	}
	
}
