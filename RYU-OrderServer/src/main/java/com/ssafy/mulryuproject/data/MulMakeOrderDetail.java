package com.ssafy.mulryuproject.data;

import lombok.Data;

@Data
public class MulMakeOrderDetail {
	
	private int productSectorId;
	
	private String sectorName;

	private String productName;
	
	private int orderQuantity;
}
