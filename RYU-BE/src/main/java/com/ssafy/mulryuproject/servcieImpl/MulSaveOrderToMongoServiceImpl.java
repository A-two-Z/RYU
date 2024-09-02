package com.ssafy.mulryuproject.servcieImpl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.repository.MulSaveOrderToMongoRepo;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulSaveOrderToMongoServiceImpl implements MulSaveOrderToMongo {
	private final MulSaveOrderToMongoRepo mongoRepository;

	// MongoDB에 Robot으로 전달한 데이터를 백업
	public void saveRobotOrderToMongo(MulMakeOrder list) {
		mongoRepository.save(list);
	}

	// 특정 날짜 이후의 데이터를 모두 가져옴
	public List<MulMakeOrder> getOrdersAfter(Date date) {
		return mongoRepository.getListAfterDate(date);
	}

	// MongoDB에 가장 최근에 저장된 데이터를 백업
    public MulMakeOrder getLastData() {
        return mongoRepository.findFirstByOrderByOrderDateDesc();
    }

    // 특정 ID로 데이터를 가져오기
    public MulMakeOrder findByOrderNumberId(String id) {
        return mongoRepository.findById(id).orElse(null);
    }
	
	
}
