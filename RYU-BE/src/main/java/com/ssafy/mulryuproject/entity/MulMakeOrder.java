package com.ssafy.mulryuproject.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@Document(collection = "MulToRobot") // MongoDB에 연동하는 Enitity
public class MulMakeOrder {
	
	@Id
	private String orderNumber;
	
	private List<MulMakeOrderDetail> orders; // OrderDetails 리스트

	@CreatedDate
	private Date orderDate;
}
