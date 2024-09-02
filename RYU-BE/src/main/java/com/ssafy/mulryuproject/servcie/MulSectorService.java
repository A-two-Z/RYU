package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulSectorDTO;
import com.ssafy.mulryuproject.entity.MulSector;

public interface MulSectorService {
	// Create
	public MulSector saveSector(MulSectorDTO dto);
	
	// Read List
	public List<MulSector> getSectorList();
	
	// Read One
	public Optional<MulSector> getSector(MulSector sector);

	// Get Sector Id By Sector Name
	public Optional<MulSector> getSectorByName(String sectorName);

	// Update
	public MulSector updateSector(MulSector sector);
	
	// Delete
	public boolean deleteSectortById(Integer id);
}
