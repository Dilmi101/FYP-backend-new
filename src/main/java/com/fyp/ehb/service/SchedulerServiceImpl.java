package com.fyp.ehb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fyp.ehb.domain.Goal;
import com.fyp.ehb.model.firebase.PushNotificationDataRequest;
import com.fyp.ehb.model.firebase.PushNotificationRequest;
import com.fyp.ehb.repository.GoalDao;

@Service
public class SchedulerServiceImpl implements SchedulerService{
	
	private Log logger = LogFactory.getLog(SchedulerServiceImpl.class);
	
    @Autowired
    private MongoTemplate mongoTemplate;
	
	@Autowired
	private NotificationService notificationService;

	@Override
	@Scheduled(cron = "${goal.reminder.cron}")
	public void goalReminder() throws Exception {
		
		logger.info("starting goal remider scheduler");
		
		Query query = new Query();

		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = outputDateFormat.format(new Date());
		
		query.addCriteria(Criteria.where("nextExecutionDate").gte(currentDate).and("endDate").lte(currentDate));
		List<Goal> goals =  mongoTemplate.find(query, Goal.class);
		
		if(!goals.isEmpty()) {
			
			for(Goal goal : goals) {
				
				try {
					
					PushNotificationDataRequest notificationDataRequest = new PushNotificationDataRequest();
					notificationDataRequest.setTitle("Reminder!");
					notificationDataRequest.setBody("Just a quick reminder to update your goal - " + goal.getGoalTitle());
					
					PushNotificationRequest notificationRequest = new PushNotificationRequest();
					notificationRequest.setTo(goal.getCustomer().getPushToken());
					notificationRequest.setNotification(notificationDataRequest);
					
					notificationService.sendPushNotification(notificationRequest);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
			}
		}
	}

	@Override
	@Scheduled(cron = "${expense.reminder.cron}")
	public void expenseReminder() throws Exception {
		
		
	}

	@Override
	@Scheduled(cron = "${rowmaterials.reminder.cron}")
	public void rowmaterialsReminder() throws Exception {
		
		
	}

	@Override
	@Scheduled(cron = "${reminde.unfinished.goal.status.cron}")
	public void remindeUnfinishedGoalStatus() throws Exception {
		
		
	}

}
