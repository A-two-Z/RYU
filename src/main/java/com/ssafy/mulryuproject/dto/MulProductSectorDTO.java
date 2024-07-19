package com.ssafy.mulryuproject.dto;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulProductSectorDTO {
	private MulProduct product;

	private MulSector sector;
	
	private int quantity;
}
