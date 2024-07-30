package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productSector")
public class MulProductSectorCon {
	
	private final MulProductSectorService service;
	
	//Create
	@PostMapping
	public ResponseEntity<MulProductSector> saveProductSector(@RequestBody MulProductSector ps) {
		MulProductSectorDTO psDto = new MulProductSectorDTO();
		psDto.setProductId(ps.getProductId().getProductId());
		psDto.setQuantity(ps.getQuantity());
		psDto.setSectorId(ps.getSectorId().getSectorId());
		MulProductSector savedEntity = service.savePS(psDto);

		return ResponseEntity.ok(savedEntity);
	}
	
	//getList
	@GetMapping
	public ResponseEntity<List<MulProductSector>> getProductSectorList() {
		List<MulProductSector> productList = service.getPSList();

		if (productList != null && !productList.isEmpty()) {
			return new ResponseEntity<>(productList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}
	
	//getOne
	@GetMapping("/productSector_{id}")
	public ResponseEntity<MulProductSector> getProductSectorOne(@PathVariable int id) {
		MulProductSector product = MulProductSector
				.builder()
				.productId(MulProduct
						.builder()
						.productId(id)
						.build())
				.build();
		Optional<MulProductSector> productOne = service.getPS(product);
		
		return productOne.get() == null 
			    ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY) 
			    : new ResponseEntity<>(productOne.get(), HttpStatus.OK);
	}
	
	//update
	
	//delete
	@DeleteMapping("/{id}")
	public ResponseEntity<MulProductSector> deleteProduct(@PathVariable int id) {
		boolean deleteEntity = service.deletePStById(id);
		return (deleteEntity ? new ResponseEntity<>(HttpStatus.ACCEPTED)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
}
