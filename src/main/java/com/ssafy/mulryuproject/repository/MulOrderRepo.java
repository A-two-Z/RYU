package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;

public interface MulOrderRepo extends JpaRepository<MulOrder, Integer>{
	
	List<MulOrder> findByOrderNumberId(MulOrderNumber order);

}
