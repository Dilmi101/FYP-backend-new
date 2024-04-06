package com.fyp.ehb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fyp.ehb.domain.RawMaterialHistory;

public interface RawMaterialHistoryDao extends MongoRepository<RawMaterialHistory, String>{

}
