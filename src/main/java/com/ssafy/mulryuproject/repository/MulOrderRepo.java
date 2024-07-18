package com.ssafy.mulryuproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulOrder;

public interface MulOrderRepo extends JpaRepository<MulOrder, Integer>{
}
