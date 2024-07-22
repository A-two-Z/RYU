package com.ssafy.mulryuproject.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MulToRobotDTO {
	private String sectorName;

	private String productName;
	
	private int orderQuantity;
}
