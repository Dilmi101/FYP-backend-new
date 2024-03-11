package com.fyp.ehb.controller;

import com.fyp.ehb.model.GoalResponse;
import com.fyp.ehb.model.UpdateGoalRequest;
import com.fyp.ehb.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fyp.ehb.model.CreateGoalRequest;
import com.fyp.ehb.model.MainResponse;
import java.util.HashMap;
import java.util.List;

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

	@PutMapping("/update/{id}")
	public MainResponse updateGoal(
			@PathVariable(value = "id") String goalId,
			@RequestBody UpdateGoalRequest goalRequest
	) throws Exception {

		GoalResponse goalResponse = goalService.updateGoal(goalId, goalRequest);

		MainResponse response = new MainResponse();
		response.setResponseCode("000");
		response.setResponseObject(goalResponse);

		return response;
	}

	@PutMapping("/delete/{id}")
	public MainResponse deleteGoal(
			@PathVariable(value = "id") String goalId
	) throws Exception {

		HashMap<String, String> deleteResponse = goalService.deleteGoal(goalId);

		MainResponse response = new MainResponse();
		response.setResponseCode("000");
		response.setResponseObject(deleteResponse);

		return response;
	}

	@GetMapping(value="/{customerId}/goalList")
	public MainResponse getGoals(@PathVariable("customerId") String customerId) throws Exception {

		List<GoalResponse> goals = goalService.getGoals(customerId);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goals);

		return mainResponse;
	}

	@GetMapping(value="/{id}")
	public MainResponse getGoalById(
			@PathVariable("id") String id
	) throws Exception {

		GoalResponse goal = goalService.getGoalById(id);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goal);

		return mainResponse;
	}

	@PutMapping(value="/{id}/updateProgress")
	public MainResponse updateGoalProgressById(
			@PathVariable("id") String id,
			@RequestParam(name = "amount") String amount
	) throws Exception {

		HashMap<String, String> response = goalService.updateGoalProgress(id, amount);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(response);

		return mainResponse;
	}

	@GetMapping(value="/search")
	public MainResponse findGoals(
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "priority", required = false) String priority,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate
	) throws Exception {

		List<GoalResponse> goals = goalService.findGoals(status, priority, fromDate, toDate);

		MainResponse mainResponse = new MainResponse();
		mainResponse.setResponseCode("000");
		mainResponse.setResponseObject(goals);

		return mainResponse;
	}

}
