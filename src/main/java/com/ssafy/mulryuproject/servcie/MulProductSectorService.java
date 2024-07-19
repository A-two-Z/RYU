package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.entity.MulProductSector;

public interface MulProductSectorService {
	// Create
	public MulProductSector savePS(MulProductSectorDTO dto);
	
	// Read List
	public List<MulProductSector> getPSList();
	
	// Read One
	public Optional<MulProductSector> getPS(MulProductSector ps);
	
	// Update
	public MulProductSector updatePS(MulProductSector ps);
	
	// Delete
	public boolean deletePStById(Integer id);
}
