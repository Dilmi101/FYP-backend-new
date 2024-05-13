package com.fyp.ehb.service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fyp.ehb.model.firebase.NotificationResponse;
import com.fyp.ehb.model.firebase.PushNotificationRequest;

@Service
public class NotificationServiceImpl implements NotificationService{
	
	@Autowired
	private Environment env;
	
	private Log logger = LogFactory.getLog(NotificationServiceImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String emailUsername;

	@Override
	@Async
	public Future<NotificationResponse> sendPushNotification(PushNotificationRequest pushNotificationRequest)
			throws Exception {
		
		try {
			
			String uri = env.getProperty("fcm.url");
			String apiKey = env.getProperty("fcm.auth.key");
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Authorization", "key=" + apiKey);
			
			RestTemplate restTempleate = new RestTemplate();
		
			HttpEntity<PushNotificationRequest> request = new HttpEntity<>(pushNotificationRequest, httpHeaders);
			
			ResponseEntity<Object> responseEntity = restTempleate.exchange(uri, HttpMethod.POST, request, Object.class);
			
			if(responseEntity != null && responseEntity.hasBody()) {
				logger.info("Push notification response : " + responseEntity);
			}
			
			NotificationResponse pushNotificationResponse=new NotificationResponse();
            pushNotificationResponse.setResponseEntity(responseEntity);

			return new AsyncResult<NotificationResponse>(pushNotificationResponse);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void sendSimpleMail(String recipient, String supplierName, String RawMaterialName, String businessName) throws Exception {

		try {

			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("supplierName", supplierName);
			data.put("RawMaterialName", RawMaterialName);
			data.put("businessName", businessName);

			String messageBody = "Dear " + supplierName + "\n\n" +
					"The inventory of " + RawMaterialName + " at " +  businessName + " is running low. The business owner will contact you shortly to place an order for replenishment. or you can proactively contact the business owner and get an order confirmation.\n\n" +
					"Thank you.\n\n";

			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

			simpleMailMessage.setFrom(emailUsername);
			simpleMailMessage.setTo(recipient);
			simpleMailMessage.setSubject("Urgent: Low Stock Level Notification for " + businessName);
			simpleMailMessage.setText(messageBody);

			javaMailSender.send(simpleMailMessage);

			logger.info("Email sent successfully to " + recipient + " at " + new Date());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Email sent failed to " + recipient + " at " + new Date());
		}

	}

}
