package com.fyp.ehb.repository;

import com.fyp.ehb.domain.GoalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GoalHistoryDao extends MongoRepository<GoalHistory, String> {

    @Query("{goal : ?0}")
    List<GoalHistory> getGoalHistoriesByGoalId(String goal);
}
