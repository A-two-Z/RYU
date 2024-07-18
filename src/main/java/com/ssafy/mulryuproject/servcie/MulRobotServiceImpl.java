package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulRobotDTO;
import com.ssafy.mulryuproject.entity.MulRobot;
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

	@Override
	public List<MulRobot> getRobotListEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MulRobot> getRobotStatusEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<MulRobot> getRobotEntity(MulRobot robot) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public MulRobot updateRobotEntity(MulRobot dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteRobotById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}


}
