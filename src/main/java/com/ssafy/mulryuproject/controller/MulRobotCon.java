package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
	
	// 로봇 status 토글
	@GetMapping("/{id}")
	public ResponseEntity<MulRobot> toggleRobotStatus(@PathVariable int id) {
		Optional<MulRobot> robot = service.getRobotEntity(id);
		
		JsonObject message = new Gson().toJsonTree(robot.get()).getAsJsonObject();
		
//		System.out.println(message.toString()); // 연동 체크 코드
		
		mqtGateway
		.sendToMqtt(
				message.get("robotId").getAsString(),
				message.get("robotStatus").toString()
				);
		
		MulRobot savedEntity = service.toggleRobotEntity(robot.get());
		return ResponseEntity.ok(savedEntity);
	}
	
	@GetMapping
	public ResponseEntity<List<MulRobot>> getRobotList(){
		List<MulRobot> list = service.getRobotListEntity();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/rest")
	public ResponseEntity<List<MulRobot>> getRestRobotList(){
		List<MulRobot> restList = service.getRestRobotListEntity();
		return ResponseEntity.ok(restList);
	}
	
	// Create, Delete는 후순위

}
