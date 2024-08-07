package com.ssafy.mulryuproject.controller;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "MongoDB Controller", description = "데이터 백업 및 일관성 유지를 위해 MongoDB에 주문 데이터를 백업하여 사용합니다.")
@RequestMapping("/Mongo")
public class MulMongoData {
	private final MulSaveOrderToMongo service;

	@GetMapping
	public ResponseEntity<List<MulMakeOrder>> getOrderForDate(@RequestParam Date date) {
		List<MulMakeOrder> list = service.getOrdersAfter(date);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/data")
	@Operation(summary = "MongoDB에서 가장 마지막으로 저장된 데이터를 가져옴", description = "해당 메소드 호출 기준 가장 최근에 저장된 데이터 불러오기")
	public ResponseEntity<MulMakeOrder> getLastData() {
		return new ResponseEntity<>(service.getLastData(), HttpStatus.OK);
	}

	@GetMapping("/number")
	public ResponseEntity<MulMakeOrder> findByOrderNumberId(@RequestParam String orderNumber) {
		return new ResponseEntity<>(service.findByOrderNumberId(orderNumber), HttpStatus.OK);
	}
}
