package com.ssafy.mulryuproject.controller;

import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.servcie.MulSectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.servcie.MulOrderService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "MulSector", description = "Sector와 관련한 기능을 담당하는 컨트롤러")
@RequestMapping("/sector")
public class MulSectorCon {
	private final MulSectorService sectorService;

    //Create
	
	//getList
	@GetMapping
    @Operation(
            summary = "Sector의 정보를 받아오는 API",
            description = "Sector의 이름 정보를 받아오는 API입니다. sector의 경우 24.08.06 기준 15개의 sector가 있으며" +
                    "sector의 이름은 S001 ~ S015 까지 존재합니다."
    )
    public ResponseEntity<List<MulSector>> getSectorList(){
        List<MulSector> sectorInfo = sectorService.getSectorList();
        return new ResponseEntity<>(sectorInfo, HttpStatus.OK);
    }
	//getOne
	
	//update
	
	//delete
}
