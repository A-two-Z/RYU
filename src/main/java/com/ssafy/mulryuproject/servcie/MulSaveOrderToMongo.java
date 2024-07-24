package com.ssafy.mulryuproject.servcie;

import java.util.Date;
import java.util.List;

import com.ssafy.mulryuproject.entity.MulMakeOrder;

public interface MulSaveOrderToMongo {
	public void saveRobotOrderToMongo(MulMakeOrder list);
	public List<MulMakeOrder> getOrdersAfter(Date date);
}