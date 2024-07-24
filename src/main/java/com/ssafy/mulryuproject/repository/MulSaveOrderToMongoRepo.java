package com.ssafy.mulryuproject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ssafy.mulryuproject.entity.MulMakeOrder;

public interface MulSaveOrderToMongoRepo extends MongoRepository<MulMakeOrder, String>{

	@Query("{ 'createdDate' : { $gt: ?0 } }")
	public List<MulMakeOrder> getListAfterDate(Date date);
	
}
