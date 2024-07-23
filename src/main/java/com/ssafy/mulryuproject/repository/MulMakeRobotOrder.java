package com.ssafy.mulryuproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.mulryuproject.entity.MulMakeRobot;

@Repository
public interface MulMakeRobotOrder extends MongoRepository<MulMakeRobot, String>{

}
