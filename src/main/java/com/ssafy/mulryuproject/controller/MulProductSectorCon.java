package com.ssafy.mulryuproject.controller;

import java.util.List;
import java.util.Optional;

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
	@GetMapping("/productSector")
	public ResponseEntity<JsonObject> getSectorProduct(@RequestParam String SectorName){
/*
추천 방법
대부분의 경우, 방법 1이 더 나은 접근 방식입니다. 이유는 다음과 같습니다:

비즈니스 로직의 분리: 서비스 간의 비즈니스 로직이 잘 분리되어 있어 유지보수 및 확장이 용이합니다.
테스트 용이성: A 테이블의 비즈니스 로직과 B 테이블의 비즈니스 로직을 독립적으로 테스트할 수 있습니다.
확장성: 나중에 A 테이블이나 B 테이블의 비즈니스 로직이 변경될 때, 서비스 간의 명확한 경계가 유지됩니다.
따라서, A 테이블의 Service를 구현하고 B 테이블의 Service에서 A 테이블의 Service를 Autowired하여 사용하는 방법이 일반적으로 더 나은 선택입니다.
 */
		
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
