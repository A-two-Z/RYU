package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulRobot;

public interface MulRobotRepo extends JpaRepository<MulRobot, Integer>{
	
	List<MulRobot> findByRobotStatus(int status);
}
