package com.ssafy.mulryuproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Number Controller", description = "주문 번호 관련 데이터를 Read 하는 API")
@RequestMapping("/order_Num")
public class MulOrderNumberCon {
	private final MulOrderNumService service;
	
	@GetMapping
	@Tag(name = "주문 번호 전체를 받아오는 API")
	public ResponseEntity<List<MulOrderNumber>> getOrderNumList(){
		List<MulOrderNumber> list =  service.getOrderNumberList();
		
		return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
	}

//	@GetMapping("/status")
//	public ResponseEntity<List<MulOrderNumber>> getOrderNumStatus(){
//		List<MulOrderNumber> list =  service.getOrderNumberList();
//
//		return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
//	}

}
