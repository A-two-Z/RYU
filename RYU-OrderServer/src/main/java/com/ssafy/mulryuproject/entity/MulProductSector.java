package com.ssafy.mulryuproject.entity;

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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Mul_Product_Sector")
public class MulProductSector {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_sector_id")
	private int productSectorId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private MulProduct productId;

	@ManyToOne
	@JoinColumn(name = "sector_id")
	private MulSector sectorId;

	@Column(nullable = false, name = "quantity")
	private int quantity;

}
