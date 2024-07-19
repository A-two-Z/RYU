package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulSectorDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulSectorRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulSectorServiceImpl implements MulSectorService {

	private final MulSectorRepo sectorRepo;
	
	@Override
	public MulSector saveSector(MulSectorDTO dto) {
		MulSector sector = MulSector.builder()
				.sectorName(dto.getSectorName())
                .build();
		return sectorRepo.save(sector);
	}

	@Override
	public List<MulSector> getSectorList() {
		// TODO Auto-generated method stub
		return sectorRepo.findAll();
	}

	@Override
	public Optional<MulSector> getSector(MulSector sector) {
		int id = sector.getSectorId();
		if(sectorRepo.existsById(sector.getSectorId())) 
			return sectorRepo.findById(id);
		else
			return null;
	}

	@Override
	public MulSector updateSector(MulSector sector) {

		if (sectorRepo.existsById(sector.getSectorId())) 
			return sectorRepo.save(sector);
		else
			return null;
	}

	@Override
	public boolean deleteSectortById(Integer id) {
		if (sectorRepo.existsById(id)) {
			sectorRepo.deleteById(id);
            return true;
        }
		
        return false;
	}

}
