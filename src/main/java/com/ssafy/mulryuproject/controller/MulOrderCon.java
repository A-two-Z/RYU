package com.ssafy.mulryuproject.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class MulOrderCon {

	private final MulOrderService service;
	private final MulSaveOrderToMongo mongoOrderService;
	
	// Create
	@PostMapping
	public ResponseEntity<MulOrder> createOrder(@RequestBody MulOrder order) {
		MulOrderDTO orderDto = new MulOrderDTO();

		orderDto.setProductId(order.getProductId().getProductId());

		orderDto.setOrderQuantity(order.getOrderQuantity());
		orderDto.setOrderNumber(order.getOrderNumber());
		orderDto.setOrderStatus(MulOrderStatus.WAIT);

		MulOrder savedEntity = service.saveOrder(orderDto);

		return ResponseEntity.ok(savedEntity);
	}

	// Read List
	@GetMapping
	public ResponseEntity<List<MulOrder>> getOrderList() {
		List<MulOrder> orderList = service.getOrderList();

		if (orderList != null && !orderList.isEmpty()) {
			return new ResponseEntity<>(orderList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	// Read One
	@GetMapping("/order_{id}")
	public ResponseEntity<MulOrder> getOrderDetail(@PathVariable int id) {
		MulOrder order = MulOrder
				.builder()
				.orderId(id)
				.build();
		Optional<MulOrder> orderOne = service.getOrder(order);

		return orderOne.get() == null ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
				: new ResponseEntity<>(orderOne.get(), HttpStatus.OK);
	}
	
	// MongoDB로부터 특정 시점 이후의 order 데이터를 가져옴
	@GetMapping("/after")
    public ResponseEntity<List<MulMakeOrder>> getOrdersAfter(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<MulMakeOrder> list = mongoOrderService.getOrdersAfter(date);
		return list.size() <= 0 ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
				: new ResponseEntity<>(list, HttpStatus.OK);
    }
	
	// getStatusList
	@GetMapping("orderList")
    public ResponseEntity<List<MulOrder>> getOrderListToStatus(@RequestParam String status) {
        List<MulOrder> orderList = null;

        // Enum 상수와 문자열을 비교할 때는 equals()를 사용합니다.
        if (MulOrderStatus.WAIT.name().equals(status)) {
            orderList = service.getOrderStatusList(MulOrderStatus.WAIT);
        } else if (MulOrderStatus.DELIVER.name().equals(status)) {
            orderList = service.getOrderStatusList(MulOrderStatus.DELIVER);
        }

        if (orderList == null || orderList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        }
    }
	// update

	// delete
}
