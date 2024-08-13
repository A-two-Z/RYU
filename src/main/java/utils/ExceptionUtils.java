package utils;

import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExceptionUtils {

    // 이미 배송된 제품에 대한 예외를 던지는 메서드
    public static void throwIfDelivered(MulOrderStatus orderStatus) {
        if (orderStatus == MulOrderStatus.DELIVERY) {
            throw new IllegalStateException("이 제품은 이미 배송 된 제품입니다.");
        }
    }

    // 다른 예외 처리 메서드를 추가할 수 있습니다.
    public static void throwIfSectorIsNull(MulProductSector sector) {
        if (sector == null) {
            throw new IllegalStateException("모든 섹터에서 물품이 존재하지 않습니다.");
        }
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleMemberException(ProductSectorException e) {
//        log.debug("[MemberException] : {} is occurred", e.getErrorCode());
//        return ErrorResponse.toResponseEntity(e.getErrorCode());
//    }

}

