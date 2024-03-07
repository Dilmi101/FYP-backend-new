package com.fyp.ehb.controller;

import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import com.fyp.ehb.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.MainResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/api/goal")
public class GoalController {

	@Autowired
	private GoalService goalService;

	//Create New Goal
	@PostMapping(value="/create")
	public MainResponse createGoal(
			@RequestParam(value = "customerId") String customerId,
			@RequestBody CreateGoalRequest goalRequest
			) throws Exception {

		GoalResponse goalResponse = goalService.createGoal(customerId, goalRequest);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goalResponse);

		return mainResponse;
	}

	@PutMapping("/update")
	public MainResponse updateGoal(
			@RequestParam(value = "goalId") String goalId,
			@RequestBody UpdateGoalRequest goalRequest
	) throws Exception {

		GoalResponse goalResponse = goalService.updateGoal(goalId, goalRequest);

		MainResponse response = new MainResponse();
		response.setResponseCode("000");
		response.setResponseObject(goalResponse);

		return response;
	}

	@PutMapping("/delete")
	public MainResponse deleteGoal(
			@RequestParam(value = "goalId") String goalId
	) throws Exception {

		HashMap<String, String> deleteResponse = goalService.deleteGoal(goalId);

		MainResponse response = new MainResponse();
		response.setResponseCode("000");
		response.setResponseObject(deleteResponse);

		return response;
	}
}
