package com.fyp.ehb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fyp.ehb.domain.ExpenseHistory;

public interface ExpenseHistoryDao extends MongoRepository<ExpenseHistory, String> {

	@Query(value ="{expense : ?0}")
	List<ExpenseHistory> getExpenseHistoryById(String expense);

}
