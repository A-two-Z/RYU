package com.ssafy.mulryuproject.constants;

public class logsConstants {

    // MulCheckIsOrderClear 메소드에서 사용되는 logs
    public static final String IS_DEIVERY = ": 정상적으로 배달되었습니다.";
    public static final String WARN_ORDERNUMBER_NOMATCH = "정상적인 번호가 아닙니다. 번호를 확인해주세요.";
    public static final String ERROR_ORDERNUMBER_NODEIVERY = ": 배달되지 않았습니다. 현장 확인이 필요합니다.";

    // MulConnToRobotCon
    public static final String MULSTATUS = ": 정상적으로 배달되었습니다.";

    // MulProductSectorServiceImpl
    public static final String REDIS_UPDATE = "Redis에서 Quantity 업데이트";

    public static final String ORDERDATASAVE = "MulOrderCon에서 데이터 정상적으로 저장됨";
    public static final String REDIS_ORDERNUMBER = ": 주문번호가 처리되어 Redis에서 업데이트";
}
