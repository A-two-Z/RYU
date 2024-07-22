package com.ssafy.mulryuproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class MulOrderCon {
	private final MulOrderService orderService;

	private final MulProductSectorService  psService;
	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody List<MulOrder> order){
		
		System.out.println(order.toString());
		
		// 전송해야 할 데이터: Order + Sector 여러개
		// 받아오는 데이터: Order 목록들
		
//		MulProduct product = MulProduct.builder()
//				.productId(order.getOrderId())
//				.build();
//		
//		List<MulProductSector> list =  psService.getPSListToProduct(product);
//		
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
