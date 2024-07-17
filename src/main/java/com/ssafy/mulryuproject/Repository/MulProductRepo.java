package com.ssafy.mulryuproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulProduct;

public interface MulProductRepo extends JpaRepository<MulProduct, Integer>{
	
}
