package com.fyp.ehb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fyp.ehb.domain.RawMaterialHistory;

public interface RawMaterialHistoryDao extends MongoRepository<RawMaterialHistory, String>{

	@Query(value ="{rawMaterial : ?0}")
	public List<RawMaterialHistory> getRawMaterialsById(String rawMaterial);

}
