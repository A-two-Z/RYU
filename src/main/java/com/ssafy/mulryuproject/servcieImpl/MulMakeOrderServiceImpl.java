package com.ssafy.mulryuproject.servcieImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulOrderNumber;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.servcie.MulMakeOrderService;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulProductService;
import com.ssafy.mulryuproject.servcie.MulSectorService;
import com.ssafy.mulryuproject.servcie.RedisService;

import lombok.RequiredArgsConstructor;
import utils.ExceptionUtils;

@Service
@RequiredArgsConstructor
public class MulMakeOrderServiceImpl implements MulMakeOrderService {

	private final MulProductSectorService psService;

	// 0729 LHJ Redis를 사용하며 DB와 직접 연동하는 service는 주석처리
//	private final MulSectorService sectorService;
//	private final MulProductService productService;

	private final MulOrderService orderService;

	private final MulOrderNumService onService;
	
	private final RedisService redisService;
	
	// 로봇에게 전달할 Order
	@Override
	public MulMakeOrder makeOrder(MulOrderNumber orderNumber) {
		List<MulMakeOrderDetail> list = new ArrayList<>();
		
		MulOrderNumber orderNumId = onService.getOrderNumber(orderNumber);
		List<MulOrder> orders = orderService.getOrderListByOrderNumberId(orderNumId);
		
		// 만약 이미 배송이 DELIVER 된 제품이면 자체적으로 차단
		ExceptionUtils.throwIfDelivered(orderNumId.getOrderStatus());

		
		for(MulOrder order : orders) {
//			MulProduct product = productService.getProduct(order).get();
			int productId = order.getProductId().getProductId();
			String product = redisService.getProductName(productId);
			
			List<MulProductSector> sectors = psService.getPSListToProduct(productId);
			MulProductSector sector = getSector(sectors, order.getOrderQuantity());
			
			
			// 0729 LHJ 남아있는 sector가 없으면 오류 던짐
			ExceptionUtils.throwIfSectorIsNull(sector);
			
			
			MulMakeOrderDetail detail = createToRobot(order, product, sector);
			
			// 0724 LHJ productSector 테이블의 quantity를 업데이트 하는 기능의 시점
//			updateQuantity(sector, order);
			
			list.add(detail);
		}
		
		MulMakeOrder makedOrder = MulMakeOrder.builder()
				.orderNumber(orderNumber.getOrderNumber())
				.orders(list)
				.orderDate(new Date())
				.build();
		
		return makedOrder;
	}

	// ProductSector 테이블의 Quantity를 빼는 메소드
	// 이 클래스 내부에서만 사용된다.
	private void updateQuantity(MulProductSector sector, MulOrder order) {
		MulProductSector quantityUp = MulProductSector.builder()
				.productSectorId(sector.getProductSectorId())
				.quantity(
						sector.getQuantity() - order.getOrderQuantity()
						)
				.build();
		psService.updatePSQunatity(quantityUp);
	}
	
	// 0724 LHJ 
	// 요구하는 Quantity보다 더 많이 갖고 있는 sectors를 가져오는 메소드
	// 이 클래스 내부에서만 사용된다.
	private MulProductSector getSector(List<MulProductSector> sectors, int Quantity) {
		MulProductSector sector = null;
		
		for(MulProductSector getSector : sectors) {
			if(getSector.getQuantity() >= Quantity) {
				sector = getSector;
				break;
			}
		}
		
		return sector;
	}
	
	// 0723 LHJ 
	// {orderId: 1, product Name: "name", "sector Name": "sector", "orderQuantity" : 20} 하나를 만드는 메소드
	// 이 클래스 내부에서만 사용된다.
	private MulMakeOrderDetail createToRobot(MulOrder order, String productName, MulProductSector sector) {

		
		// MariaDB에서 받아옴
//		Optional<MulSector> sectorOne = sectorService
//				.getSector(MulSector.builder().sectorId(sector.getSectorId().getSectorId()).build());
		
		// Redis에서 받아옴
		String sectorName = redisService.getSectorName(sector.getSectorId().getSectorId());
		
		MulMakeOrderDetail robot = new MulMakeOrderDetail();
		
		robot.setProductSectorId(sector.getProductSectorId());
		robot.setProductName(productName);
		robot.setSectorName("S201"); // 임시 섹터 이름
		robot.setOrderQuantity(order.getOrderQuantity());

		return robot;
	}
	
}
