package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulRobotDTO;
import com.ssafy.mulryuproject.entity.MulRobot;
import com.ssafy.mulryuproject.enums.MulRobotStatus;
import com.ssafy.mulryuproject.repository.MulRobotRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulRobotServiceImpl implements MulRobotService {

	private final MulRobotRepo robotRepo;

	// 후순위 메소드
	@Override
	public MulRobot saveRobotEntity(MulRobotDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	// 로봇의 리스트를 가져오는 메소드
	@Override
	public List<MulRobot> getRobotListEntity() {
		return robotRepo.findAll();
	}

	// 쉬는 로봇의 리스트를 가져오는 메소드
	@Override
	public List<MulRobot> getRestRobotListEntity() {
		return robotRepo.findByRobotStatus(MulRobotStatus.REST);
	}

	// 로봇 하나를 가져오는 메소드
	@Override
	public Optional<MulRobot> getRobotEntity(int id) {
		if(id != 0) // robot의 인덱스는 1로 시작하기 때문에 0일 수 없음.
			return robotRepo.findById(id);
		else
			return null;
	}

	// 로봇의 status를 갱신하는 메소드
	@Override
	public MulRobot toggleRobotEntity(MulRobot robot) {
		if (robotRepo.existsById(robot.getRobotId())) {
			MulRobot update = MulRobot.builder()
					.robotId(robot.getRobotId())
					.robotStatus(robot.getRobotStatus() == MulRobotStatus.REST ? MulRobotStatus.WORK : MulRobotStatus.REST)
					.build();
			return robotRepo.save(update);
		}else return null;
	}

	
	// 후순위
	@Override
	public boolean deleteRobotById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}


}
