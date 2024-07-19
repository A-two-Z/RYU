package com.ssafy.mulryuproject.servcie;

import java.util.List;

import com.ssafy.mulryuproject.entity.MulOrder;

public interface MulOrderService {

	public List<MulOrder> getOrderList();
	public MulOrder saveOrder();
	public List<MulOrder> submitOrder();
	public List<MulOrder> getWaitRobotList();
	public MulOrder updateOrder();
}
