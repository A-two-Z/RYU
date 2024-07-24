package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulOrderDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.repository.MulOrderRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulOrderServiceImpl implements MulOrderService {

	private final MulOrderRepo orderRepo;
	
	@Override
	public MulOrder saveOrder(MulOrderDTO dto) {
		MulOrder product = MulOrder.builder()
				.orderNumber(dto.getOrderNumber())
				.orderQuantity(dto.getOrderQuantity())
				.orderStatus(MulOrderStatus.WAIT)
                .build();
		return orderRepo.save(product);
	}

	@Override
	public List<MulOrder> getOrderList() {
		return 	orderRepo.findAll();
	}

	@Override
	public List<MulOrder> getOrderStatusList(MulOrderStatus status) {
		return orderRepo.findByOrderStatus(status);
	}

	@Override
	public Optional<MulOrder> getOrder(MulOrder order) {
		int id = order.getOrderId();
		if(orderRepo.existsById(order.getOrderId())) 
			return orderRepo.findById(id);
		else
			return null;		
	}

	@Override
	public MulOrder updateOrder(MulOrder order) {
		if (orderRepo.existsById(order.getOrderId())) 
			return orderRepo.save(order);
		else
			return null;
	}
	
	@Override
	public void toggleOrderStatus(MulOrder order) {		
		orderRepo.updateStatus(
				order.getOrderStatus() == MulOrderStatus.DELIVER ?
						MulOrderStatus.WAIT : MulOrderStatus.DELIVER
						, order.getOrderId());
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
