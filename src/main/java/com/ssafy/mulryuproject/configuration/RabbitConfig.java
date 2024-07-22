package com.ssafy.mulryuproject.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // Exchange, Queue, 그리고 Routing Key의 이름을 상수로 정의
	
	//메시지를 받는 역할을 하며, 메시지를 적절한 큐로 라우팅하는 규칙을 정의합니다.
    private static final String EXCHANGE_NAME = "robot.exchange";

    //실제 메시지가 저장되는 버퍼 역할을 합니다. 소비자(Consumer)는 이 큐로부터 메시지를 읽어 처리합니다.
    private static final String QUEUE_NAME = "robot.queue"; 
    
    //메시지가 특정 큐에 도달할 수 있도록 익스체인지와 큐를 연결하는 문자열입니다. 라우팅 키는 메시지를 큐에 전달하는 규칙을 정의합니다.
    private static final String ROUTING_KEY = "robot.#";

    /**
     * 큐(Queue) 정의
     * 
     * @return Queue 객체. 이름은 'robot.queue'이며, durable(내구성) 속성은 false로 설정됨.
     * durable이 false이면, RabbitMQ가 재시작되었을 때 큐가 삭제됩니다.
     */
    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    /**
     * 토픽 익스체인지(Topic Exchange) 정의
     * 
     * @return TopicExchange 객체. 이름은 'robot.exchange'.
     * TopicExchange는 메시지를 라우팅 키의 패턴을 기반으로 여러 큐에 라우팅할 수 있습니다.
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * 바인딩(Binding) 정의
     * 
     * 큐와 익스체인지를 라우팅 키를 통해 바인딩합니다.
     * 
     * @param queue Queue 객체. 위에서 정의한 큐가 주입됩니다.
     * @param exchange TopicExchange 객체. 위에서 정의한 익스체인지가 주입됩니다.
     * @return Binding 객체. 큐와 익스체인지를 'robot.#' 라우팅 키로 바인딩합니다.
     * 'robot.#'는 'robot.'으로 시작하는 모든 라우팅 키와 일치합니다.
     */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
