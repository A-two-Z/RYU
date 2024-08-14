package com.ssafy.mulryuproject.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "주문 관련 데이터를 CRUD하는 API")
@RequestMapping("/order")
@Slf4j
public class MulOrderCon {

	private final MulOrderService orderService;
	private final MulOrderNumService orderNumservice;

	// Create
	@PostMapping
	@Operation(summary = "주문을 생성하는 메소드", description = "한 개의 주문을 생성하는 메소드입니다.")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
			required = true,
			description = "Json 형식으로 받아온 뒤, 클래스 내부에서 Json 데이터를 파싱하는 형식입니다.",
			content = @Content(schema = @Schema(type = "object"),
					examples = @ExampleObject(name = "Order Example",
							value = "{ \"orders\": [ { \"productId\": 0, \"quantity\": 0 }, { \"productId\": 0, \"quantity\": 0 } ], \"orderNumber\": 0 }")))
	public ResponseEntity<MulOrder> createOrder(@RequestBody String getOrder) {
		// Order를 Json으로 받아옴
		JsonObject jsonObject = JsonParser.parseString(getOrder).getAsJsonObject();
		
		// orderNumberTable에 OrderNumber를 저장한다
		String orderNumber = jsonObject.get("orderNum").getAsString();
		MulOrderNumber saveNumber = orderNumservice.saveOrderNum(MulOrderNumber.builder().orderNumber(orderNumber).build());
		
		// 테이블에 저장된 orderNumber를 통해 OrderNumber 테이블의 PK를 가져온다
		int orderNumberId = saveNumber.getOrderNumberId();

		log.info("서버에서 주문번호 [" +orderNumber+"]와 관련한 주문을 받아옴");

		JsonArray ordersArray = jsonObject.getAsJsonArray("orders");
		
		for (JsonElement order : ordersArray) {
			JsonObject jsonOrder = order.getAsJsonObject();

			int productId = jsonOrder.get("productId").getAsInt();
			int quantity = Integer.parseInt(jsonOrder.get("quantity").getAsString());

			MulOrder saveOrder = MulOrder
					.builder()
					.productId(
							MulProduct
							.builder()
							.productId(productId)
							.build())
					.orderQuantity(quantity)
					.orderNumberId(
							MulOrderNumber
							.builder()
							.orderNumberId(orderNumberId)
							.build())
					.build();

			orderService.saveOrder(saveOrder);

			log.info("DB에 Order 데이터 정상적으로 저장됨");

		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	// Read List
	@GetMapping
	@Operation(summary = "주문을 가져오는 메소드", description = "status가 Delivery인 주문을 포함한, 모든 주문을 가져오는 메소드입니다.")
	public ResponseEntity<List<MulOrder>> getOrderList() {
		List<MulOrder> orderList = orderService.getOrderList();

		if (orderList != null && !orderList.isEmpty()) {
			return new ResponseEntity<>(orderList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	// Read One
	@GetMapping("/order_{id}")
	@Operation(summary = "한 개의 주문을 가져오는 메소드", description = "orderId를 통해 하나의 주문을 가져오는 메소드 입니다.")
	public ResponseEntity<MulOrder> getOrderDetail(@PathVariable int id) {
		MulOrder order = MulOrder.builder().orderId(id).build();
		Optional<MulOrder> orderOne = orderService.getOrder(order);

		return orderOne.get() == null ? new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
				: new ResponseEntity<>(orderOne.get(), HttpStatus.OK);
	}


	// getStatusList
	/*
	 * @GetMapping("orderList") public ResponseEntity<List<MulOrder>>
	 * getOrderListToStatus(@RequestParam String status) { List<MulOrder> orderList
	 * = null;
	 * 
	 * MulOrderNum orderNumber = orderNumservice.
	 * 
	 * // Enum 상수와 문자열을 비교할 때는 equals()를 사용합니다. if
	 * (MulOrderStatus.WAIT.name().equals(status)) { orderList =
	 * orderService.getOrderStatusList(MulOrderStatus.WAIT); } else if
	 * (MulOrderStatus.DELIVER.name().equals(status)) { orderList =
	 * orderService.getOrderStatusList(MulOrderStatus.DELIVER); }
	 * 
	 * if (orderList == null || orderList.isEmpty()) { return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); } else { return new
	 * ResponseEntity<>(orderList, HttpStatus.OK); } }
	 */
	// update

	// delete
}
