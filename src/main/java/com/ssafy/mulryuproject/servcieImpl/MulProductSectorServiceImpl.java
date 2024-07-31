package com.ssafy.mulryuproject.servcieImpl;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.entity.*;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;

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

	@Override
	public void updateQuantity(MulMakeOrder orders) {

		for(MulMakeOrderDetail detail : orders.getOrders()){
			Optional<MulProductSector> ps = psRepo.findById(detail.getProductSectorId());

			MulProductSector quantityUp = MulProductSector.builder()
					.productSectorId(detail.getProductSectorId())
					.quantity(
							ps.get().getQuantity() - detail.getOrderQuantity()
					)
			.build();

			psRepo.save(quantityUp);
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
