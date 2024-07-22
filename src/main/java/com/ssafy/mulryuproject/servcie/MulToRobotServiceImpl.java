package com.ssafy.mulryuproject.servcie;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
import com.ssafy.mulryuproject.entity.MulToRobot;
import com.ssafy.mulryuproject.repository.MulToRobotOrder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulToRobotServiceImpl {

	private final MulProductSectorService psService;

	private final MulSectorService sectorService;

	private final RabbitTemplate rabbitTemplate;

	private final MulToRobotOrder orderRepository;
	
	public List<MulToRobotDTO> chkSectorQuantity(MulOrder order, MulProduct product) {
		List<MulProductSector> sector = psService.getPSListToProduct(product);
		int orderQuantity = order.getOrderQuantity();
		List<MulToRobotDTO> robotDestinationList = new LinkedList<>();
		
		for (MulProductSector sectorOne : sector) {
			// 반드시 처음 한 번은 섹터에 들려야 한다.
			 robotDestinationList.add(createToRobot(order, product));

			 if(sectorOne.getQuantity() >= orderQuantity) {// 하나의 섹터에만 가면 된다.
				 orderQuantity = 0;
				 break;
			 }
			else  // 2개 이상의 섹터에 가야한다.
				orderQuantity -= sectorOne.getQuantity();
		}
		
		if(orderQuantity == 0) {
			return robotDestinationList;
		}else {
			return null;
		}
	}
	
	// Order Create ( sector의 갯수가 하나 이상일 때 )
	public MulToRobotDTO createToRobot(MulOrder order, MulProduct product) {
		List<MulProductSector> sector = psService.getPSListToProduct(product);
		MulProductSector getFirst = null;

		// 0722 LHJ list.getFirst() 메소드와 동일한 기능
		for (MulProductSector sectorOne : sector) {
			getFirst = sectorOne;
			break;
		}

		Optional<MulSector> sectorOne = sectorService
				.getSector(MulSector.builder().sectorId(getFirst.getSectorId().getSectorId()).build());

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
					.setHeader("content_type", "application/json").build();

			System.out.println(message.toString());
			System.out.println("JSON Output Size: " + robotOrder.getBytes().length + " bytes");

//			rabbitTemplate.send("robot.exchange", "robot.key", message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	// RabbitMQ와 통신이 되었다는 걸 확인하고, DB를 업데이트 하는 메소드
	
	
	
	// MongoDB에 Robot으로 전달한 데이터를 백업
	public void saveRobotOrderToMongo(List<MulToRobotDTO> list) {
		MulToRobot saveRobotOrder = MulToRobot.builder()
				.orders(list)
				.orderDate(new Date())
				.build();
		
		orderRepository.save(saveRobotOrder);
	}
	
	
}
