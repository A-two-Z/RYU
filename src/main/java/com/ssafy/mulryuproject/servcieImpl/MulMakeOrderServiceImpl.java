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
import com.ssafy.mulryuproject.enums.MulOrderStatus;
import com.ssafy.mulryuproject.servcie.MulMakeOrderService;
import com.ssafy.mulryuproject.servcie.MulOrderNumService;
import com.ssafy.mulryuproject.servcie.MulOrderService;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulProductService;
import com.ssafy.mulryuproject.servcie.MulSectorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MulMakeOrderServiceImpl implements MulMakeOrderService {

	private final MulProductSectorService psService;

	private final MulSectorService sectorService;
	
	private final MulProductService productService;

	private final MulOrderService orderService;

	private final MulOrderNumService onService;
	
	// 로봇에게 전달할 Order
	@Override
	public MulMakeOrder makeOrder(MulOrderNumber orderNumber) {
		List<MulMakeOrderDetail> list = new ArrayList<>();
		
		MulOrderNumber orderNum = onService.getOrderNumber(orderNumber);
		
		List<MulOrder> orders = orderService.getOrderListByOrderNumberId(orderNum);
		
		// 만약 이미 배송이 DELIVER 된 제품이면 자체적으로 차단
		if(orderNum.getOrderStatus() == MulOrderStatus.DELIVER) {
			return null;
		}

		for(MulOrder order : orders) {
			MulProduct product = productService.getProduct(order).get();
			List<MulProductSector> sectors = psService.getPSListToProduct(product);
			MulProductSector sector = getSector(sectors, order.getOrderQuantity());
			
			if(sector==null) {
				// 0729 LHJ 남아있는 sector가 없으면 
			}
			
			MulMakeOrderDetail detail = createToRobot(order, product, sector);
			
			// 0724 LHJ productSector 테이블의 quantity를 업데이트 하는 기능의 시점
			updateQuantity(sector, order);
			
			list.add(detail);
		}
		
		// 0724 LHJ orderStatus를 Toggle 형식으로 바꿈
		onService.toggleOrderStatus(orderNum);
		
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
	private MulMakeOrderDetail createToRobot(MulOrder order, MulProduct product, MulProductSector sector) {

		Optional<MulSector> sectorOne = sectorService
				.getSector(MulSector.builder().sectorId(sector.getSectorId().getSectorId()).build());

		MulMakeOrderDetail robot = new MulMakeOrderDetail();
		
		robot.setProductName(product.getProductName());
		robot.setSectorName(sectorOne.get().getSectorName());
		robot.setOrderQuantity(order.getOrderQuantity());

		return robot;
	}
	
}
