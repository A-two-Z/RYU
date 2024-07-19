package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssafy.mulryuproject.entity.MulRobot;
import com.ssafy.mulryuproject.gateway.MqttGateway;
import com.ssafy.mulryuproject.servcie.MulRobotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/robot")
public class MulRobotCon {
	
	private final MulRobotService service;

	private final MqttGateway mqtGateway;
	
	private final BlockingQueue<MulRobot> queue;
	
	// 로봇 status 토글
	@GetMapping("/{id}")
	public ResponseEntity<MulRobot> toggleRobotStatus(@PathVariable int id) {
		Optional<MulRobot> robot = service.getRobot(id);
		
		JsonObject message = new Gson().toJsonTree(robot.get()).getAsJsonObject();
		
		System.out.println(message.toString()); // 연동 체크 코드
		
		mqtGateway
		.sendToMqtt(
				message.get("robotId").getAsString(),
				message.get("robotStatus").toString()
				);
		
		System.out.println(queue.toString());
		System.out.println(queue.size());
		
		
		MulRobot savedEntity = service.toggleRobotStatus(robot.get());
		return ResponseEntity.ok(savedEntity);
	}

	// 모든 로봇 가져옴
	@GetMapping
	public ResponseEntity<List<MulRobot>> getRobotList(){ 
		
		List<MulRobot> list = service.getRobotList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/rest")
	public ResponseEntity<List<MulRobot>> getRestRobotList(){ // 서버 시작할 때 반드시 가장 먼저 실행되어야 할 메소드
		
		List<MulRobot> restList = service.getRestRobotList();
		
		for(MulRobot tmp : restList) {
			queue.add(tmp);
		}
		System.out.println(queue.toString());
		
		return ResponseEntity.ok(restList);
	}
	
	// Create, Delete는 후순위
	
}
