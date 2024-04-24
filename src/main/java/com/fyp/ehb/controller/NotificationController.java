package com.fyp.ehb.controller;

import com.fyp.ehb.model.AddExpenseRequest;
import com.fyp.ehb.model.MainResponse;
import com.fyp.ehb.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private SchedulerService schedulerService;

    @PostMapping(value="/goalReminder")
    public MainResponse goalReminder() throws Exception {

        schedulerService.goalReminder();

        MainResponse mainResponse = new MainResponse();
        mainResponse.setResponseCode("000");
        mainResponse.setResponseObject("Sent Goal Reminder");

        return mainResponse;
    }

    @PostMapping(value="/remindeUnfinishedGoalStatus")
    public MainResponse remindeUnfinishedGoalStatus() throws Exception {

        schedulerService.remindeUnfinishedGoalStatus();

        MainResponse mainResponse = new MainResponse();
        mainResponse.setResponseCode("000");
        mainResponse.setResponseObject("Sent Reminder Unfinished Goal Status");

        return mainResponse;
    }

    @PostMapping(value="/rowmaterialsReminder")
    public MainResponse rowmaterialsReminder() throws Exception {

        schedulerService.rowmaterialsReminder();

        MainResponse mainResponse = new MainResponse();
        mainResponse.setResponseCode("000");
        mainResponse.setResponseObject("Sent Raw Material Reminder");

        return mainResponse;
    }

    @PostMapping(value="/expenseReminder")
    public MainResponse expenseReminder() throws Exception {

        schedulerService.expenseReminder();

        MainResponse mainResponse = new MainResponse();
        mainResponse.setResponseCode("000");
        mainResponse.setResponseObject("Sent Expense Reminder");

        return mainResponse;
    }
}
