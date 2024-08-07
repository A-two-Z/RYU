package com.ssafy.mulryuproject;
import java.util.List;

import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulProductRepo;
import com.ssafy.mulryuproject.repository.MulSectorRepo;

import jakarta.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MulProductRepo productRepository;
    @Autowired
    private MulSectorRepo sectorRepository;
    @Autowired
    private MulProductSectorRepo productSectorRepo;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // MulProduct 테이블의 모든 데이터를 Redis에 저장
        List<MulProduct> products = productRepository.findAll();
        for (MulProduct product : products) {
            redisTemplate.opsForValue().set("product_" + product.getProductId(), product.getProductName());
        }

        // MulSelect 테이블의 모든 데이터를 Redis에 저장
        List<MulSector> sectors = sectorRepository.findAll();
        for (MulSector sector : sectors) {
            redisTemplate.opsForValue().set("sector_" + sector.getSectorId(), sector.getSectorName());
        }

        // MulProductSector 테이블의 모든 데이터를 Redis에 저장
        List<MulProductSector> productSectors = productSectorRepo.findAll();
        for(MulProductSector ps : productSectors){
            String psId = "productSector_"+ps.getProductSectorId();
            String quantity = Integer.toString(ps.getQuantity());

            redisTemplate.opsForValue().set(psId,quantity);
        }

    }
}
