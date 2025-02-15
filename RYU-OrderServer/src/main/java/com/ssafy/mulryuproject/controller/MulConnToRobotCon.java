package com.ssafy.mulryuproject.controller;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.mulryuproject.servcie.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.ssafy.mulryuproject.servcieImpl.MulCheckIsOrderClear;

import static com.ssafy.mulryuproject.constants.logsConstants.MULSTATUS;

// 서버 간 통신 담당 Controller

@RestController
@RequiredArgsConstructor
@Tag(name = "MulToRobot", description = "로봇 서버로 order를 전달하는 API")
@RequestMapping("/MulToRobot")
@Slf4j
public class MulConnToRobotCon {
	private final MulMakeOrderService toMakeOrderService;
	private final MulSaveOrderToMongo saveOrderService;
	private final MulTransmitOrderService transmitService;
	private final MulOrderNumService onService;
	private final MulProductSectorService psService;

	private final MulCheckIsOrderClear clear;
	private final MulCheckIsOrderClear IsOrderClear;

	// 중요! RabbitMQ로 전달하는 메소드
	@PostMapping("/orderToQ")
	@Operation(
            summary = "RabbitMQ로 order를 전달하는 API",
            description = "MulOrder 데이터 여러개를 List 형식으로 받아와 해당 Order에 맞는 sector를 배분하여 json 형식의 order를 만든 후 RabbitMQ에 전송합니다.\n"
                    + "order가 만들어지는 시점에 DB가 업데이트 되며, RabbitMQ로 데이터가 전송되면 해당 order 데이터가 MongoDB에 저장됩니다."
    )
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "MulOrderNumber의 값을 받아옵니다. 실제로 전송되는 데이터는 orderNumber 하나 뿐이어도 정상적으로 작동합니다.\n"
					+ "Id의 값은 반드시 1 이상이여야 하며, status=Delivery 인 orderNumber를 가져오려 하면 서버 내부적으로 무시됩니다."
			)
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody MulOrderNumber orderNumber) { //

		MulMakeOrder robot = toMakeOrderService.makeOrder(orderNumber);
		
		Gson jsonToRobot = new Gson();
		String robotOrderToJson = jsonToRobot.toJson(robot);
		
		// 0729 Ssafy Wifi에서 통신 불가능
		transmitService.sendMessage(robotOrderToJson); // 0초~1초 사이

		log.info("주문번호 ["+orderNumber.getOrderNumber()+"]의 배달 명령 RabbitMQ로 전달");

		// 내부적으로 실행
		IsOrderClear.orderNumberPut(orderNumber.getOrderNumber());

		// 0724 LHJ orderStatus를 Toggle 형식으로 바꿈
		MulOrderNumber on = onService.getOrderNumber(orderNumber);
		onService.toggleOrderStatus(on);

		// rabbitMQ에 보내게 되면 업데이트
		psService.updateQuantity(robot);
		log.info("주문번호 ["+orderNumber.getOrderNumber()+"]의 Quantity Redis에서 업데이트");

		// 몽고 DB에도 저장
		saveOrderService.saveRobotOrderToMongo(robot);
		log.info("주문번호 ["+orderNumber.getOrderNumber()+"] 데이터 MongoDB에 백업");

		return new ResponseEntity<>(HttpStatus.OK);
	}


	@Operation(
			summary = "Robot 서버로부터 완료된 OrderNumber를 받아오는 API",
			description = "MulCheckIsOrderClear 클래스의 orderNumberPut 메소드에 구현된 스케줄러를 통해 일정 시간 내로" +
					"Robot 서버에서 OrderNumber를 받아오지 못한다면 해당 order는 성공하지 못한 것으로 판단하여 Redis를 롤백합니다."
	)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "OrderNumber와 status 값을 받아옵니다."
	)
	@PostMapping("status")
	public ResponseEntity<List<MulOrder>> orderToQ(@RequestBody String getOrder) {
		JsonObject jsonObject = JsonParser.parseString(getOrder).getAsJsonObject();

		Gson gson = new Gson();
		MulOrderNumber mul = gson.fromJson(getOrder, MulOrderNumber.class);

		log.info(getOrder+MULSTATUS);

		clear.orderNumberClear(mul.getOrderNumber());

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
