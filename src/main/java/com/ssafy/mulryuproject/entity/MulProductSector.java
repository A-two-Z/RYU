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

@Entity
@Getter
@Builder
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
	private MulProduct product;

	@ManyToOne
    @JoinColumn(name = "sector_id")
	private MulSector sector;
	
	@Column(nullable = false, name = "quantity")
	private int quantity;

}
