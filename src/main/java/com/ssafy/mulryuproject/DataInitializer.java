package com.ssafy.mulryuproject;
import java.util.*;

import com.ssafy.mulryuproject.constants.RedisConstants;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulProductRepo;
import com.ssafy.mulryuproject.repository.MulSectorRepo;

import jakarta.transaction.Transactional;

// 서버가 실행될 때, 1시간 단위로 업데이트, 서버가 종료될 때 실행되는 메소드를 다루는 클래스입니다.

@Component
@Slf4j
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
    private RedisTemplate<String, String> redisTemplate; // IntelliJ를 사용할 시 Error표시가 날 수 있으나, 정상 작동합니다.

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // MulProduct 테이블의 모든 데이터를 Redis에 저장
        List<MulProduct> products = productRepository.findAll();
        checkDataConsistency(RedisConstants.PRODUCT, products);


        // MulSelect 테이블의 모든 데이터를 Redis에 저장
        List<MulSector> sectors = sectorRepository.findAll();
        checkDataConsistency(RedisConstants.SECTOR, sectors);


        // MulProductSector 테이블의 모든 데이터를 Redis에 저장
        List<MulProductSector> productSectors = productSectorRepo.findAll();
        checkDataConsistency(RedisConstants.PRODUCTSECTOR, productSectors);

        log.info("Redis is ready");

    }


    private void checkDataConsistency(String keyPattern, List<?> DbData) {
        try {
            // Redis에서 모든 데이터 추출
            Map<String, String> redisData = getRedisData(keyPattern + "*");

            // 데이터베이스에서 모든 데이터 추출
            Map<String, String> dbData = getDbData(DbData);

            // 데이터 비교 및 불일치 처리
            compareAndLogDiscrepancies(redisData, dbData);

        } catch (Exception e) {
            System.err.println("Error occurred while checking data consistency: " + e.getMessage());
        }
    }

    private Map<String, String> getRedisData(String keyPattern) {
        Map<String, String> data = new HashMap<>();
        redisTemplate.keys(keyPattern).forEach(key -> {
            data.put(key, redisTemplate.opsForValue().get(key));
        });
        return data;
    }

    private Map<String, String> getDbData(List<?> dbEntities) {
        Map<String, String> data = new HashMap<>();
        for (Object entity : dbEntities) {
            if (entity instanceof MulProduct) {
                MulProduct product = (MulProduct) entity;
                data.put(RedisConstants.PRODUCT + product.getProductId(), product.getProductName());
            } else if (entity instanceof MulSector) {
                MulSector sector = (MulSector) entity;
                data.put(RedisConstants.SECTOR + sector.getSectorId(), sector.getSectorName());
            } else if (entity instanceof MulProductSector) {
                MulProductSector ps = (MulProductSector) entity;
                data.put(RedisConstants.PRODUCTSECTOR + ps.getProductSectorId(), Integer.toString(ps.getQuantity()));
            }
        }
        return data;
    }

    private void compareAndLogDiscrepancies(Map<String, String> redisData, Map<String, String> dbData) {
        redisData.forEach((key, redisValue) -> {
            String dbValue = dbData.get(key);
            if (dbValue == null) {
                log.error(dbValue+"가 없습니다. dbValue를 확인해주세요." );

            } else if (!redisValue.equals(dbValue)) {
                log.warn("Redis와 DB의 값이 다릅니다. 확인해주세요. key: " + key +
                        ". Redis value: " + redisValue + ", DB value: " + dbValue);
            }
        });

        dbData.forEach((key, dbValue) -> {
            if (!redisData.containsKey(key)) {
                redisTemplate.opsForValue().set(key, dbValue); //만약 null 일 때만 redis에 set 함
                log.warn(key + " 가 없습니다. 해당 키 값을 Redis에 생성합니다.");
            }
        });



    }

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void saveDBForOneHour(){
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
