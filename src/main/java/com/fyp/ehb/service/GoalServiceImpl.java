package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Goal;
import com.fyp.ehb.domain.GoalHistory;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.enums.Status;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.GoalDao;
import com.fyp.ehb.repository.GoalHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoalDao goalDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private GoalHistoryDao goalHistoryDao;

    @Override
    public GoalResponse createGoal(String customerId, CreateGoalRequest goalRequest) throws Exception {

        Goal goal = new Goal();

        goal.setGoalTitle(goalRequest.getGoalTitle());
        goal.setGoalDescription(goalRequest.getGoalDescription());
        goal.setStartDate(LocalDateTime.parse(goalRequest.getStartDate()));
        goal.setEndDate(LocalDateTime.parse(goalRequest.getEndDate()));
        goal.setUnit(goalRequest.getUnit());
        goal.setTarget(goalRequest.getTarget());
        goal.setPriority(goal.getPriority());
        goal.setReminder(goalRequest.getReminder());
        goal.setPriority(goalRequest.getPriority());
        goal.setReminder(goalRequest.getReminder());
        goal.setGoalStatus(Status.ACTIVE_STATUS.getStatus());
        goal.setIsRecurringGoal(Status.NO_STATUS.getStatus());

        Optional<Customer> customer = customerDao.findById(customerId);

        if(customer.isPresent()) {
            Customer _customer = customer.get();
            goal.setCustomer(_customer);
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.CUSTOMER_NOT_FOUND);
        }

        Goal goalNew = goalDao.save(goal);

        if(goalNew != null) {

            GoalResponse goalResponse = new GoalResponse();

            goalResponse.setId(goalNew.getId());
            goalResponse.setGoalTitle(goalNew.getGoalTitle());
            goalResponse.setGoalDescription(goalNew.getGoalDescription());
            goalResponse.setStartDate(String.valueOf(goalNew.getStartDate()));
            goalResponse.setEndDate(String.valueOf(goalNew.getEndDate()));
            goalResponse.setUnit(goalNew.getUnit());
            goalResponse.setTarget(goalNew.getTarget());
            goalResponse.setPriority(goalNew.getPriority());
            goalResponse.setReminder(goalNew.getReminder());
            goalResponse.setGoalStatus(goalNew.getGoalStatus());
            goalResponse.setIsRecurringGoal(goalNew.getIsRecurringGoal());
            goalResponse.setCustomer(goalNew.getCustomer());

            return goalResponse;
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_CREATED);
        }
    }

    @Override
    public GoalResponse updateGoal(String goalId, UpdateGoalRequest goalRequest) throws Exception {

        Optional<Goal> goal = goalDao.findById(goalId);

        if(goal.isPresent()){

            Goal goalCurrent = goal.get();

            goalCurrent.setId(goalId);
            goalCurrent.setGoalTitle(goalRequest.getGoalTitle());
            goalCurrent.setGoalDescription(goalRequest.getGoalDescription());
            goalCurrent.setStartDate(LocalDateTime.parse(goalRequest.getStartDate()));
            goalCurrent.setEndDate(LocalDateTime.parse(goalRequest.getEndDate()));
            goalCurrent.setUnit(goalRequest.getUnit());
            goalCurrent.setTarget(goalRequest.getTarget());
            goalCurrent.setPriority(goalRequest.getPriority());
            goalCurrent.setReminder(goalRequest.getReminder());
            goalCurrent.setGoalStatus(Status.ACTIVE_STATUS.getStatus());
            goalCurrent.setIsRecurringGoal(Status.NO_STATUS.getStatus());

            Goal updatedGoal = goalDao.save(goalCurrent);

            if(updatedGoal != null){

                GoalResponse goalResponse = new GoalResponse();

                goalResponse.setId(updatedGoal.getId());
                goalResponse.setGoalTitle(updatedGoal.getGoalTitle());
                goalResponse.setGoalDescription(updatedGoal.getGoalDescription());
                goalResponse.setStartDate(String.valueOf(updatedGoal.getStartDate()));
                goalResponse.setEndDate(String.valueOf(updatedGoal.getEndDate()));
                goalResponse.setUnit(updatedGoal.getUnit());
                goalResponse.setTarget(updatedGoal.getTarget());
                goalResponse.setPriority(updatedGoal.getPriority());
                goalResponse.setReminder(updatedGoal.getReminder());
                goalResponse.setGoalStatus(updatedGoal.getGoalStatus());
                goalResponse.setIsRecurringGoal(updatedGoal.getIsRecurringGoal());
                goalResponse.setCustomer(updatedGoal.getCustomer());

                return goalResponse;
            }
            else{
                throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_UPDATED);
            }
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_FOUND);
        }
    }

    @Override
    public HashMap<String, String> deleteGoal(String goalId) throws Exception {

        Optional<Goal> goal = goalDao.findById(goalId);

        if (goal.isPresent()) {

            Goal goalCurrent = goal.get();

            goalCurrent.setId(goalId);
            goalCurrent.setGoalStatus(Status.INACTIVE_STATUS.getStatus());

            Goal updatedGoal = goalDao.save(goalCurrent);

            HashMap<String, String> hm = new HashMap<>();

            if(updatedGoal != null){
                hm.put("Message", "Success");
            }
            else{
                hm.put("Message", "Failed");
            }

            return hm;

        } else {
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_FOUND);
        }
    }

    @Override
    public List<GoalResponse> getGoals(String customerId) throws Exception {

        List<Goal> goals = new ArrayList<>();

        goals.addAll(goalDao.findAll());

        if(!goals.isEmpty()){

            List<GoalResponse> goalResponses = new ArrayList<>();

            for(Goal goal : goals){
                GoalResponse goalResponse = new GoalResponse();

                goalResponse.setId(goal.getId());
                goalResponse.setGoalTitle(goal.getGoalTitle());
                goalResponse.setGoalDescription(goal.getGoalDescription());
                goalResponse.setStartDate(String.valueOf(goal.getStartDate()));
                goalResponse.setEndDate(String.valueOf(goal.getEndDate()));
                goalResponse.setUnit(goal.getUnit());
                goalResponse.setTarget(goal.getTarget());
                goalResponse.setPriority(goal.getPriority());
                goalResponse.setReminder(goal.getReminder());
                goalResponse.setGoalStatus(goal.getGoalStatus());
                goalResponse.setIsRecurringGoal(goal.getIsRecurringGoal());

                LocalDateTime current = LocalDateTime.now();

                Duration duration = Duration.between(current, goal.getEndDate());

                long days = duration.toDays();

                if(days < 0){
                    continue;
                }
                goalResponse.setRemainingDays(String.valueOf(days));

                List<GoalHistory> records = goalHistoryDao.getGoalHistoriesByGoalId(goal.getId());

                double sum = 0.00;

                if(!records.isEmpty()){
                    for(GoalHistory record : records){
                        double achieved = Double.parseDouble(record.getAchievedAmount());
                        sum += achieved;
                    }

                    double target = Double.parseDouble(goal.getTarget());

                    double percentage = (sum / target) * 100;

                    goalResponse.setProgressPercentage(String.valueOf(Math.round(percentage)));
                }
                else{
                    goalResponse.setProgressPercentage("0");
                }

                goalResponses.add(goalResponse);
            }

            if(goalResponses.isEmpty()){
                throw new EmpowerHerBizException(EmpowerHerBizError.GOALS_NOT_FOUND);
            }

            return goalResponses.stream()
                    .sorted(Comparator.comparing(GoalResponse :: getStartDate).reversed())
                    .collect(Collectors.toList());
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOALS_NOT_FOUND);
        }
    }

    @Override
    public GoalResponse getGoalById(String id) throws Exception {

        Optional<Goal> goalById = goalDao.findById(id);

        if(goalById.isPresent()){
            GoalResponse goalResponse = new GoalResponse();

            Goal goal = goalById.get();

            goalResponse.setId(goal.getId());
            goalResponse.setGoalTitle(goal.getGoalTitle());
            goalResponse.setGoalDescription(goal.getGoalDescription());
            goalResponse.setStartDate(String.valueOf(goal.getStartDate()));
            goalResponse.setEndDate(String.valueOf(goal.getEndDate()));
            goalResponse.setUnit(goal.getUnit());
            goalResponse.setTarget(String.valueOf(goal.getTarget()));
            goalResponse.setPriority(goal.getPriority());
            goalResponse.setReminder(goal.getReminder());
            goalResponse.setGoalStatus(goal.getGoalStatus());
            goalResponse.setIsRecurringGoal(goal.getIsRecurringGoal());

            List<GoalHistory> records = goalHistoryDao.getGoalHistoriesByGoalId(goal.getId());

            double sum = 0.00;

            if(!records.isEmpty()){
                for(GoalHistory record : records){
                    double achieved = Double.parseDouble(record.getAchievedAmount());
                    sum += achieved;
                }

                double target = Double.parseDouble(goal.getTarget());
                double remaining = target - sum;

                goalResponse.setPendingTarget(String.valueOf(remaining));
                goalResponse.setGoalRecords(records);
            }
            else{
                goalResponse.setPendingTarget(goal.getTarget());
            }

            return goalResponse;
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_FOUND);
        }
    }

    @Override
    public HashMap<String, String> updateGoalProgress(String id, String amount) throws Exception {

        Optional<Goal> goal = goalDao.findById(id);

        if(goal.isPresent()){
            if(Double.parseDouble(amount) > 0){
                GoalHistory record = new GoalHistory();

                record.setGoal(goal.get());
                record.setAchievedAmount(amount);
                record.setCreatedDate(LocalDateTime.now());

                GoalHistory history = goalHistoryDao.save(record);

                HashMap<String, String> hm = new HashMap<>();

                if(history != null){
                    hm.put("Message", "Success");
                }
                else{
                    hm.put("Message", "Failed");
                }
                return hm;
            }
            else{
                throw new EmpowerHerBizException(EmpowerHerBizError.INVALID_AMOUNT);
            }
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_FOUND);
        }
    }

    @Override
    public List<GoalResponse> findGoals(String status, String priority, String fromDate, String toDate) throws Exception {

        Query query = new Query();

        if(status != null && !status.isEmpty()) {
            query.addCriteria(Criteria.where("goalStatus").is(status));
        }
        if(priority != null && !priority.isEmpty()) {
            query.addCriteria(Criteria.where("priority").is(priority));
        }
        if(fromDate != null && !fromDate.isEmpty()) {
            query.addCriteria(Criteria.where("startDate").gte(LocalDateTime.parse(fromDate)));
        }
        if(toDate != null && !toDate.isEmpty()) {
            query.addCriteria(Criteria.where("endDate").lte(LocalDateTime.parse(toDate)));
        }

        List<Goal> goals =  mongoTemplate.find(query, Goal.class);

        if(!goals.isEmpty()){

            List<GoalResponse> goalResponses = new ArrayList<>();

            for(Goal goal : goals){
                GoalResponse goalResponse = new GoalResponse();

                goalResponse.setId(goal.getId());
                goalResponse.setGoalTitle(goal.getGoalTitle());
                goalResponse.setGoalDescription(goal.getGoalDescription());
                goalResponse.setStartDate(String.valueOf(goal.getStartDate()));
                goalResponse.setEndDate(String.valueOf(goal.getEndDate()));
                goalResponse.setUnit(goal.getUnit());
                goalResponse.setTarget(goal.getTarget());
                goalResponse.setPriority(goal.getPriority());
                goalResponse.setReminder(goal.getReminder());
                goalResponse.setGoalStatus(goal.getGoalStatus());
                goalResponse.setIsRecurringGoal(goal.getIsRecurringGoal());

                LocalDateTime current = LocalDateTime.now();

                Duration duration = Duration.between(current, goal.getEndDate());

                long days = duration.toDays();

                if(days < 0){
                    continue;
                }
                goalResponse.setRemainingDays(String.valueOf(days));

                List<GoalHistory> records = goalHistoryDao.getGoalHistoriesByGoalId(goal.getId());

                double sum = 0.00;

                if(!records.isEmpty()){
                    for(GoalHistory record : records){
                        double achieved = Double.parseDouble(record.getAchievedAmount());
                        sum += achieved;
                    }

                    double target = Double.parseDouble(goal.getTarget());

                    double percentage = (sum / target) * 100;

                    goalResponse.setProgressPercentage(String.valueOf(Math.round(percentage)));
                }
                else{
                    goalResponse.setProgressPercentage("0");
                }

                goalResponses.add(goalResponse);
            }

            if(goalResponses.isEmpty()){
                throw new EmpowerHerBizException(EmpowerHerBizError.GOALS_NOT_FOUND);
            }

            return goalResponses.stream()
                    .sorted(Comparator.comparing(GoalResponse :: getStartDate).reversed())
                    .collect(Collectors.toList());
        }
        else{
            throw new EmpowerHerBizException(EmpowerHerBizError.GOALS_NOT_FOUND);
        }
    }

}
