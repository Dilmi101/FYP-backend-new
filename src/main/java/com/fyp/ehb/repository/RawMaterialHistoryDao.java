package com.fyp.ehb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fyp.ehb.domain.RawMaterialHistory;

public interface RawMaterialHistoryDao extends MongoRepository<RawMaterialHistory, String>{

	public List<RawMaterialHistory> getRawMaterialsById(String id);

}
