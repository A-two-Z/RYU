package com.ssafy.mulryuproject.dto;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulProductSectorDTO {
	private int product;

	private int sector;
	
	private int quantity;
}
