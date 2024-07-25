package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.dto.MulOrderNumDTO;
import com.ssafy.mulryuproject.entity.MulOrder;

public interface MulOrderRepo extends JpaRepository<MulOrder, Integer>{
	
	List<MulOrder> findByOrderNumberId(MulOrderNumDTO num);

}
