package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

public interface MulOrderService {

	// Create
	public MulOrder saveOrder(MulOrder product );
	
	// Read List
	public List<MulOrder> getOrderList();

	// Read One
	public Optional<MulOrder> getOrder(MulOrder order);
	
	// Read By OrderNumber
	public List<MulOrder> getOrderListByOrderNumberId(MulOrderNumber orderNumber);
	
	// Update
	public MulOrder updateOrder(MulOrder order);
	
	// Delete
	public boolean deleteOrdertById(Integer id);
}
