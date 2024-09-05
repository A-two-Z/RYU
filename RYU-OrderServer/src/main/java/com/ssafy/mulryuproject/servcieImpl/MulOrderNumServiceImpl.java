package com.ssafy.mulryuproject.servcieImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.repository.MulOrderNumRepo;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulOrderNumServiceImpl implements MulOrderNumService {

	private final MulOrderNumRepo orderNumRepo;

	@Override
	public void toggleOrderStatus(MulOrderNumber orderNum) {
		orderNumRepo.updateStatus(
				(orderNum.getOrderStatus() == MulOrderStatus.DELIVERY ? MulOrderStatus.WAIT : MulOrderStatus.DELIVERY),
				orderNum.getOrderNumberId());
	}

	// Read List Status
	@Override
	public List<MulOrderNumber> getOrderStatusList(MulOrderStatus status) {
		return orderNumRepo.findByOrderStatus(status);
	}

	@Override
	public MulOrderNumber saveOrderNum(MulOrderNumber orderNum) {
		return orderNumRepo.save(orderNum);
	}

	
	@Override
	public List<MulOrderNumber> getOrderNumberList() {
		return orderNumRepo.findAllByOrderByOrderNumberIdDesc();
	}

	@Override
	public MulOrderNumber getOrderNumber(MulOrderNumber orderNum) {
		
		return orderNumRepo.findByOrderNumber(orderNum.getOrderNumber());
	}

}
