package com.ssafy.mulryuproject.servcieImpl;

import com.ssafy.mulryuproject.constants.RedisConstants;
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
        return redisTemplate.opsForValue().get(RedisConstants.PRODUCT +key);
    }
    
    @Override
	public String getSectorName(int key) {
        return redisTemplate.opsForValue().get(RedisConstants.SECTOR+key);
    }

    @Override
    public String getProductSector(int key) {
        return redisTemplate.opsForValue().get(RedisConstants.PRODUCTSECTOR+key);
    }


}
