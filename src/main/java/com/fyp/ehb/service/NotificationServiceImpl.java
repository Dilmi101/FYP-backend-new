package com.fyp.ehb.service;

import java.util.Date;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

}
