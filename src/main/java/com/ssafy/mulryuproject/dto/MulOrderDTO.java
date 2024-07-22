package com.ssafy.mulryuproject.dto;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.enums.MulOrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulOrderDTO {
	private int productId;
//	private MulRobot robotId;
	private String orderNumber;
	private int orderQuantity;
	private MulOrderStatus orderStatus;
}
