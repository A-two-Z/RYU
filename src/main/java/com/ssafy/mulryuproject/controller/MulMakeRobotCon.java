package com.ssafy.mulryuproject.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.data.MulMakeRobotDetail;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductService;
import com.ssafy.mulryuproject.servcie.MulMakeRobotServiceImpl;

import lombok.RequiredArgsConstructor;


// 서버 간 통신 담당 Controller

@RestController
@RequiredArgsConstructor
@RequestMapping("/MulToRobot")
public class MulMakeRobotCon {
	private final MulOrderService orderService;

	private final MulMakeRobotServiceImpl toRobotService;
	
	private final MulProductService productService;

	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody List<MulOrder> order){
		List<MulMakeRobotDetail> robotList = new LinkedList<>();
		for(MulOrder getOrderOne : order) {
			Optional<MulOrder> orderOne = orderService.getOrder(getOrderOne);
			
			Optional<MulProduct> product = productService
					.getProduct(
							MulProduct
							.builder()
							.productId(orderOne.get()
									.getProductId()
									.getProductId())
							.build());
			
			MulMakeRobotDetail robot = toRobotService.createToRobot(orderOne.get(), product.get());
			robotList.add(robot);
		}
		
		toRobotService.sendMessage(robotList);
		
		return null;
	}
}
