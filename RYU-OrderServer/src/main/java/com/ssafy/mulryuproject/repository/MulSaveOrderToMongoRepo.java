package com.ssafy.mulryuproject.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.ssafy.mulryuproject.entity.MulMakeOrder;

public interface MulSaveOrderToMongoRepo extends MongoRepository<MulMakeOrder, String> {

	@Query("{ 'orderDate' : { $gt: ?0 } }")
	public List<MulMakeOrder> getListAfterDate(Date date);

	@Query(sort = "{ 'orderDate' : -1 }")
	MulMakeOrder findFirstByOrderByOrderDateDesc();
	
	MulMakeOrder findByOrderNumber(String orderNumber);
}
