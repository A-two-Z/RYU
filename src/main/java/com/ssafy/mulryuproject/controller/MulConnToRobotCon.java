package com.ssafy.mulryuproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.servcie.MulMakeOrderService;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;
import com.ssafy.mulryuproject.servcie.MulTransmitOrderService;
import com.ssafy.mulryuproject.servcie.MulTransmitOrderServiceImpl;

import lombok.RequiredArgsConstructor;

// 서버 간 통신 담당 Controller

@RestController
@RequiredArgsConstructor
@RequestMapping("/MulToRobot")
public class MulConnToRobotCon {
	private final MulMakeOrderService toMakeOrderService;
	private final MulSaveOrderToMongo saveOrderService;
	private final MulTransmitOrderService transmitService;
	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody List<MulOrder> order) { //
		MulMakeOrder robot = toMakeOrderService.makeOrder(order);
		Gson jsonToRobot = new Gson();
		String obj = jsonToRobot.toJson(robot);
		
		saveOrderService.saveRobotOrderToMongo(robot);
		
		transmitService.sendMessage(obj);
		
		return null;
	}
}
