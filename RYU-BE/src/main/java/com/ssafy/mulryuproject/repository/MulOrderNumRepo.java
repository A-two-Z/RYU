package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

import jakarta.transaction.Transactional;

public interface MulOrderNumRepo  extends JpaRepository<MulOrderNumber, Integer>{

	List<MulOrderNumber> findByOrderStatus(MulOrderStatus status);
	
	@Modifying
	@Transactional
	@Query("update MulOrderNumber set orderStatus = :status where orderNumberId = :id")
	void updateStatus(@Param("status") MulOrderStatus status, @Param("id") int id);
	
	@Query("SELECT m FROM MulOrderNumber m WHERE m.orderNumber = :orderNumber")
	MulOrderNumber findByOrderNumber(@Param("orderNumber") String orderNumber);
	
	List<MulOrderNumber> findAllByOrderByOrderNumberIdDesc();


	
}
