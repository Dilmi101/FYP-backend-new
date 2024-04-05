package com.fyp.ehb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fyp.ehb.domain.Dashboard;

public interface DashboardDao extends MongoRepository<Dashboard, String>{

}
