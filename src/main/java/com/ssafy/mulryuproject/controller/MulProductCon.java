package com.ssafy.mulryuproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.servcie.MulProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mul")
public class MulProductCon {
	private final MulProductService service;

	@GetMapping("/{str}")
	public ResponseEntity<MulProduct> test(@PathVariable String str){
		MulProductDTO productDto = new MulProductDTO();
		productDto.setProductName(str);
		MulProduct savedEntity = service.saveProductEntity(productDto);
		return ResponseEntity.ok(savedEntity);
	}
	
}
