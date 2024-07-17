package com.ssafy.mulryuproject.servcie;

import com.ssafy.mulryuproject.dto.MulProductDTO;
import com.ssafy.mulryuproject.entity.MulProduct;

public interface MulProductService {
	public MulProduct saveProductEntity(MulProductDTO dto);
}
