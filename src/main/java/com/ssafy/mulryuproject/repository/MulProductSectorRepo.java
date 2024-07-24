package com.ssafy.mulryuproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;

import jakarta.transaction.Transactional;

public interface MulProductSectorRepo extends JpaRepository<MulProductSector, Integer>{
	
	List<MulProductSector> findByProductIdOrderByQuantityDesc(MulProduct productId);
	
    @Modifying
    @Transactional
    @Query("update mul_product_sector set quantity = :newQuantity where product_sector_id = :id")
    void updateProductSectorQuantity(@Param("newQuantity") int newQuantity, @Param("id") int id);

}
