package utils;

import com.google.gson.JsonObject;
import com.ssafy.mulryuproject.constants.RedisConstants;
import com.ssafy.mulryuproject.controller.MulMongoData;
import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulSaveOrderToMongo;
import com.ssafy.mulryuproject.servcie.RedisService;
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
    private final RedisService redisService;
    private final MulOrderNumService orderNumService;

    // 데이터를 저장하고, TTL(시간 제한)을 설정하는 메서드
    public void orderNumberPut(String orderNumber) {
        // 데이터를 HashSet에 저장
        IsOrderClear.add(orderNumber);
        // TTL이 지나면 데이터 삭제 작업을 스케줄링합니다
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            // TTL이 지나면 데이터가 삭제되고 API 호출
            if (IsOrderClear.remove(orderNumber)) {
                MulMakeOrder order = mongo.findByOrderNumberId(orderNumber);
                for(MulMakeOrderDetail psId : order.getOrders()){
                    falseRobot(orderNumber, psId);
                }
            }
        }, 6, TimeUnit.HOURS); // 6시간 내로 데이터를 받아오지 못하면 Redis 다시 업데이트
        // 만료 작업을 추적하기 위한 ScheduledFuture 저장
        expirationTasks.put(orderNumber, future);
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

    // 0806 LHJ 이 클래스에서만 사용되는 메소드
    // 일정시간 이내로 Robot으로부터 clear 신호가 넘어오지 않았기 때문에
    // Robot이 정상적인 작동을 하지 않았던 것으로 판단하고 productSector의 물건수량 값을 다시 올린다
    private void falseRobot(String orderNumber, MulMakeOrderDetail psId){

        // ProductSector Redis에 저장한거 가져오고 quantity에 해당 order는 완료되지 않았다 판단하고 + 하기
        int psQuantity = Integer.parseInt(
                redisService.getProductSector(
                        psId.getProductSectorId()
                )
        );

        String finalQuantity = Integer.toString(psQuantity + psId.getOrderQuantity());
        redisService.saveValue(RedisConstants.PRODUCTSECTOR, finalQuantity);

        // Order의 status를 다시 WAIT으로 조정
        MulOrderNumber orderNumberId = orderNumService.getOrderNumber(MulOrderNumber.builder().orderNumber(orderNumber).build());
        orderNumService.toggleOrderStatus(orderNumberId);

    }

}
