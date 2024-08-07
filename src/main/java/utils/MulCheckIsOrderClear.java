package utils;

import com.google.gson.JsonObject;
import com.ssafy.mulryuproject.controller.MulMongoData;
import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class MulCheckIsOrderClear {

    // 데이터 저장을 위한 HashSet
    private final static Set<String> IsOrderClear = Collections.newSetFromMap(new ConcurrentHashMap<>());
    // ScheduledExecutorService를 사용하여 데이터 만료를 스케줄링합니다
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // 각 데이터 항목의 만료 작업을 추적하기 위한 맵
    private final static Map<String, ScheduledFuture<?>> expirationTasks = new ConcurrentHashMap<>();

    private final MulSaveOrderToMongo mongo;
    private final MulProductSectorService productSector;

    // 데이터를 저장하고, TTL(시간 제한)을 설정하는 메서드
    public void orderNumberPut(String value) {
        // 데이터를 HashSet에 저장
        IsOrderClear.add(value);
        // TTL이 지나면 데이터 삭제 작업을 스케줄링합니다
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            // TTL이 지나면 데이터가 삭제되고 API 호출
            if (IsOrderClear.remove(value)) {
                MulMakeOrder order = mongo.findByOrderNumberId(value);
                for(MulMakeOrderDetail psId : order.getOrders()){
                    // ProductSector Redis에 저장한거 가져오고 quantity에 해당 order는 완료되지 않았다 판단하고 + 하기

                    // Order의 status를 다시 WAIT으로 조정

                }
            }
        }, 6, TimeUnit.HOURS); // 6시간 내로 데이터를 받아오지 못하면 Redis 다시 업데이트
        // 만료 작업을 추적하기 위한 ScheduledFuture 저장
        expirationTasks.put(value, future);
    }

    // 데이터를 조회하고 삭제하는 메서드
    public boolean orderNumberClear(String value) {
        // 데이터 조회
        boolean exists = IsOrderClear.remove(value);

        if (exists) {
            // 데이터가 삭제되면 만료 타이머를 취소
            expirationTasks.remove(value).cancel(false);
        }

        // Redis의 value값을 DB로 업데이트
        MulMakeOrder order = mongo.findByOrderNumberId(value);
        for(MulMakeOrderDetail psId : order.getOrders()){
            // Redis에 저장한 데이터 값 가져오고 DB에 quantity 업데이트 하기

        }


        return exists; // 조회된 데이터 존재 여부 반환
    }

}
