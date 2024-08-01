package com.ssafy.mulryuproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mulryuproject.entity.MulSector;

import java.util.Optional;

public interface MulSectorRepo extends JpaRepository<MulSector, Integer>{
    Optional<MulSector> findBySectorName(String sectorName);
}
