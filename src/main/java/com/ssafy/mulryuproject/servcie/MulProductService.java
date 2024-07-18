package com.ssafy.mulryuproject.servcie;

import java.util.List;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;

public interface MulProductService {
	public MulProduct saveProductEntity(MulProductDTO dto);
	
	public List<MulProduct> getProductListEntity();
	
	public MulProduct getProductEntity(MulProduct product);
	
	public MulProduct updateProductEntity(MulProduct dto);
	
	public boolean deleteProductById(Integer id);
	
}
