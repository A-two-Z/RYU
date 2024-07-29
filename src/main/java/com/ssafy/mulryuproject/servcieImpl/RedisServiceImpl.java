package com.ssafy.mulryuproject.servcieImpl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.servcie.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
	public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
	public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    @Override
	public String getProductName(int key) {
        return redisTemplate.opsForValue().get("product_"+key);
    }
    
    @Override
	public String getSectorName(int key) {
        return redisTemplate.opsForValue().get("sector_"+key);
    }
    
    
    
}
