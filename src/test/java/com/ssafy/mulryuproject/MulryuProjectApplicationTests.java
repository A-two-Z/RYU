package com.ssafy.mulryuproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.servcie.RedisService;

@SpringBootTest
class MulryuProjectApplicationTests {

	@Autowired
	private RedisService redisService;

	@BeforeEach
	void setUp() {
		// Reset or initialize Redis state if needed
	}

	@Test
	void checkBuild() {
		MulOrder order = MulOrder.builder().orderId(1).orderQuantity(10)
				.orderNumberId(MulOrderNumber.builder().orderNumberId(1).build())
				.productId(MulProduct.builder().productId(1).build()).build();
		
		System.out.println(order.toString());

		 MulOrder updatedOrder = order.toBuilder()
			        .orderNumberId(null)
			        .build();
			    
	    System.out.println("Updated Order: " + updatedOrder);
		
	}

//	@Test
	void contextLoads() {
		// Given
		String key = "testKey";
		String value = "testValue";

		// When
		redisService.saveValue(key, value);
		String retrievedValue = redisService.getValue(key);

		// Then
		assertEquals(value, retrievedValue, "The retrieved value should match the saved value.");

	}

}
