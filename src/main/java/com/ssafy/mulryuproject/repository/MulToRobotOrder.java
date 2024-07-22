package com.ssafy.mulryuproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.mulryuproject.entity.MulToRobot;

@Repository
public interface MulToRobotOrder extends MongoRepository<MulToRobot, String>{

}
