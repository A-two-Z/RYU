package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;

public interface MulProductSectorService {
	// Create
	public MulProductSector savePS(MulProductSectorDTO dto);
	
	// Read List
	public List<MulProductSector> getPSList();
	
	// Read One
	public Optional<MulProductSector> getPS(MulProductSector ps);
	
	// Product Id를 통해 section을 찾아오는 메소드
	public List<MulProductSector> getPSListToProduct(MulProduct product);
	
	// Update
	public MulProductSector updatePS(MulProductSector ps);
	
	// 수량 업데이트 하는 메소드
	public void updatePSList(List<MulProductSector> psList);
	
	// Delete
	public boolean deletePStById(Integer id);
}
