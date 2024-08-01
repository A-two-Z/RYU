package com.ssafy.mulryuproject.controller;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.dto.MulSectorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.JsonObject;
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
	
	//0731 LHJ Sector 이름을 전송하면, 해당 sector에 있는 모든 물류 전부 List로 받아서 보내주는 컨트롤러
	@GetMapping("/sectorInfo")
	public ResponseEntity<Map<String, List<MulSectorData>>> getSectorProduct(@RequestParam String SectorName){
		Map<String, List<MulSectorData>> sectorInfo = service.getListBySectorName(SectorName);
		return new ResponseEntity<>(sectorInfo, HttpStatus.OK);
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
		
		return productOne.isEmpty() == true
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
