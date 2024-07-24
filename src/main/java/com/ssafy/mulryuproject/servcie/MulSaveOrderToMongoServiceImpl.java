package com.ssafy.mulryuproject.servcie;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.repository.MulSaveOrderToMongoRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulSaveOrderToMongoServiceImpl implements MulSaveOrderToMongo {
	private final MulSaveOrderToMongoRepo MongoOrderRepository;

	// MongoDB에 Robot으로 전달한 데이터를 백업
	public void saveRobotOrderToMongo(MulMakeOrder list) {
		MongoOrderRepository.save(list);
	}

	// 특정 날짜 이후의 데이터를 모두 가져옴
	public List<MulMakeOrder> getOrdersAfter(Date date) {
		return MongoOrderRepository.getListAfterDate(date);
	}

	// MongoDB에 가장 최근에 저장된 데이터를 백업

}
