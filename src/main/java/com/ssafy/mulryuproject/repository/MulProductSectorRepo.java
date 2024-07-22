package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;

public interface MulProductSectorRepo extends JpaRepository<MulProductSector, Integer>{
	
	List<MulProductSector> findByProductIdOrderByQuantityDesc(MulProduct productId);
	
}
