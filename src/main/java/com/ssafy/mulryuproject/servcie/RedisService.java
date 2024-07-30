package com.ssafy.mulryuproject.servcie;

public interface RedisService {

	void saveValue(String key, String value);

	String getValue(String key);

	String getProductName(int key);

	String getSectorName(int key);

}