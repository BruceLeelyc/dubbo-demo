package com.bitfty.mall.service;

import com.bitfty.mall.dto.BuySnapshotDto;
import com.bitfty.util.Page;

import java.util.Date;
import java.util.List;

public interface BuySnapshotService {
	/**
	 * 查询
	 * @param productIds
	 * @return
	 */
	List<BuySnapshotDto> findSnapshotByProdIds(List<String> productIds);

	List<BuySnapshotDto> queryBuySnapshotByOrderId(String orderId);

	List<BuySnapshotDto> findByOrderIds(List<String> orderIds);

	List<BuySnapshotDto> findSnapshotByIds(List<String> snopIds);

    Page<BuySnapshotDto> findByProducTypeAndLucre(String uid, Integer productType, Integer type, Integer pageNo, Integer pageSize);

	BuySnapshotDto findByOrderId(String orderId);

	void updateOrderInfo(String orderId, String currencyType, Date realGainDate, Date realExpireDate);
}
