package com.fyp.ehb.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.ExpenseDao;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ExpenseDao expenseDao;

	@Override
	public HashMap<String, String> addExpense(String customerId, AddExpenseRequest addExpenseRequest) {
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
		
		Expense expense = new Expense();
		expense.setAmount(addExpenseRequest.getAmount());
		expense.setDuration(addExpenseRequest.getDuration());
		expense.setEndDate(addExpenseRequest.getEndDate());
		expense.setExpenseCategory(addExpenseRequest.getExpenseCategory());
		expense.setExpenseDescription(addExpenseRequest.getExpenseDescription());
		expense.setExpenseStatus("A");
		expense.setExpenseTitle(addExpenseRequest.getExpenseTitle());
		expense.setReminder(addExpenseRequest.getReminder());
		expense.setStartDate(addExpenseRequest.getStartDate());
		
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
			
			existingEx.setAmount(addExpenseRequest.getAmount());
			existingEx.setDuration(addExpenseRequest.getDuration());
			existingEx.setEndDate(addExpenseRequest.getEndDate());
			existingEx.setExpenseCategory(addExpenseRequest.getExpenseCategory());
			existingEx.setExpenseDescription(addExpenseRequest.getExpenseDescription());
			existingEx.setExpenseStatus("A");
			existingEx.setExpenseTitle(addExpenseRequest.getExpenseTitle());
			existingEx.setReminder(addExpenseRequest.getReminder());
			existingEx.setStartDate(addExpenseRequest.getStartDate());
			
			existingEx = expenseDao.save(existingEx);
		}
		
		if(existingEx == null) {
			throw new EmpowerHerBizException(EmpowerHerBizError.EXPENSE_UPDATE_FAILED);
		}
		
		HashMap<String, String> hm = new HashMap<>();
		hm.put("code", "000");
		hm.put("message", "Expense details have been updated.");
		return null;
	}

}
