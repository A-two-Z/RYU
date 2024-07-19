package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.repository.MulProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulProductServiceImpl implements MulProductService {
	
	private final MulProductRepo productRepo;
	
	// 0718 LHJ product를 저장하는 메소드
	@Override
	public MulProduct saveProduct(MulProductDTO dto) {
		MulProduct product = MulProduct.builder()
                .productName(dto.getProductName())
                .build();
		return productRepo.save(product);
	}

	// 0718 LHJ product의 전체 리스트를 가지고 오는 메소드
	@Override
	public List<MulProduct> getProductList() {
		return productRepo.findAll();
	}
	
	// 0718 LHJ product 테이블 중 id가 일치하는 하나의 데이터를 가져오는 메소드
	@Override
	public Optional<MulProduct> getProduct(MulProduct product) {
		int id = product.getProductId();
		if(id != 0) // product의 인덱스는 1로 시작하기 때문에 0일 수 없음.
			return productRepo.findById(id);
		else
			return null;
	}

	// 0718 LHJ product를 업데이트하는 메소드
	@Override
	public MulProduct updateProduct(MulProduct product) {
		
		if (productRepo.existsById(product.getProductId())) 
			return productRepo.save(product);
		else
			return null;
	}

	// 0718 LHJ product를 삭제하는 메소드
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
