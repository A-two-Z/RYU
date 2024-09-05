package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Optional;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;

public interface MulProductService {
	public MulProduct saveProduct(MulProductDTO dto);
	
	public List<MulProduct> getProductList();
	
	public Optional<MulProduct> getProduct(MulProduct product);
	
	public Optional<MulProduct> getProduct(MulOrder order);
	
	public MulProduct updateProduct(MulProduct product);
	
	public boolean deleteProductById(Integer id);
	
}
