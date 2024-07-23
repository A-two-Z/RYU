package com.ssafy.mulryuproject.servcie;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.repository.MulSaveOrderToMongoRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulSaveOrderToMongoServiceImpl implements MulSaveOrderToMongo {
	private final MulSaveOrderToMongoRepo makeOrderRepository;
	
	// MongoDB에 Robot으로 전달한 데이터를 백업
	public void saveRobotOrderToMongo(MulMakeOrder list) {
		makeOrderRepository.save(list);
	}
}
