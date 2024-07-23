package com.ssafy.mulryuproject.servcie;

public interface MulTransmitOrderService {

	// RabbitMQ에 메세지를 보내는 메소드 
	void sendMessage(String message);

}