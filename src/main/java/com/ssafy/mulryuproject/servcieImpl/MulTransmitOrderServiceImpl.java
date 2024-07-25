package com.ssafy.mulryuproject.servcieImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.constants.RabbitMQConstants;
import com.ssafy.mulryuproject.servcie.MulTransmitOrderService;

import lombok.RequiredArgsConstructor;


// 0723 LHJ MQ를 통해 로봇 서버와 통신하는 기능을 구현한 클래스
@Service
@RequiredArgsConstructor
public class MulTransmitOrderServiceImpl implements MulTransmitOrderService {
//	private static final Logger log = LoggerFactory.getLogger(MulTransmitOrderServiceImpl.class);

	private final RabbitTemplate rabbitTemplate;

	// RabbitMQ에 메세지를 보내는 메소드 
    @Override
	public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_NAME, RabbitMQConstants.ROUTING_KEY, message);
    }
    
}
