package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

public interface MulOrderService {

	// Create
	public MulOrder saveOrder(MulOrderDTO dto);
	
	// Read List
	public List<MulOrder> getOrderList();
	
	// Read List Status
	public List<MulOrder> getOrderStatusList(MulOrderStatus status);
	
	// Read One
	public Optional<MulOrder> getOrder(MulOrder order);
	
	// Update
	public MulOrder updateOrder(MulOrder order);
	
	// Delete
	public boolean deleteOrdertById(Integer id);
}
