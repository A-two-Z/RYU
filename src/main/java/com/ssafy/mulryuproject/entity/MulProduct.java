package com.ssafy.mulryuproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder // 주 생성자(또는 특정 필드를 초기화하는 생성자)를 생성하는 어노테이션
@NoArgsConstructor // 기본 생성자를 추가하는 어노테이션
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 생성
@Table(name = "Mul_Product")
public class MulProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_id")
	private int productId;
	
	@Column(nullable = false, name = "product_name", length = 255, unique = true)
	private String productName;
	
}
