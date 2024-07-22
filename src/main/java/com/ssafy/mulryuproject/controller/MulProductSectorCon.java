package com.ssafy.mulryuproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
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
		psDto.setProduct(ps.getProductId());
		psDto.setQuantity(ps.getQuantity());
		psDto.setSector(ps.getSectorId());
		MulProductSector savedEntity = service.savePS(psDto);

		return ResponseEntity.ok(savedEntity);
	}
	
	//getList

	
	//getOne
	
	//update
	
	//delete
}
