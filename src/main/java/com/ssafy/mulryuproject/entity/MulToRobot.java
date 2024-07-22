package com.ssafy.mulryuproject.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;
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
	
	@Column(name="sector_name")
	private String sectorName;

	@Column(name="product_name")
	private String productName;
	
	@Column(name="order_quantity")
	private String orderQuantity;

	@Column(name="order_Date")
	private Date orderDate;
}
