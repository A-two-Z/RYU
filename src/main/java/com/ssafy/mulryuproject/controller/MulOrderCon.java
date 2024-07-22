package com.ssafy.mulryuproject.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulToRobotDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulProductService;
import com.ssafy.mulryuproject.servcie.MulSectorService;
import com.ssafy.mulryuproject.servcie.MulToRobotServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class MulOrderCon {
	private final MulOrderService orderService;

	private final MulToRobotServiceImpl toRobotService;
	
	private final MulProductService productService;

	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody List<MulOrder> order){
		List<MulToRobotDTO> robotList = new LinkedList<>();
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
			
			MulToRobotDTO robot = toRobotService.createToRobot(orderOne.get(), product.get());
			robotList.add(robot);
		}
		
		toRobotService.sendMessage(robotList);
		
		return null;
	}
	
	//Create
	@PostMapping("")
	public ResponseEntity<MulOrder> createOrder(){
		
		return null;
	}
	
	//getList
	
	//getOne
	
	//getStatusList
	
	//update
	
	//delete
}
