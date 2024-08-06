package com.ssafy.mulryuproject.controller;

import java.util.List;

import com.ssafy.mulryuproject.servcie.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// 서버 간 통신 담당 Controller

@RestController
@RequiredArgsConstructor
@Tag(name = "MulToRobot", description = "로봇 서버로 order를 전달하는 API")
@RequestMapping("/MulToRobot")
public class MulConnToRobotCon {
	private final MulMakeOrderService toMakeOrderService;
	private final MulSaveOrderToMongo saveOrderService;
	private final MulTransmitOrderService transmitService;
	private final MulOrderNumService onService;
	private final MulProductSectorService psService;
	
	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	@Operation(
            summary = "RabbitMQ로 order를 전달하는 API",
            description = "MulOrder 데이터 여러개를 List 형식으로 받아와 해당 Order에 맞는 sector를 배분하여 json 형식의 order를 만든 후 RabbitMQ에 전송합니다.\n"
                    + "order가 만들어지는 시점에 DB가 업데이트 되며, RabbitMQ로 데이터가 전송되면 해당 order 데이터가 MongoDB에 저장됩니다."
    )
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "MulOrderNumber의 값을 받아옵니다. 실제로 전송되는 데이터는 orderNumberId 하나 뿐이어도 정상적으로 작동합니다.\n"
					+ "Id의 값은 반드시 1 이상이여야 하며, status=Delivery 인 orderNumber를 가져오려 하면 서버 내부적으로 무시됩니다."
			)
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody MulOrderNumber orderNumber) { //

		long beforeTime = System.currentTimeMillis();
		System.out.println(orderNumber.toString());
		MulMakeOrder robot = toMakeOrderService.makeOrder(orderNumber); 
		
		Gson jsonToRobot = new Gson();
		String robotOrderToJson = jsonToRobot.toJson(robot);
		
		// 0729 Ssafy Wifi에서 통신 불가능
		transmitService.sendMessage(robotOrderToJson); // 0초~1초 사이

		// 0724 LHJ orderStatus를 Toggle 형식으로 바꿈
		MulOrderNumber on = onService.getOrderNumber(orderNumber);
		System.out.println(on.toString());
		onService.toggleOrderStatus(on);
		System.out.println(robotOrderToJson);

		// rabbitMQ에 보내게 되면 업데이트
		psService.updateQuantity(robot);

		// 몽고 DB에도 저장
		saveOrderService.saveRobotOrderToMongo(robot);

		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("시간차이(m) : "+secDiffTime);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
