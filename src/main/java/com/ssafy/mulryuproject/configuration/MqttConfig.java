package com.ssafy.mulryuproject.configuration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MqttConfig {
//	private final String TOPIC_NAME = "1"; // 임시 토픽.
	private final String USER_NAME = "TestName";
	private final String[] SERVER_URI = {"tcp://39.115.5.187:1883"};
    // 배열을 사용함으로써 클라이언트가 하나 이상의 MQTT 브로커에 연결을 시도할 수 있다.
	
	private final String SERVER_ID = "serverIn";
	private final String SERVER_OUT = "serverOut";
	
	@Bean // MQTT 클라이언트를 위한 설정을 정의하는 빈
	public MqttPahoClientFactory mqttClientFactory() {
		// 기본 MQTT Paho 클라이언트 팩토리를 생성
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		
		// MQTT 연결 옵션을 설정
	    MqttConnectOptions options = new MqttConnectOptions();
	    
	    options.setServerURIs(SERVER_URI); // 서버 URI 설정
	    
	    options.setUserName(USER_NAME); // 사용자 이름 설정
	    options.setCleanSession(true); // 클린 세션 설정

	    // 생성된 옵션을 팩토리에 설정
	    factory.setConnectionOptions(options);

	    // 팩토리를 반환하여 빈으로 등록
	    return factory;
	}
	
	// MQTT 메시지를 받을 채널을 정의하는 빈
	@Bean
	public MessageChannel InputTestChannel() {
	    // 직접 채널을 생성하여 반환
	    return new DirectChannel();
	}

	// MQTT 메시지를 수신하는 프로듀서를 정의하는 빈
	@Bean
	public MessageProducer inbound() {
	    // MQTT Paho 메시지 드리븐 채널 어댑터를 생성
	    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(SERVER_ID, mqttClientFactory(), "#");
	    adapter.setCompletionTimeout(5000); // 완료 타임아웃 설정
	    adapter.setConverter(new DefaultPahoMessageConverter()); // 메시지 변환기 설정
	    adapter.setQos(2); // QoS-2 설정: 메시지가 정확히 한 번 전달되는 것을 보장
	    adapter.setOutputChannel(InputTestChannel()); // 출력 채널 설정

	    // 어댑터를 반환하여 빈으로 등록
	    return adapter;
	}
	
	@Bean
    @ServiceActivator(inputChannel = "InputTestChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
            if (topic.equals("1")) {
                System.out.println("Received message: " + message.getPayload());
                // Add your message processing logic here
            }
        };
    }

	// MQTT 메시지를 보낼 채널을 정의하는 빈
	@Bean
	public MessageChannel mqttOutboundChannel() {
	    // 직접 채널을 생성하여 반환
	    return new DirectChannel();
	}

	// MQTT 메시지를 보내는 핸들러를 정의하는 빈
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
	    // MQTT Paho 메시지 핸들러를 생성
	    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(SERVER_OUT, mqttClientFactory());
	    messageHandler.setAsync(true); // 비동기 설정
	    messageHandler.setDefaultTopic("#"); // 기본 토픽 설정
	    messageHandler.setDefaultRetained(false); // 기본 리텐션 설정

	    // 메시지 핸들러를 반환하여 빈으로 등록
	    return messageHandler;
	}
}
