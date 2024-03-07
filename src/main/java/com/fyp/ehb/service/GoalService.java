package com.fyp.ehb.service;

import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import java.util.HashMap;
import java.util.List;

public interface GoalService {

    GoalResponse createGoal(String customerId, CreateGoalRequest goalRequest) throws Exception;

    GoalResponse updateGoal(String goalId, UpdateGoalRequest goalRequest) throws Exception;

    HashMap<String, String> deleteGoal(String goalId) throws Exception;

    List<GoalResponse> getGoals() throws Exception;

    GoalResponse getGoalById(String id) throws Exception;

    HashMap<String, String> updateGoalProgress(String id, String amount) throws Exception;

    List<GoalResponse> findGoals(String status, String priority, String fromDate, String toDate) throws  Exception;
}
