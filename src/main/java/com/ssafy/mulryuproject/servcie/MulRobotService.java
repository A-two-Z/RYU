package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulRobotDTO;
import com.ssafy.mulryuproject.entity.MulRobot;

public interface MulRobotService {

	// 로봇을 추가하는 메소드
	public MulRobot saveRobotEntity(MulRobotDTO dto);

	// 로봇 리스트를 가져오는 메소드
	public List<MulRobot> getRobotListEntity();
	
	// 로봇의 status에 따라 로봇을 가져오는 메소드
	public List<MulRobot> getRestRobotListEntity();

	// 특정 로봇 하나를 가져오는 메소드
	public Optional<MulRobot> getRobotEntity(int id);

	// 로봇의 status를 업데이트 하는 메소드
	public MulRobot toggleRobotEntity(MulRobot robot);

	// 로봇 하나를 삭제하는 메소드
	public boolean deleteRobotById(Integer id);
}
