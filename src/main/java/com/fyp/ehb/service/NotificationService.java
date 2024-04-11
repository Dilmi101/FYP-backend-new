package com.fyp.ehb.service;

import java.util.concurrent.Future;

import com.fyp.ehb.model.firebase.NotificationResponse;
import com.fyp.ehb.model.firebase.PushNotificationRequest;

public interface NotificationService {

	public Future<NotificationResponse> sendPushNotification(PushNotificationRequest pushNotificationRequest) throws Exception;
}
