package com.ssafy.mulryuproject.constants;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class RabbitMQConstants {
    public static final String EXCHANGE_NAME = "robot.exchange";
    public static final String QUEUE_NAME = "robot.queue";
    public static final String ROUTING_KEY = "robot.#";
}

