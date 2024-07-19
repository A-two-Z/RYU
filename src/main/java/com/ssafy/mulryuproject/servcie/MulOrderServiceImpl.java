package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

public class MulOrderServiceImpl implements MulOrderService {

	@Override
	public MulOrder saveOrder(MulOrderDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MulOrder> getOrderList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MulOrder> getOrderStatusList(MulOrderStatus stauts) {
		return null;
	}

	@Override
	public Optional<MulOrder> getOrder(MulOrder order) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public MulOrder updateOrder(MulOrder dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteOrdertById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

}
