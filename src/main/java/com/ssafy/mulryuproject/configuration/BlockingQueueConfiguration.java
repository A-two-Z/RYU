package com.ssafy.mulryuproject.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssafy.mulryuproject.entity.MulOrder;

@Configuration
public class BlockingQueueConfiguration {

	// 의존성 주입을 사용하여 큐의 인스턴스를 주입(일관성 유지)
	@Bean
	public BlockingQueue<MulOrder> mulRobotQueue(){
		return new ArrayBlockingQueue<>(100);
	}
	
	
}
