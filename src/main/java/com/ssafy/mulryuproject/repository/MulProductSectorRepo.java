package com.ssafy.mulryuproject.repository;

import java.util.List;

import com.ssafy.mulryuproject.entity.MulSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;

import jakarta.transaction.Transactional;

@Repository
public interface MulProductSectorRepo extends JpaRepository<MulProductSector, Integer>{
	
	List<MulProductSector> findByProductIdOrderBySectorId(MulProduct productId);
	
	@Modifying(clearAutomatically = true)
    @Transactional
    @Query("update MulProductSector m set m.quantity = :newQuantity where m.productSectorId = :id")
    void updateProductSectorQuantity(@Param("newQuantity") int newQuantity, @Param("id") int id);

	List<MulProductSector> findBySectorId(MulSector sectorId);
	
}
