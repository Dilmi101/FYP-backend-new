package com.fyp.ehb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fyp.ehb.domain.Budget;

public interface BudgetDao extends MongoRepository<Budget, String> {

	@Query(value ="{customer : ?0}")
	List<Budget> findByCustomerId(String customer);

	@Query(value ="{customer : ?0, month : ?1}")
	List<Budget> getBudgetListByCustomerAndMonth(String customer, String month);

}
