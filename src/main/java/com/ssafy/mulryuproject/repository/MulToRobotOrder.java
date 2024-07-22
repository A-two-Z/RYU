package com.ssafy.mulryuproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssafy.mulryuproject.entity.MulToRobot;

public interface MulToRobotOrder extends MongoRepository<MulToRobot, String>{

}
