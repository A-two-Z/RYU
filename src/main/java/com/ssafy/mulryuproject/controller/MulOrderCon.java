package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class MulOrderCon {
	private final MulOrderService orderService;

	private final MulProductSectorService psService;
	
	private final MulProductService productService;
	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody List<MulOrder> order){
		
		// 나중에 MulToRobotService로 리펙토링 하기
		for(MulOrder one : order) {
			Optional<MulOrder> str = orderService.getOrder(one);
			System.out.println(str.get());
			
			int productId = str.get().getProductId().getProductId();
			
			Optional<MulProduct> product = productService.getProduct(MulProduct.builder().productId(productId).build());
			System.out.println(product.get());
			
			List<MulProductSector> sector = psService.getPSListToProduct(product.get());
		
			System.out.println(sector.toString());
			
			
		}
		
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
