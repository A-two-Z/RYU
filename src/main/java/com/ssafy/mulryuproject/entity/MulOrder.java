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
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Mul_order")
public class MulOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private int orderId;

	@ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private MulProduct productId;

//	0719 LHJ 안쓰는 컬럼 주석 처리
//	@ManyToOne
//    @JoinColumn(name = "robot_id", referencedColumnName = "robot_id")
//	private MulRobot robotId;

	// 클라이언트 주문 번호
	@Column(nullable = false, name = "order_number", length = 255)
	private String orderNumber;

	// 주문한 수량
	@Column(nullable = false, name = "order_Quantity")
	private int orderQuantity;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, name = "order_status")
	private MulOrderStatus orderStatus;
}
