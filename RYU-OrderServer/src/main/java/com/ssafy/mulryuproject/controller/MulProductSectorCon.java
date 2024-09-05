package com.ssafy.mulryuproject.controller;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.dto.MulSectorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product Sector Controller", description = "Sector 별 물품과 수량을 관리하는 컨트롤러")
@RequestMapping("/productSector")
public class MulProductSectorCon {
	
	private final MulProductSectorService service;
	
	//Create
	@PostMapping
	@Operation(
			summary = "product Sector를 만드는 메소드",
			description = "Product, Sector, Quantity를 전송하면 데이터가 생성됩니다."
	)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "productId, sectorId, quantity를 한 개씩 전달받습니다.")
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
	@Operation(
			summary = "product Sector 리스트를 전달하는 메소드",
			description = "Sector 별 물품과 수량에 대해 전체적인 테이블을 제공하는 메소드 입니다."
	)
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
	@Operation(
			summary = "Sector 이름을 전송하면 product Sector 리스트를 전달하는 메소드",
			description = "한 개의 Sector 안에 있는 물품과 수량에 대한 정보를 제공하는 메소드 입니다."
	)
	public ResponseEntity<Map<String, List<MulSectorData>>> getSectorProduct(@RequestParam String SectorName){
		Map<String, List<MulSectorData>> sectorInfo = service.getListBySectorName(SectorName);
		return new ResponseEntity<>(sectorInfo, HttpStatus.OK);
	}

	/* // 해당 메소드는 API로 구현하더라도 위의 sectorInfo 메소드에 비해 사용되지 않으리라 판단하여 구현하지 않았습니다.
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
	*/
	//update

	/* 현재는 사용하지 않는 메소드

	//delete
	@Operation(
			summary = "Sector의 id를 전송하면 해당 productSector를 삭제하는 메소드",
			description = "PS를 삭제하는 메소드입니다.."
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<MulProductSector> deleteProduct(@PathVariable int id) {
		boolean deleteEntity = service.deletePStById(id);
		return (deleteEntity ? new ResponseEntity<>(HttpStatus.ACCEPTED)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

	 */
}
