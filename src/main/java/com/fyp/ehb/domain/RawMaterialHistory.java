package com.fyp.ehb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "raw_material_records")
public class RawMaterialHistory {

	@Id
	private String id;
	
	private String status;
	
	private String count;
	
	@DBRef
	private RawMaterial rawMaterial;
}
