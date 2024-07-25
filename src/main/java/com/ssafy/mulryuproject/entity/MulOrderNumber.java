package com.ssafy.mulryuproject.entity;

import org.hibernate.annotations.ColumnDefault;

import com.ssafy.mulryuproject.enums.MulOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString // Debug용
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Mul_order_number")
public class MulOrderNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_number_id")
	private int orderNumberId;

	@Column(nullable = false, unique = true, name = "order_number", length = 255)
	private String orderNumber;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, name = "order_status")
	@Builder.Default
	private MulOrderStatus orderStatus = MulOrderStatus.WAIT;
	
}
