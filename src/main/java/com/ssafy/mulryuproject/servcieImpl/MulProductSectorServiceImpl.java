package com.ssafy.mulryuproject.servcieImpl;

import java.util.*;

import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.dto.MulSectorDTO;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulSectorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulProductSectorServiceImpl implements MulProductSectorService {
	private final MulProductSectorRepo psRepo;

	@Override
	public MulProductSector savePS(MulProductSectorDTO dto) {
		MulProductSector ps = MulProductSector.builder()
				.productId(MulProduct.builder().productId(dto.getProductId()).build())
				.sectorId(MulSector.builder().sectorId(dto.getSectorId()).build())
				.quantity(dto.getQuantity())
                .build();
		return psRepo.save(ps);
	}

	@Override
	public List<MulProductSector> getPSList() {
		// TODO Auto-generated method stub
		return 	psRepo.findAll();
	}

	@Override
	public Optional<MulProductSector> getPS(MulProductSector ps) {
		// TODO Auto-generated method stub
		int id = ps.getProductSectorId();
//		psRepo.findById(id).orElseThrow(() -> new RuntimeException("오류!"));

		if(psRepo.existsById(ps.getProductSectorId()))
			return psRepo.findById(id);
		else
			return Optional.empty();
	}

	@Override
	public MulProductSector updatePS(MulProductSector ps) {
		if (psRepo.existsById(ps.getProductSectorId())) 
			return psRepo.save(ps);
		else
			return null;
	}

	private final MulSectorService sectorService;

	@Override
	public Map<String, List<MulSectorData>> getListBySectorName(String SectorName){
		Optional<MulSector> sectorId = sectorService.getSectorByName(SectorName);
		List<MulProductSector> list = psRepo.findBySectorId(sectorId.get());

		Map<String, List<MulSectorData>> map  = new LinkedHashMap<>();

		for (MulProductSector ps : list) {
			String sectorName = ps.getSectorId().getSectorName();
			String productName = ps.getProductId().getProductName();
			int quantity = ps.getQuantity();

			map.computeIfAbsent(sectorName, k -> new ArrayList<>())
					.add(new MulSectorData(productName, quantity));
		}

		return map;
	}
	
	@Override
	public void updateQuantity(MulMakeOrder orders) {

		for(MulMakeOrderDetail detail : orders.getOrders()){
			Optional<MulProductSector> ps = psRepo.findById(detail.getProductSectorId());

			psRepo.updateProductSectorQuantity(detail.getProductSectorId(), ps.get().getQuantity() - detail.getOrderQuantity());
		}
	}


	@Override
	public boolean deletePStById(Integer id) {
		if (psRepo.existsById(id)) {
			psRepo.deleteById(id);
            return true;
        }
		
        return false;
	}

	@Override
	public List<MulProductSector> getPSListToProduct(int productId) {
		return psRepo.findByProductIdOrderBySectorId(MulProduct.builder().productId(productId).build());
	}
	
	@Override
	public List<MulProductSector> getPSListToProduct(MulProduct product) {
		return psRepo.findByProductIdOrderBySectorId(product);
	}

	@Override
	public void updatePSQunatity(MulProductSector ps) {
		psRepo.updateProductSectorQuantity(ps.getQuantity(), ps.getProductSectorId());
	}
	
}
