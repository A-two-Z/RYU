package com.ssafy.mulryuproject.servcieImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.dto.MulOrderNumDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.repository.MulOrderRepo;
import com.ssafy.mulryuproject.servcie.MulOrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulOrderServiceImpl implements MulOrderService {

	private final MulOrderRepo orderRepo;

	@Override
	public MulOrder saveOrder(MulOrder product) {
		return orderRepo.save(product);
	}

	@Override
	public List<MulOrder> getOrderList() {
		return orderRepo.findAll();
	}

	@Override
	public Optional<MulOrder> getOrder(MulOrder order) {
		int id = order.getOrderId();
		if (orderRepo.existsById(order.getOrderId()))
			return orderRepo.findById(id);
		else
			return null;
	}

	@Override
	public List<MulOrder> getOrderListByOrderNumberId(MulOrderNumber orderNumber) {
		
		List<MulOrder> list = orderRepo.findByOrderNumberId(orderNumber);
		
		return list;
	}
	
	@Override
	public MulOrder updateOrder(MulOrder order) {
		if (orderRepo.existsById(order.getOrderId()))
			return orderRepo.save(order);
		else
			return null;
	}


	@Override
	public boolean deleteOrdertById(Integer id) {
		if (orderRepo.existsById(id)) {
			orderRepo.deleteById(id);
			return true;
		}

		return false;
	}

}
