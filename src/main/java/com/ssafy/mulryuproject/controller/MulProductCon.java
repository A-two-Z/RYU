package com.ssafy.mulryuproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	// Create
	@PostMapping()
	public ResponseEntity<MulProduct> saveProduct(@RequestBody MulProduct product){
		MulProductDTO productDto = new MulProductDTO();
		productDto.setProductName(product.getProductName());
		MulProduct savedEntity = service.saveProductEntity(productDto);
		return ResponseEntity.ok(savedEntity);
	}
	
	// Read
	@GetMapping
	public ResponseEntity<List<MulProduct>> getProductList(){
		List<MulProduct> productList = service.getProductListEntity();
		
		if (productList != null && !productList.isEmpty()) {
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }	
	}
	
	// Update
	@PutMapping
	public ResponseEntity<MulProduct> updateProduct(@RequestBody MulProduct product){
		MulProduct savedEntity = service.updateProductEntity(product);
		return ResponseEntity.ok(savedEntity);
	}
	
	// Delete
	
	
	
	
}
