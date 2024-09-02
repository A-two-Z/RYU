package com.ssafy.mulryuproject.servcie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ssafy.mulryuproject.data.MulSectorData;
import com.ssafy.mulryuproject.dto.MulProductSectorDTO;
import com.ssafy.mulryuproject.dto.MulSectorDTO;
import com.ssafy.mulryuproject.entity.MulMakeOrder;
import com.ssafy.mulryuproject.entity.MulOrder;
import com.ssafy.mulryuproject.entity.MulProduct;
import com.ssafy.mulryuproject.entity.MulProductSector;

public interface MulProductSectorService {
	// Create
	public MulProductSector savePS(MulProductSectorDTO dto);
	
	// Read List
	public List<MulProductSector> getPSList();
	
	// Read One
	public Optional<MulProductSector> getPS(MulProductSector ps);
	public Optional<MulProductSector> getPS(int psId);

	// Product Id를 통해 section을 찾아오는 메소드 ( & 오버 리딩)
	public List<MulProductSector> getPSListToProduct(MulProduct product);
	public List<MulProductSector> getPSListToProduct(int productId);

	// SectorName을 통해 어떤 물품이 몇 개 있는지 확인하는 메소드
	public Map<String, List<MulSectorData>> getListBySectorName(String SectorName);

	// Update
	public MulProductSector updatePS(MulProductSector ps);

	//Redis에 업데이트 하는 메소드
	public void updateQuantity(MulMakeOrder orders);

	// 수량 업데이트 하는 메소드(List 형식으로 되어있을 때 사용)
	public void updatePSQunatity(MulProductSector ps);
	// 데이터를 한 개씩 업데이트 할 때 사용(현재는 서버가 종료될 때 Redis에 저쟁되어 있던 quantity DB에 저장할 떄 사용)
	public void updatePSQunatity(int quantity, int id);

	// Delete
	public boolean deletePStById(Integer id);

}
