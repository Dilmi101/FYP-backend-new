package com.fyp.ehb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawMaterialResponse {

	private String name;
	private String unit;
	private String remainingStock;
	private String lowStockLvl;
	private String reminder;
	private String availability;
	private String supplierName;
	private String supplierEmail;
	private String status;
	private String customerId;
}
