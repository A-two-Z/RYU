package com.ssafy.mulryuproject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ssafy.mulryuproject.constants.RedisConstants;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulProductRepo;
import com.ssafy.mulryuproject.repository.MulSectorRepo;

import jakarta.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    @Autowired
    private MulProductRepo productRepository;
    @Autowired
    private MulSectorRepo sectorRepository;
    @Autowired
    private MulProductSectorRepo productSectorRepo;
    @Autowired
    private MulProductSectorService productSectorService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // MulProduct 테이블의 모든 데이터를 Redis에 저장
        List<MulProduct> products = productRepository.findAll();
        for (MulProduct product : products) {
            redisTemplate.opsForValue().set(RedisConstants.PRODUCT + product.getProductId(), product.getProductName());
        }

        // MulSelect 테이블의 모든 데이터를 Redis에 저장
        List<MulSector> sectors = sectorRepository.findAll();
        for (MulSector sector : sectors) {
            redisTemplate.opsForValue().set(RedisConstants.SECTOR + sector.getSectorId(), sector.getSectorName());
        }

        // MulProductSector 테이블의 모든 데이터를 Redis에 저장
        List<MulProductSector> productSectors = productSectorRepo.findAll();
        for(MulProductSector ps : productSectors){
            String psId = RedisConstants.PRODUCTSECTOR+ps.getProductSectorId();
            String quantity = Integer.toString(ps.getQuantity());

            redisTemplate.opsForValue().set(psId,quantity);
        }

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 종료 전에 Redis에 저장되어 있었던 모든 Quantity 저장

        Set<String> keys = redisTemplate.keys(RedisConstants.PRODUCTSECTOR+"*");

        if (keys != null) {
            for (String key : keys) {
                int quantity = Integer.parseInt(
                        redisTemplate.opsForValue().get(key)
                );
                int processedKey = Integer.parseInt(
                        key.substring(RedisConstants.PRODUCTSECTOR.length())
                );
                Optional<MulProductSector> sec = productSectorService.getPS(processedKey);
                productSectorService.updatePSQunatity(quantity, processedKey);
            }
        }

    }
}
