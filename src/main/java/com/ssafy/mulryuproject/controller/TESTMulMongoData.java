package com.ssafy.mulryuproject.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "주문 관련 데이터를 CRUD하는 API")
@RequestMapping("/Mongo")
public class TESTMulMongoData {
	private final MulSaveOrderToMongo service;

	@GetMapping
	public ResponseEntity<List<MulMakeOrder>> getOrderForDate(@RequestParam Date date) {
		List<MulMakeOrder> list = service.getOrdersAfter(date);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/data")
	public ResponseEntity<MulMakeOrder> getLastData() {
		return new ResponseEntity<>(service.getLastData(), HttpStatus.OK);
	}

	@GetMapping("/number")
	public ResponseEntity<MulMakeOrder> findByOrderNumberId(@RequestParam String orderNumber) {
		return new ResponseEntity<>(service.findByOrderNumberId(orderNumber), HttpStatus.OK);
	}
}
