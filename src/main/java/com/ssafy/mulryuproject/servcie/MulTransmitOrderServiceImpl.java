package com.ssafy.mulryuproject.servcie;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


// 0723 LHJ MQ를 통해 로봇 서버와 통신하는 기능을 구현한 클래스
@Service
@RequiredArgsConstructor
public class MulTransmitOrderServiceImpl {
	
	private final RabbitTemplate rabbitTemplate;

	// MQ로 Transmit
		/*
//		@RabbitListener(queues = "robot.queue")
		public void sendMessage(List<MulToRobotDetail> robotDTO) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				String robotOrder = objectMapper.writeValueAsString(robotDTO);
				rabbitTemplate.convertAndSend(robotOrder);

				Message message = MessageBuilder.withBody(robotOrder.getBytes())
						.setHeader("content_type", "application/json").build();

				System.out.println(message.toString());
				System.out.println("JSON Output Size: " + robotOrder.getBytes().length + " bytes");

//				rabbitTemplate.send("robot.exchange", "robot.key", message);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	*/
		
		// RabbitMQ와 통신이 되었다는 걸 확인하는 메소드

}
