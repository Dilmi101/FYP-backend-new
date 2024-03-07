package com.fyp.ehb.service;

import com.fyp.ehb.domain.Customer;
import com.fyp.ehb.domain.Goal;
import com.fyp.ehb.enums.EmpowerHerBizError;
import com.fyp.ehb.enums.Status;
import com.fyp.ehb.exception.EmpowerHerBizException;
import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import com.fyp.ehb.repository.CustomerDao;
import com.fyp.ehb.repository.GoalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalDao goalDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    public GoalResponse createGoal(String customerId, CreateGoalRequest goalRequest) throws Exception {

        Goal goal = new Goal();

        goal.setGoalTitle(goalRequest.getGoalTitle());
        goal.setGoalDescription(goalRequest.getGoalDescription());
        goal.setStartDate(LocalDateTime.parse(goalRequest.getStartDate()));
        goal.setEndDate(LocalDateTime.parse(goalRequest.getEndDate()));
        goal.setUnit(goalRequest.getUnit());
        goal.setTarget(String.valueOf(goalRequest.getTarget()));
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
            goalResponse.setTarget(String.valueOf(goalNew.getTarget()));
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
            goalCurrent.setTarget(String.valueOf(goalRequest.getTarget()));
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
                goalResponse.setTarget(String.valueOf(updatedGoal.getTarget()));
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

}
