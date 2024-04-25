package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Dashboard;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
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

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = inputDateFormat.parse(goalRequest.getStartDate());
            eDate = inputDateFormat.parse(goalRequest.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = outputDateFormat.format(sDate);
        String endDate = outputDateFormat.format(eDate);

        goal.setGoalTitle(goalRequest.getGoalTitle());
        goal.setGoalDescription(goalRequest.getGoalDescription());
        goal.setStartDate(startDate);
        goal.setEndDate(endDate);
        goal.setUnit(goalRequest.getUnit());
        goal.setTarget(goalRequest.getTarget());
        goal.setPriority(goal.getPriority());
        goal.setReminder(goalRequest.getReminder());
        goal.setPriority(goalRequest.getPriority());
        goal.setReminder(goalRequest.getReminder());
        goal.setGoalStatus(Status.ACTIVE_STATUS.getStatus());
        goal.setIsRecurringGoal(Status.NO_STATUS.getStatus());
        goal.setNextExecutionDate(startDate);

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
            goalResponse.setStartDate(goalNew.getStartDate());
            goalResponse.setEndDate(goalNew.getEndDate());
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

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = null;
        Date eDate = null;

        try {
            sDate = inputDateFormat.parse(goalRequest.getStartDate());
            eDate = inputDateFormat.parse(goalRequest.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = outputDateFormat.format(sDate);
        String endDate = outputDateFormat.format(eDate);

        if(goal.isPresent()){

            Goal goalCurrent = goal.get();

             goalCurrent.setId(goalId);
            goalCurrent.setGoalTitle(goalRequest.getGoalTitle());
            goalCurrent.setGoalDescription(goalRequest.getGoalDescription());
            goalCurrent.setStartDate(startDate);
            goalCurrent.setEndDate(endDate);
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
                goalResponse.setStartDate(updatedGoal.getStartDate());
                goalResponse.setEndDate(updatedGoal.getEndDate());
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
            goalCurrent.setGoalStatus(Status.INACTIVE_STATUS.getStatus());

            Goal updatedGoal = goalDao.save(goalCurrent);

            HashMap<String, String> hm = new HashMap<>();

            if(updatedGoal != null){
                hm.put("message", "Success");
            }
            else{
                hm.put("message", "Failed");
            }

            return hm;

        } else {
            throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_NOT_FOUND);
        }
    }

    @Override
    public List<GoalResponse> getGoals(String customerId) throws Exception {

        List<Goal> goals = new ArrayList<>();

        goals.addAll(goalDao.getGoalsByCustomerId(customerId));

        if(!goals.isEmpty()){

            List<GoalResponse> goalResponses = new ArrayList<>();

            for(Goal goal : goals){
            	
            	if(!goal.getGoalStatus().equalsIgnoreCase("A") && !goal.getGoalStatus().equalsIgnoreCase("C")) {
            		continue;
            	}
            	
                Query query2 = new Query();
                query2.addCriteria(Criteria.where("goalId").is(goal.getId()));
                query2.addCriteria(Criteria.where("status").is("A"));
                Dashboard isDashboardItem =  mongoTemplate.findOne(query2, Dashboard.class);
                
                GoalResponse goalResponse = new GoalResponse();

                goalResponse.setId(goal.getId());
                goalResponse.setGoalTitle(goal.getGoalTitle());
                goalResponse.setGoalDescription(goal.getGoalDescription());
                goalResponse.setStartDate(goal.getStartDate());
                goalResponse.setEndDate(goal.getEndDate());
                goalResponse.setUnit(goal.getUnit());
                goalResponse.setTarget(goal.getTarget());
                goalResponse.setPriority(goal.getPriority());
                goalResponse.setReminder(goal.getReminder());
                goalResponse.setGoalStatus(goal.getGoalStatus());
                goalResponse.setIsRecurringGoal(goal.getIsRecurringGoal());
                
            	if(isDashboardItem != null && isDashboardItem.getStatus().equalsIgnoreCase("A")) {
            		goalResponse.setIsDashboardItem("Y");
            	} else {
            		goalResponse.setIsDashboardItem("N");
            	}

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                long days = 0;

                try {
                    Date goalEndDate = format.parse(goal.getEndDate());

                    Instant currentDate = Instant.now();
                    Instant endDate = goalEndDate.toInstant();

                    Duration duration = Duration.between(currentDate, endDate);
                    days = duration.toDays();

                    if (days < 0) {
                        continue;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
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
                    double remaining = target - sum;
                    
                    if(remaining < 0) {
                    	goalResponse.setPendingTarget(String.valueOf(0));
                    } else {
                    	goalResponse.setPendingTarget(String.valueOf(remaining));
                    }

                    goalResponse.setProgressPercentage(String.valueOf(Math.round(percentage)));
                }
                else{
                    goalResponse.setProgressPercentage("0");
                    goalResponse.setPendingTarget(goal.getTarget());
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
            goalResponse.setStartDate(goal.getStartDate());
            goalResponse.setEndDate(goal.getEndDate());
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
                double percentage = (sum / target) * 100;
                
                if(remaining < 0) {
                	goalResponse.setPendingTarget(String.valueOf(0));
                } else {
                	goalResponse.setPendingTarget(String.valueOf(remaining));
                }

                goalResponse.setProgressPercentage(String.valueOf(Math.round(percentage)));
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date currentDate = new Date();
                String dateStr = "";

                try {
                    dateStr = sdf.format(currentDate);
                }
                catch (Exception e){
                   e.printStackTrace();
                }

                record.setCreatedDate(dateStr);

                GoalHistory history = goalHistoryDao.save(record);

                HashMap<String, String> hm = new HashMap<>();

                if(history != null){

                    List<GoalHistory> records = goalHistoryDao.getGoalHistoriesByGoalId(id);

                    double sum = 0.00;

                    if(!records.isEmpty()) {
                        for (GoalHistory goalHistory : records) {
                            double achieved = Double.parseDouble(goalHistory.getAchievedAmount());
                            sum += achieved;
                        }
                    }

                    double target = Double.parseDouble(goal.get().getTarget());
                    double percentage = (sum / target) * 100;
                    
                    if(percentage >= 100) {
                    	throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_TARGET_REACHED);
                    }

                    if(sum >= target){
                        hm.put("isAchieved", Status.YES_STATUS.getStatus());

                        goal.get().setGoalStatus(Status.COMPLETED_STATUS.getStatus());
                        goalDao.save(goal.get());
                    }
                    else{
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        long days;

                        try {
                            Date goalEndDate = format.parse(goal.get().getEndDate());

                            Instant thisDate = Instant.now();
                            Instant endDate = goalEndDate.toInstant();

                            Duration duration = Duration.between(thisDate, endDate);
                            days = duration.toDays();

                            if (days <= 0) {
                                hm.put("isAchieved", Status.NO_STATUS.getStatus());
                            }
                            else{
                                hm.put("isAchieved", Status.PENDING_STATUS.getStatus());
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    hm.put("title", goal.get().getGoalTitle());
                }
                else{
                    throw new EmpowerHerBizException(EmpowerHerBizError.GOAL_PROGRESS_UPDATE_FAILED);
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
    public List<GoalResponse> findGoals(String customerId, String status, String priority, String fromDate, String toDate) throws Exception {

        Query query = new Query();

        query.addCriteria(Criteria.where("customer.id").is(customerId));

        if(status != null && !status.isEmpty()) {
            query.addCriteria(Criteria.where("goalStatus").is(status));
        }
        if(priority != null && !priority.isEmpty()) {
            query.addCriteria(Criteria.where("priority").is(priority));
        }
        if(fromDate != null && !fromDate.isEmpty()) {
            query.addCriteria(Criteria.where("startDate").gte(fromDate));
        }
        if(toDate != null && !toDate.isEmpty()) {
            query.addCriteria(Criteria.where("endDate").lte(toDate));
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

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                long days = 0;

                try {
                    Date goalEndDate = format.parse(goal.getEndDate());

                    Instant currentDate = Instant.now();
                    Instant endDate = goalEndDate.toInstant();

                    Duration duration = Duration.between(currentDate, endDate);
                    days = duration.toDays();

                    if (days < 0) {
                        continue;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
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
