package com.ssafy.mulryuproject.servcie;

import java.util.List;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;

public interface MulMakeOrderService {

	// 로봇에게 전달할 Order
	MulMakeOrder makeOrder(List<MulOrder> orders);

}