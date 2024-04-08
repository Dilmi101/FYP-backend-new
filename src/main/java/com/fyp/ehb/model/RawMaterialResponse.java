package com.fyp.ehb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawMaterialResponse {

	private String name;
	private String remainingStock;
	private String availability;
	private String rawMateId;
	private List<RawMaterialHistoryMain> rawHistoryList;
}
