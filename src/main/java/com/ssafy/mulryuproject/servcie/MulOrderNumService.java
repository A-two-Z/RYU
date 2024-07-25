package com.ssafy.mulryuproject.servcie;

import java.util.List;

import com.ssafy.mulryuproject.dto.MulOrderNumDTO;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

public interface MulOrderNumService {

	void toggleOrderStatus(MulOrderNumber orderNum);

	// Read List Status
	List<MulOrderNumber> getOrderStatusList(MulOrderStatus status);
	
	// create
	public MulOrderNumber saveOrderNum(MulOrderNumber mulOrderNumber);
	
	// Read List
	public List<MulOrderNumber> getOrderNumberList();
	
	// Read One By OrderNumber
	public MulOrderNumber getOrderNumber(MulOrderNumber orderNum);
	
}