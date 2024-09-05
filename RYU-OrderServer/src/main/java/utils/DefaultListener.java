//package com.ssafy.mulryuproject.serializer;
//
//import java.util.LinkedHashMap;
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//import com.rabbitmq.client.Channel;
//import com.ssafy.mulryuproject.configuration.RabbitConfig;
//import com.ssafy.mulryuproject.constants.RabbitMQConstants;
//
//@Component  
//@RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)  
//public class DefaultListener {  
//  
//	@RabbitHandler  
//	public void receiveMessage(LinkedHashMap message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {  
//		try {  
//			System.out.println(message);  
//			channel.basicAck(tag, false);  
//		} catch (Exception e) {  
//			e.printStackTrace();  
//		}  
//	}  
//  
//}
package utils;

