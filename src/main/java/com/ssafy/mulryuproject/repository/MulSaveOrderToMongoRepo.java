package com.ssafy.mulryuproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.mulryuproject.entity.MulMakeOrder;

public interface MulSaveOrderToMongoRepo extends MongoRepository<MulMakeOrder, String>{

}
