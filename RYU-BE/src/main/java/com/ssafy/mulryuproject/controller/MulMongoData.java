package com.ssafy.mulryuproject.controller;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
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
	@Operation(summary = "MongoDB에 저장된 모든 데이터를 가져옴", description = "현재까지 MongoDB에 저장된 모든 주문 데이터를 가져옵니다.")
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
	@Operation(summary = "MongoDB에서 orderNumber를 ID로 가진 값을 가져옴")
	public ResponseEntity<MulMakeOrder> findByOrderNumberId(@RequestParam String orderNumber) {
		return new ResponseEntity<>(service.findByOrderNumberId(orderNumber), HttpStatus.OK);
	}

	// MongoDB로부터 특정 시점 이후의 order 데이터를 가져옴
	@GetMapping("/after")
	@Operation(summary = "로봇에게 전달된 order를 특정 시점 이후로 모든 메소드", description = "Date 파라미터를 통해 특정 날짜 이후의 모든 데이터를 받아오는 기능을 담당합니다.")
	@Parameter(description = "date 파라미터를 받아오며, 파라미터의 패턴은 yyyy-MM-dd 형식입니다.")
	public ResponseEntity<List<MulMakeOrder>> getOrdersAfter(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		List<MulMakeOrder> list = service.getOrdersAfter(date);
		return list.size() <= 0 ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
				: new ResponseEntity<>(list, HttpStatus.OK);
	}

}

