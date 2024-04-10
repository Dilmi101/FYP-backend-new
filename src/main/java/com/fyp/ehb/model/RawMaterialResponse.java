package com.fyp.ehb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawMaterialResponse {

	private String name;
	private String remainingStock;
	private String availability;
	private String rawMateId;
	private List<RawMaterialHistoryMain> rawHistoryList;
}
