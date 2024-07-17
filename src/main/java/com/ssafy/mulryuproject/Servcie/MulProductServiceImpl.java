package com.ssafy.mulryuproject.Servcie;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.Repository.MulProductRepo;
import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulProductServiceImpl implements MulProductService {
	
	private final MulProductRepo productRepo;
	
	@Override
	public MulProduct saveProductEntity(MulProductDTO dto) {
		MulProduct product = MulProduct.builder()
                .productName(dto.getProductName())
                .build();

		
		return productRepo.save(product);
	}

}
