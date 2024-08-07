package com.ssafy.mulryuproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ssafy.mulryuproject.dto.MulSectorDTO;
import com.ssafy.mulryuproject.entity.*;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.repository.MulOrderNumRepo;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulSectorService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.mulryuproject.servcie.RedisService;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor
class MulryuProjectApplicationTests {

	@Autowired
	private RedisService redisService;

	@Autowired
	private MulOrderNumRepo orderNumRepo;

	@BeforeEach
	void setUp() {
		// Reset or initialize Redis state if needed
	}

//	@Test
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
	public void toggleOrderStatus() {
		MulOrderNumber orderNum = MulOrderNumber.builder().orderNumberId(2).build();
		orderNumRepo.updateStatus(
				MulOrderStatus.WAIT,
				orderNum.getOrderNumberId());
	}
	MulSectorService sectorService;
	MulProductSectorRepo psRepo;
//	@Test
	public List<MulProductSector> getListBySectorId(String sectorName){
		Optional<MulSector> sectorId = sectorService.getSectorByName(sectorName);
		List<MulProductSector> list = psRepo.findBySectorId(sectorId.get());
		return list;
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
