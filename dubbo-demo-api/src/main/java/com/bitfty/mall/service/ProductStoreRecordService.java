package com.bitfty.mall.service;

import com.bitfty.mall.dto.ProductStoreRecordDto;
import com.bitfty.mall.enums.ProductStoreStatus;

import java.util.Date;
import java.util.List;

public interface ProductStoreRecordService {

    ProductStoreRecordDto findByOrderIdAndType(String id, Integer type);

	/**
	 * 查询处理中记录
	 * @param status
	 * @return
	 */
	List<ProductStoreRecordDto> findRecordByStatus(int status);

	/**
	 * 查询N分钟之前的处理中记录
	 * @param beforeDate 查询时间
	 * @return List<ProductStoreRecordDto> 处理中记录
	 */
	List<ProductStoreRecordDto> findHandleRecordByDate(Date beforeDate);

	/**
	 * 更新状态
	 * @param successList 订单id
	 * @param recordStatus 更新状态
	 * @return
	 */
	int updateRecordStatusByOrderId(List<String> successList, int recordStatus);

	/**
	 * 更新状态
	 * @param id id
	 * @param status 状态
	 */
	int updateRecordStatusById(String id, int status);

	/**
	 * 通过orderId和状态查询记录
	 * @param orderId 订单id
	 * @param recordStatus 记录状态
	 * @return dto
	 */
	ProductStoreRecordDto findByOrderIdAndStatus(String orderId, int recordStatus);

	/**
	 * 通过prodId查询处理中记录
	 * @param prodIdList 产品id列表
	 * @return List<String> 产品id
	 */
	List<String> findHandleByProdIds(List<String> prodIdList);
}
