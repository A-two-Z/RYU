package com.ssafy.mulryuproject.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "MulToRobot") // MongoDB에 연동하는 Enitity
public class MulToRobot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private String productId;
	private String productName;
	private String orderQuantity;
	private String productLocation;
	private boolean isDelivery;
	private Date productDate;
	
	
	
}
