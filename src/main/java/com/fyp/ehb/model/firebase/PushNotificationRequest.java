package com.fyp.ehb.model.firebase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushNotificationRequest {

	private String to;
	private PushNotificationDataRequest notification;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public PushNotificationDataRequest getNotification() {
		return notification;
	}
	public void setNotification(PushNotificationDataRequest notification) {
		this.notification = notification;
	}
	
	
}
