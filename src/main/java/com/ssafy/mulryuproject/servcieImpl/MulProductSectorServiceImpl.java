package com.ssafy.mulryuproject.servcieImpl;

import java.util.*;

import com.ssafy.mulryuproject.constants.RedisConstants;
import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.dto.MulSectorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ssafy.mulryuproject.data.MulMakeOrderDetail;
import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;
import com.ssafy.mulryuproject.entity.MulSector;
import com.ssafy.mulryuproject.repository.MulProductSectorRepo;
import com.ssafy.mulryuproject.servcie.MulProductSectorService;
import com.ssafy.mulryuproject.servcie.MulSectorService;

import lombok.RequiredArgsConstructor;

import static com.ssafy.mulryuproject.constants.logsConstants.REDIS_ORDERNUMBER;
import static com.ssafy.mulryuproject.constants.logsConstants.REDIS_UPDATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class MulProductSectorServiceImpl implements MulProductSectorService {
	private final MulProductSectorRepo psRepo;

	@Override
	public MulProductSector savePS(MulProductSectorDTO dto) {
		MulProductSector ps = MulProductSector.builder()
				.productId(MulProduct.builder().productId(dto.getProductId()).build())
				.sectorId(MulSector.builder().sectorId(dto.getSectorId()).build())
				.quantity(dto.getQuantity())
                .build();
		return psRepo.save(ps);
	}

	@Override
	public List<MulProductSector> getPSList() {
		// TODO Auto-generated method stub
		return 	psRepo.findAll();
	}

	@Override
	public Optional<MulProductSector> getPS(MulProductSector ps) {
		// TODO Auto-generated method stub
		int id = ps.getProductSectorId();
//		return psRepo.findById(id).orElseThrow(() -> new RuntimeException("오류!"));

		if(psRepo.existsById(ps.getProductSectorId()))
			return psRepo.findById(id);
		else
			return Optional.empty();
	}
	@Override
	public Optional<MulProductSector> getPS(int psId){
		return psRepo.findById(psId);
	}


	@Override
	public MulProductSector updatePS(MulProductSector ps) {
		if (psRepo.existsById(ps.getProductSectorId())) 
			return psRepo.save(ps);
		else
			return null;
	}

	private final MulSectorService sectorService;

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public Map<String, List<MulSectorData>> getListBySectorName(String SectorName){
		Optional<MulSector> sectorId = sectorService.getSectorByName(SectorName);
		List<MulProductSector> list = psRepo.findBySectorId(sectorId.get());

		Map<String, List<MulSectorData>> map  = new LinkedHashMap<>();

		for (MulProductSector ps : list) {
			String sectorName = ps.getSectorId().getSectorName();
			String productName = ps.getProductId().getProductName();

			String nowQuantity = redisTemplate.opsForValue().get(RedisConstants.PRODUCTSECTOR+ps.getProductSectorId());

			int quantity = Integer.parseInt(nowQuantity);

			map.computeIfAbsent(sectorName, k -> new ArrayList<>())
					.add(new MulSectorData(productName, quantity));
		}

		return map;
	}


	@Override
	public void updateQuantity(MulMakeOrder orders) {
		log.info(orders.getOrderNumber()+REDIS_ORDERNUMBER);
		for(MulMakeOrderDetail order : orders.getOrders()){
			// Redis의 Key값을 가져온다.
			String psId = RedisConstants.PRODUCTSECTOR + order.getProductSectorId();
			System.out.println("MulProductSectorServiceImpl의 updateQuantity 메소드: "+psId);
			// Redis에 저장된 Product Sector의 현재 수량을 가져온다.
			int nowQuantity = Integer.parseInt(
					redisTemplate.opsForValue().get(psId)
			);

			// nowQuantity(현재 수량) - 주문한 수량
			String quantity = Integer.toString(nowQuantity - order.getOrderQuantity());

			// redis에 업데이트
			redisTemplate.opsForValue().set(psId,quantity);
		}
		log.info(REDIS_UPDATE);
	}

	@Override
	public void updatePSQunatity(int quantity, int id) {
		psRepo.updateProductSectorQuantity(quantity, id);
	}

	@Override
	public boolean deletePStById(Integer id) {
		if (psRepo.existsById(id)) {
			psRepo.deleteById(id);
            return true;
        }
		
        return false;
	}

	@Override
	public List<MulProductSector> getPSListToProduct(int productId) {
		return psRepo.findByProductIdOrderBySectorId(MulProduct.builder().productId(productId).build());
	}
	
	@Override
	public List<MulProductSector> getPSListToProduct(MulProduct product) {
		return psRepo.findByProductIdOrderBySectorId(product);
	}

	@Override
	public void updatePSQunatity(MulProductSector ps) {
		psRepo.updateProductSectorQuantity(ps.getQuantity(), ps.getProductSectorId());
	}
	
}
