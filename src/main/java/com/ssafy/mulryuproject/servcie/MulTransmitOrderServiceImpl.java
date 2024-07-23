package com.ssafy.mulryuproject.servcie;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.constants.RabbitMQConstants;

import lombok.RequiredArgsConstructor;


// 0723 LHJ MQ를 통해 로봇 서버와 통신하는 기능을 구현한 클래스
@Service
@RequiredArgsConstructor
public class MulTransmitOrderServiceImpl {
	
	private final RabbitTemplate rabbitTemplate;

	// RabbitMQ에 메세지를 보내는 메소드 
    @Autowired
    private TopicExchange exchange;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange.getName(), RabbitMQConstants.ROUTING_KEY, message);
    }
}
