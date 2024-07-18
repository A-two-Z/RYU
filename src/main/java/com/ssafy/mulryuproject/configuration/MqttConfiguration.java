package com.ssafy.mulryuproject.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.google.gson.Gson;
import com.ssafy.mulryuproject.entity.MulRobot;
import com.ssafy.mulryuproject.servcie.MulRobotService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MqttConfiguration {
	private final String TOPIC_NAME = "mytopic";
	private final String USER_NAME = "sub_client";
	private final String SERVER_URI = "tcp://localhost:1883";
	private final String SERVER_ID = "serverIn";
	private final String SERVER_OUT = "serverOut";
	
	@Autowired
	private final MulRobotService service;
	
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		
		MqttConnectOptions options = new MqttConnectOptions();
		
		options.setServerURIs(new String[] {SERVER_URI});
		options.setUserName(USER_NAME);
		options.setCleanSession(true);
		
		factory.setConnectionOptions(options);
		
		return factory;
	}
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(SERVER_ID,
				mqttClientFactory(), "#");

		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}
	
	
	// Pub에서 전달한 데이터 출력
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
				System.out.println(message.toString());
				
				if(topic.equals(TOPIC_NAME)) {
					System.out.println("This is the topic");
				}
				
				Gson gson = new Gson();
		        MulRobot pack = gson.fromJson(message.getPayload().toString(), MulRobot.class);
		        System.out.println(pack.toString());
		        
		        //packService.create(pack);
		        
				System.out.println(message.getPayload());
			}

		};
	}
	
	
	@Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
	@Bean
    @ServiceActivator(inputChannel =  "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(SERVER_OUT, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }
}
