package com.ssafy.mulryuproject.controller;

import com.ssafy.mulryuproject.entity.MulOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/status") // 컨트롤러의 기본 경로를 지정합니다.
public class TEST {

    @PostMapping
    public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody String getOrder) {
        System.out.println("OK");
        System.out.println(getOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
