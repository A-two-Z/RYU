package com.ssafy.mulryuproject.servcie;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.repository.MulProductRepo;

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

	@Override
	public List<MulProduct> readProductEntity() {
		return productRepo.findAll();
	}

	@Override
	public MulProduct updateProductEntity(MulProductDTO dto) {
		MulProduct product = MulProduct.builder()
				.productName(dto.getProductName())
				.build();
		return productRepo.save(product);
	}

	@Override
	public boolean deleteProductById(Integer id) {
		// 0718 LHJ 만약 ID가 있으면 해당 ID 삭제
		if (productRepo.existsById(id)) {
			productRepo.deleteById(id);
            return true;
        }
		
        return false;
	}

}
