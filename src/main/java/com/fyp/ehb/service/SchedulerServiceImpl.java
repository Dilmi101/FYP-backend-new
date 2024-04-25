package com.fyp.ehb.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.fyp.ehb.domain.Expense;
import com.fyp.ehb.domain.RawMaterial;
import com.fyp.ehb.enums.ReminderFrequency;
import com.fyp.ehb.model.ExpenseResponse;
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

	@Autowired
	private GoalDao goalDao;

	@Override
	@Scheduled(cron = "${goal.reminder.cron}")
	public void goalReminder() throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar calendarFromDate = Calendar.getInstance();
		calendarFromDate.setTime(new Date());
		calendarFromDate.set(Calendar.HOUR, 00);
		calendarFromDate.set(Calendar.MINUTE, 00);
		calendarFromDate.set(Calendar.AM_PM, 0);
		calendarFromDate.set(Calendar.SECOND, 0);

		Date fromDate = calendarFromDate.getTime();
		String fromDateString = formatter.format(fromDate.getTime());
		logger.info("Goal Reminder From Date String >>>> " + fromDateString);

		Calendar calendarToDate = Calendar.getInstance();
		calendarToDate.setTime(new Date());

		calendarToDate.set(Calendar.HOUR, 00);
		calendarToDate.set(Calendar.MINUTE, 00);
		calendarToDate.set(Calendar.AM_PM, 0);
		calendarToDate.set(Calendar.SECOND, 0);

		Date toDate = calendarToDate.getTime();
		String toDateString = formatter.format(toDate.getTime());
		logger.info("Goal Reminder To Date String >>>> " + toDate);

		logger.info("starting goal reminder scheduler");

		Query query = new Query();
		Criteria c = new Criteria().andOperator(Criteria.where("nextExecutionDate").is(fromDateString), Criteria.where("status").is("A"));
		query.addCriteria(c);

		List<Goal> goals =  mongoTemplate.find(query, Goal.class);

		if(!goals.isEmpty()) {

			for(Goal goal : goals) {

				try {

					if(goal.getCustomer().getPushToken() != null && !goal.getCustomer().getPushToken().isEmpty()){

						PushNotificationDataRequest notificationDataRequest = new PushNotificationDataRequest();
						notificationDataRequest.setTitle("Reminder!");
						notificationDataRequest.setBody("Just a quick reminder to update your goal - " + goal.getGoalTitle());

						PushNotificationRequest notificationRequest = new PushNotificationRequest();
						notificationRequest.setTo(goal.getCustomer().getPushToken());
						notificationRequest.setNotification(notificationDataRequest);

						notificationService.sendPushNotification(notificationRequest);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

				updateNextUpcommingdays(goal, toDate);

			}
		}
	}


	@Override
	@Scheduled(cron = "${expense.reminder.cron}")
	public void expenseReminder() throws Exception {

		logger.info("starting expenses reminder scheduler....");

		Calendar calendarEndDate = Calendar.getInstance();
		calendarEndDate.setTime(new Date());

		calendarEndDate.set(Calendar.HOUR, 00);
		calendarEndDate.set(Calendar.MINUTE, 00);
		calendarEndDate.set(Calendar.AM_PM, 0);
		calendarEndDate.set(Calendar.SECOND, 0);

		Date endDate = calendarEndDate.getTime();

		Query query = new Query();
		Criteria c = new Criteria().andOperator(Criteria.where("endDate").lte(endDate));
		query.addCriteria(c);

		List<Expense> expenses =  mongoTemplate.find(query, Expense.class);

		if(expenses != null && !expenses.isEmpty()){

			for (Expense expense : expenses){

				try {

					if(expense.getCustomer().getPushToken() != null && !expense.getCustomer().getPushToken().isEmpty()){

						PushNotificationDataRequest notificationDataRequest = new PushNotificationDataRequest();
						notificationDataRequest.setTitle("Keep Going!");
						notificationDataRequest.setBody("Just a quick to reminder record your expenses - " + expense.getExpenseTitle());

						PushNotificationRequest notificationRequest = new PushNotificationRequest();
						notificationRequest.setTo(expense.getCustomer().getPushToken());
						notificationRequest.setNotification(notificationDataRequest);

						notificationService.sendPushNotification(notificationRequest);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	@Scheduled(cron = "${rowmaterials.reminder.cron}")
	public void rowmaterialsReminder() throws Exception {

		Query query = new Query();
		Criteria c = new Criteria().where("status").is("A");
		query.addCriteria(c);

		List<RawMaterial> rawMaterials =  mongoTemplate.find(query, RawMaterial.class);

		if(rawMaterials != null && !rawMaterials.isEmpty()){

			for (RawMaterial rawMaterial : rawMaterials){

				try {

					if(rawMaterial.getCustomer().getPushToken() != null && !rawMaterial.getCustomer().getPushToken().isEmpty()){

						PushNotificationDataRequest notificationDataRequest = new PushNotificationDataRequest();
						notificationDataRequest.setTitle("Keep Going!");
						notificationDataRequest.setBody("Just a quick reminder to update your - " + rawMaterial.getName());

						PushNotificationRequest notificationRequest = new PushNotificationRequest();
						notificationRequest.setTo(rawMaterial.getCustomer().getPushToken());
						notificationRequest.setNotification(notificationDataRequest);

						notificationService.sendPushNotification(notificationRequest);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	@Scheduled(cron = "${reminde.unfinished.goal.status.cron}")
	public void remindeUnfinishedGoalStatus() throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar calendarEndDate = Calendar.getInstance();
		calendarEndDate.setTime(new Date());

		calendarEndDate.set(Calendar.HOUR, 00);
		calendarEndDate.set(Calendar.MINUTE, 00);
		calendarEndDate.set(Calendar.AM_PM, 0);
		calendarEndDate.set(Calendar.SECOND, 0);

		String toDateString = formatter.format(calendarEndDate.getTime());

		Query query = new Query();
		Criteria c = new Criteria().andOperator(Criteria.where("endDate").lte(toDateString), Criteria.where("status").is("A"));
		query.addCriteria(c);

		List<Goal> goals =  mongoTemplate.find(query, Goal.class);

		if(goals != null && !goals.isEmpty()){

				for(Goal goal : goals){

				try {

					if(goal.getCustomer().getPushToken() != null && !goal.getCustomer().getPushToken().isEmpty()){

						PushNotificationDataRequest notificationDataRequest = new PushNotificationDataRequest();
						notificationDataRequest.setTitle("Keep Going!");
						notificationDataRequest.setBody("You have failed to achieve your goal - " + goal.getGoalTitle());

						PushNotificationRequest notificationRequest = new PushNotificationRequest();
						notificationRequest.setTo(goal.getCustomer().getPushToken());
						notificationRequest.setNotification(notificationDataRequest);

						notificationService.sendPushNotification(notificationRequest);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		
		
	}

	private void updateNextUpcommingdays(Goal goal, Date toDate) throws Exception {

		String frequecy = goal.getReminder();
		String endDateString = goal.getEndDate();
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endDate = formatter.parse(endDateString);

		if (endDate.after(toDate)) {

			if (ReminderFrequency.DAILY.getFrequencyType().equalsIgnoreCase(frequecy)) {

				Date nextExecutionDate = formatter.parse(goal.getNextExecutionDate());
				calendar.setTime(nextExecutionDate);
				calendar.add(Calendar.DATE, 1);

				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String newNextExecutionDate = outputDateFormat.format(calendar.getTime());
				goal.setNextExecutionDate(newNextExecutionDate);

			} else if (ReminderFrequency.WEEKLY.getFrequencyType().equalsIgnoreCase(frequecy)) {

				Date nextExecutionDate = formatter.parse(goal.getNextExecutionDate());
				calendar.setTime(nextExecutionDate);
				calendar.add(Calendar.WEEK_OF_YEAR, 1);

				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String newNextExecutionDate = outputDateFormat.format(calendar.getTime());
				goal.setNextExecutionDate(newNextExecutionDate);

			} else if (ReminderFrequency.MONTHLY.getFrequencyType().equalsIgnoreCase(frequecy)) {

				Date nextExecutionDate = formatter.parse(goal.getNextExecutionDate());
				calendar.setTime(nextExecutionDate);
				calendar.add(Calendar.MONTH, 1);

				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String newNextExecutionDate = outputDateFormat.format(calendar.getTime());
				goal.setNextExecutionDate(newNextExecutionDate);

			} else if (ReminderFrequency.YEARLY.getFrequencyType().equalsIgnoreCase(frequecy)) {

				Date nextExecutionDate = formatter.parse(goal.getNextExecutionDate());
				calendar.setTime(nextExecutionDate);
				calendar.add(Calendar.YEAR, 1);

				SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String newNextExecutionDate = outputDateFormat.format(calendar.getTime());
				goal.setNextExecutionDate(newNextExecutionDate);
			}

			goalDao.save(goal);
		}
	}

	@Override
	public List<ExpenseResponse> expenseReminderListGet() throws Exception {

		logger.info("starting expenses reminder scheduler....");

		Calendar calendarEndDate = Calendar.getInstance();
		calendarEndDate.setTime(new Date());

		calendarEndDate.set(Calendar.HOUR, 00);
		calendarEndDate.set(Calendar.MINUTE, 00);
		calendarEndDate.set(Calendar.AM_PM, 0);
		calendarEndDate.set(Calendar.SECOND, 0);

		Date endDate = calendarEndDate.getTime();

		Query query = new Query();
		Criteria c = new Criteria().andOperator(Criteria.where("endDate").lte(endDate));
		query.addCriteria(c);

		List<Expense> expenses = mongoTemplate.find(query, Expense.class);

		List<ExpenseResponse> response = new ArrayList<>();

		if(expenses != null && !expenses.isEmpty()){

			for(Expense expense : expenses){

				ExpenseResponse expenseResponse = new ExpenseResponse();
				expenseResponse.setId(expense.getId());
				expenseResponse.setExpenseTitle(expense.getExpenseTitle());

				response.add(expenseResponse);
			}
		}
		else{
			logger.info("No expenses found for reminder");
		}

		return response;
	}

}
