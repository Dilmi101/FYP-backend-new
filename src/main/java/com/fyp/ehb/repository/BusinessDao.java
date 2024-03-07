package com.fyp.ehb.repository;

import com.fyp.ehb.domain.Business;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessDao extends MongoRepository<Business, String> {
}
