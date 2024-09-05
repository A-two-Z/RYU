package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Controller", description = "Product 관련 데이터를 CRUD 하는 컨트롤러")
@RequestMapping("/product")
public class MulProductCon {
	private final MulProductService service;

	// Create
	@PostMapping
	@Operation(
			summary = "product를 생성하는 메소드",
			description = "product의 이름만 전송하면 자동으로 생성됩니다. Id는 auto-increased로 설정되어 클라이언트에서 ID를 전송하더라도 무시됩니다."
	)
	public ResponseEntity<MulProduct> saveProduct(@RequestBody MulProduct product) {
		MulProductDTO productDto = new MulProductDTO();
		productDto.setProductName(product.getProductName());
		MulProduct savedEntity = service.saveProduct(productDto);

		return ResponseEntity.ok(savedEntity);
	}

	// Read List
	@GetMapping
	@Operation(
			summary = "product를 가져오는 메소드",
			description = "product 리스트를 가져옵니다."
	)
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
	@Operation(
			summary = "product 하나를 가져오는 메소드",
			description = "id를 전송하면 product 하나의 기능을 가져옵니다."
	)
	public ResponseEntity<MulProduct> getProductOne(@PathVariable int id) {
		MulProduct product = MulProduct.builder().productId(id).build();
		Optional<MulProduct> productOne = service.getProduct(product);
		
		return productOne.get() == null 
			    ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY) 
			    : new ResponseEntity<>(productOne.get(), HttpStatus.OK);
	}

	// Update
	@PutMapping
	@Operation(
			summary = "product를 업데이트 하는 메소드",
			description = "product를 업데이트 합니다."
	)
	public ResponseEntity<MulProduct> updateProduct(@RequestBody MulProduct product) {
		MulProduct savedEntity = service.updateProduct(product);
		return ResponseEntity.ok(savedEntity);
	}

	/* 현재는 사용하지 않는 메소드
	// Delete
	@DeleteMapping("/{id}")
	@Operation(
			summary = "product를 삭제 하는 메소드",
			description = "product를 삭제 합니다."
	)
	public ResponseEntity<MulProduct> deleteProduct(@PathVariable int id) {
		boolean deleteEntity = service.deleteProductById(id);
		return (deleteEntity ? new ResponseEntity<>(HttpStatus.ACCEPTED)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
*/
}
