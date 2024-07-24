package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

import jakarta.transaction.Transactional;

public interface MulOrderRepo extends JpaRepository<MulOrder, Integer>{
	
	List<MulOrder> findByOrderStatus(MulOrderStatus status);
	
    @Modifying
    @Transactional
    @Query("update mul_order set order_status = :status where order_id = :id")
    void updateStatus(@Param("status") MulOrderStatus status, @Param("id") int id);

}
