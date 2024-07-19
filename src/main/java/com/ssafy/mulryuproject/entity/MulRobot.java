package com.ssafy.mulryuproject.entity;


import com.ssafy.mulryuproject.enums.MulRobotStatus;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder // 주 생성자(또는 특정 필드를 초기화하는 생성자)를 생성하는 어노테이션
@NoArgsConstructor // 기본 생성자를 추가하는 어노테이션
@AllArgsConstructor // 모든 필드를 초기화하는 생성자 생성
@Table(name = "Mul_Robot")
public class MulRobot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="robot_id")
	private int robotId;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, name = "robot_status")
	private MulRobotStatus robotStatus;
	
}


