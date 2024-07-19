package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulOrder;

public interface MulOrderService {

	// Create
	public MulOrder saveOrder(MulOrderDTO dto);
	
	// Read List
	public List<MulOrder> getOrderList();
	
	// Read One
	public Optional<MulOrder> getOrder(MulOrder order);
	
	// Update
	public MulOrder updateOrder(MulOrder dto);
	
	// Delete
	public boolean deleteOrdertById(Integer id);
}
