package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mulryuproject.dto.MulToRobotDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.entity.MulSector;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulToRobotServiceImpl {

	private final MulProductSectorService psService;

	private final MulSectorService sectorService;

	private final RabbitTemplate rabbitTemplate;

	private static final Logger log = LoggerFactory.getLogger(MulToRobotServiceImpl.class);

	// Order Create
	public MulToRobotDTO createToRobot(MulOrder order, MulProduct product) {
		List<MulProductSector> sector = psService.getPSListToProduct(product);
		MulProductSector getFirst = null;

		// 0722 LHJ list.getFirst() 메소드와 동일한 기능
		for (MulProductSector sectorOne : sector) {
			if(sectorOne.getQuantity() > order.getOrderQuantity()) {
				
			}
			
			getFirst = sectorOne;
			break;
		}

		Optional<MulSector> sectorOne = sectorService
				.getSector(
						MulSector
						.builder()
						.sectorId(
								getFirst
								.getSectorId()
								.getSectorId())
						.build());

		MulToRobotDTO robot = new MulToRobotDTO();
		robot.setProductName(product.getProductName());
		robot.setSectorName(sectorOne.get().getSectorName());
		robot.setOrderQuantity(order.getOrderQuantity());

		System.out.println(robot.toString());

		return robot;
	}
	
	// Transmit
//	@RabbitListener(queues = "robot.queue")
	public void sendMessage(List<MulToRobotDTO> robotDTO) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String robotOrder = objectMapper.writeValueAsString(robotDTO);
			rabbitTemplate.convertAndSend(robotOrder);
			
			Message message = MessageBuilder.withBody(robotOrder.getBytes())
                    .setHeader("content_type", "application/json")
                    .build();

			System.out.println(message.toString());
			 System.out.println("JSON Output Size: " + robotOrder.getBytes().length + " bytes");

			
//			rabbitTemplate.send("robot.exchange", "robot.key", message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	// Read?

	// ReadOne?

	// Update?

	// Delete?
}
