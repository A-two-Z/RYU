package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/product")
public class MulProductCon {
	private final MulProductService service;

	// Create
	@PostMapping
	public ResponseEntity<MulProduct> saveProduct(@RequestBody MulProduct product) {
		MulProductDTO productDto = new MulProductDTO();
		productDto.setProductName(product.getProductName());
		MulProduct savedEntity = service.saveProduct(productDto);

		return ResponseEntity.ok(savedEntity);
	}

	// Read List
	@GetMapping
	public ResponseEntity<List<MulProduct>> getProductList() {
		List<MulProduct> productList = service.getProductList();

		if (productList != null && !productList.isEmpty()) {
			return new ResponseEntity<>(productList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	// Read One
	@GetMapping("/product_{id}")
	public ResponseEntity<MulProduct> getProductOne(@PathVariable int id) {
		MulProduct product = MulProduct.builder().productId(id).build();
		Optional<MulProduct> productOne = service.getProduct(product);
		
		return productOne.get() == null 
			    ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY) 
			    : new ResponseEntity<>(productOne.get(), HttpStatus.OK);
	}

	// Update
	@PutMapping
	public ResponseEntity<MulProduct> updateProduct(@RequestBody MulProduct product) {
		MulProduct savedEntity = service.updateProduct(product);
		return ResponseEntity.ok(savedEntity);
	}

	// Delete
	@DeleteMapping("/{id}")
	public ResponseEntity<MulProduct> deleteProduct(@PathVariable int id) {
		boolean deleteEntity = service.deleteProductById(id);
		return (deleteEntity ? new ResponseEntity<>(HttpStatus.ACCEPTED)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

}
