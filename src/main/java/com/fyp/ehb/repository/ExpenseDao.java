package com.fyp.ehb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fyp.ehb.domain.Expense;

@Repository
public interface ExpenseDao extends MongoRepository<Expense, String> {

}
