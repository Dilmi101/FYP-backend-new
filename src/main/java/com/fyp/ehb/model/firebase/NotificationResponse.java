package com.fyp.ehb.model.firebase;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResponse {

    private ResponseEntity<Object> responseEntity;

	public ResponseEntity<Object> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<Object> responseEntity) {
		this.responseEntity = responseEntity;
	}
}
