package com.ssafy.mulryuproject.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssafy.mulryuproject.data.MulToRobotDetail;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Document(collection = "MulToRobot") // MongoDB에 연동하는 Enitity
public class MulToRobot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	private List<MulToRobotDetail> orders; // OrderDetails 리스트

	@Column(name="order_Date")
	private Date orderDate;
}
