package com.ssafy.mulryuproject.entity;

import com.ssafy.mulryuproject.enums.MulOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Mul_Product_Sector")
public class MulOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_sector_id")
	private int productSectorId;

	@ManyToOne
    @JoinColumn(name = "product_id")
	private MulProduct product;

	@ManyToOne
    @JoinColumn(name = "robot_id")
	private MulRobot robotId;

	// 클라이언트 주문 번호
	@Column(nullable = false, name = "order_number")
	private int orderNumber;

	// 주문한 수량
	@Column(nullable = false, name = "order_Quantity")
	private int orderQuantity;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, name = "order_status")
	private MulOrderStatus orderStatus;
}
