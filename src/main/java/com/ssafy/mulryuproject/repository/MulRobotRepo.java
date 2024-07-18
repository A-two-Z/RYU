package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.dto.MulRobotDTO;
import com.ssafy.mulryuproject.entity.MulRobot;
import com.ssafy.mulryuproject.enums.MulRobotStatus;

public interface MulRobotRepo extends JpaRepository<MulRobot, Integer>{
	
	List<MulRobot> findByRobotStatus(MulRobotStatus status);


}
