package com.ssafy.mulryuproject.entity;

import java.util.Date;
import java.util.List;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString // Debug용
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Mul_order")
public class MulOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int orderId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private MulProduct productId;
	
	// 클라이언트 주문 번호
	@ManyToOne
	@JoinColumn(name = "order_number_id")
	private MulOrderNumber orderNumberId;
	
	// 주문한 수량
	@Column(nullable = false, name = "order_Quantity")
	private int orderQuantity;
	
	
}
