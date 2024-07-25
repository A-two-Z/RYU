package com.ssafy.mulryuproject.servcie;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

public interface MulMakeOrderService {

	// 로봇에게 전달할 Order
	MulMakeOrder makeOrder(MulOrderNumber orderNumber);

}