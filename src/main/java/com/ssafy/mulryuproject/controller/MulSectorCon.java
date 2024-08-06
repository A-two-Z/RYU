package com.ssafy.mulryuproject.controller;

import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.servcie.MulSectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mulryuproject.servcie.MulOrderService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sector")
public class MulSectorCon {
	private final MulSectorService sectorService;

    //Create
	
	//getList
	@GetMapping
    public ResponseEntity<List<MulSector>> getSectorList(){
        List<MulSector> sectorInfo = sectorService.getSectorList();
        return new ResponseEntity<>(sectorInfo, HttpStatus.OK);
    }
	//getOne
	
	//update
	
	//delete
}
