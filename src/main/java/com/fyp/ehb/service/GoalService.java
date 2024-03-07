package com.fyp.ehb.service;

import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import java.util.HashMap;

public interface GoalService {

    GoalResponse createGoal(String customerId, CreateGoalRequest goalRequest) throws Exception;

    GoalResponse updateGoal(String goalId, UpdateGoalRequest goalRequest) throws Exception;

    HashMap<String, String> deleteGoal(String goalId) throws Exception;
}
