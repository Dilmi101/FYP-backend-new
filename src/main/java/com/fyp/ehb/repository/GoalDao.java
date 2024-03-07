package com.fyp.ehb.repository;

import com.fyp.ehb.domain.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoalDao extends MongoRepository<Goal, String> {
}
