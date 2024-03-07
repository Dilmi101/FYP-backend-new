package com.fyp.ehb.model;

import lombok.Data;

@Data
public class AddRawMaterialRequest {
	
	private String rawMaterialName;
	private String unit;
	private int stockRemaining;
	private int lowStockLevel;
	private String availability;
	private String supplierName;
	private String supplierEmailAddress;
	private String reminder;

}
