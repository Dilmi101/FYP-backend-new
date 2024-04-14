package com.fyp.ehb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Budget;
import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.domain.ExpenseHistory;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.BudgetCreateRequest;
import com.fyp.ehb.model.BudgetCreateRequest.BudgetCreateSubRequest;
import com.fyp.ehb.model.BudgetCreateSubResponse;
import com.fyp.ehb.repository.BudgetDao;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.ExpenseDao;
import com.fyp.ehb.repository.ExpenseHistoryDao;

@Service
public class BudgetServiceImpl implements BudgetService {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BudgetDao budgetDao;
	
	@Autowired
	private ExpenseHistoryDao expenseHistoryDao;
	
	@Autowired
	private ExpenseDao expenseDao;

	@Override
	public HashMap<String, String> createBudget(String customerId, BudgetCreateRequest budgetCreateRequest)
			throws Exception {
		
		//get month from FE >> JAN,FEB and this into db
		//write a search query for budget expense list
		HashMap<String, String> hm = new HashMap<>();
		
		Optional<Customer> customer = customerDao.findById(customerId);
		
		if(!customer.isPresent()) {
			throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
		}
		
		List<Budget> existingList = budgetDao.findByCustomerId(customerId);
		
		if(existingList != null && !existingList.isEmpty()) {
			
			for(Budget b : existingList) {
				b.setStatus("D");
				budgetDao.save(b);
			}
		}
		
		for(BudgetCreateSubRequest subReq : budgetCreateRequest.getExpenses()) {
			
			Budget newBudget = new Budget();
			newBudget.setAmount(subReq.getActualAmt());
			newBudget.setCustomer(customer.get());
			newBudget.setExpenseId(subReq.getExpenseId());
			newBudget.setMonth(subReq.getMonth().toUpperCase());
			newBudget.setTitle(subReq.getTitle());
			newBudget.setStatus("A");
			newBudget.setType("PLANNED");
			
			newBudget = budgetDao.save(newBudget);
			
			if(newBudget != null) {
				hm.put("code", "000");
				hm.put("message", "Your budget plan has been successfully created.");
			} else {
				hm.put("code", "999");
				hm.put("message", "Cannot create your budget plan.");
			}
		}
				
		return hm;
	}

	@Override
	public BudgetCreateSubResponse getBudgetList(String customerId, String month, String type) throws Exception {
		
		BudgetCreateSubResponse budget = new BudgetCreateSubResponse();
		List<BudgetCreateSubRequest> list = new ArrayList<>();
		BigDecimal sum = new BigDecimal(0);
		
		List<Budget> budgetList = budgetDao.getBudgetListByCustomerAndMonth(customerId, month.toUpperCase());
		
		if(type.equalsIgnoreCase("PLANNED")) {

			for(Budget b : budgetList) {
				
				if(!b.getStatus().equalsIgnoreCase("A")) {
					continue;
				}
				
				sum = sum.add(new BigDecimal(b.getAmount()));
				
				BudgetCreateSubRequest response = new BudgetCreateSubRequest();
				response.setActualAmt(b.getAmount());
				response.setExpenseId(b.getExpenseId());
				response.setMonth(b.getMonth());
				response.setTitle(b.getTitle());
				list.add(response);
			}
			
			budget.setExpenseList(list);
			budget.setTotalBudget(sum.toString());
			
			
		} else {
			
			for(Budget b : budgetList) {
								
				if(!b.getStatus().equalsIgnoreCase("A")) {
					continue;
				}
				
				Optional<Expense> expense = expenseDao.findById(b.getExpenseId());
				Expense expen = expense.get();
				
				sum = sum.add(new BigDecimal(expen.getActualAmount()));
				
				BudgetCreateSubRequest response = new BudgetCreateSubRequest();
				response.setActualAmt(expen.getActualAmount());
				response.setExpenseId(b.getExpenseId());
				response.setMonth(b.getMonth());
				response.setTitle(b.getTitle());
				list.add(response);

			}
			
			budget.setExpenseList(list);
			budget.setTotalBudget(sum.toString());
			
		}
		return budget;
	}

	
}
