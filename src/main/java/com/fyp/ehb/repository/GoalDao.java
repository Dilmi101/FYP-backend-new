package com.fyp.ehb.repository;

import com.fyp.ehb.domain.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface GoalDao extends MongoRepository<Goal, String> {
    Collection<? extends Goal> getGoalsByCustomerId(String customerId);
}
