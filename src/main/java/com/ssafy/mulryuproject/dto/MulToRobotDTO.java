package com.ssafy.mulryuproject.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulToRobotDTO {
	private String sectorName;

	private String productName;
	
	private String orderQuantity;

	private Date orderDate;
}
