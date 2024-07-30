package com.ssafy.mulryuproject.servcie;

import org.springframework.data.mongodb.repository.Query;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

public interface MulMakeOrderService {

	// 로봇에게 전달할 Order
	MulMakeOrder makeOrder(MulOrderNumber orderNumber);

}