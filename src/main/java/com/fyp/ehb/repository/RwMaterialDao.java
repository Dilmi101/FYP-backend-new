package com.fyp.ehb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fyp.ehb.domain.RawMaterial;

public interface RwMaterialDao extends MongoRepository<RawMaterial, String> {

}
