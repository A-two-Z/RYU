	package com.ssafy.mulryuproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.mulryuproject.servcieImpl.RedisService;

@SpringBootTest
class MulryuProjectApplicationTests {
	
	@Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        // Reset or initialize Redis state if needed
    }
	@Test
	void contextLoads() {
		 // Given
        String key = "testKey";
        String value = "testValue";

        // When
        redisService.saveValue(key, value);
        String retrievedValue = redisService.getValue(key);
        
        // Then
        assertEquals(value, retrievedValue, "The retrieved value should match the saved value.");
        
	}

}
