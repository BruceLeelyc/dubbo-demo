package com.bitfty.mall.eventbus.handler;

import com.bitfty.eventBus.EventBusHandler;
import com.bitfty.mall.dto.ProductStoreRecordDto;
import com.bitfty.mall.enums.OrderStatus;
import com.bitfty.mall.enums.ProductStoreStatus;
import com.bitfty.mall.eventbus.event.OrderEvent;
import com.bitfty.mall.service.ProductService;
import com.bitfty.mall.service.ProductStoreRecordService;
import com.bitfty.mall.service.ProductStoreRecordServiceImpl;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program ProductStoreHandler
 * @description: 产品库存
 * @author: lixl
 * @create: 2019/12/11 10:21
 */
@Component
public class ProductStoreHandler implements EventBusHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProductStoreHandler.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductStoreRecordService productStoreRecordService;

	@Subscribe
	public void productStoreOperate(OrderEvent orderEvent){
		if (null == orderEvent) {
			logger.info("库存监听订单消息为null");
			return;
		}

		Integer orderStatus = orderEvent.getStatus();
		if (!(OrderStatus.ORDER_PAY_SUCCESS.getStatus() == orderStatus || OrderStatus.ORDER_CANCEL.getStatus() == orderStatus)) {
			logger.info("库存监听订单状态不是最终状态,不处理.orderEvent={}", orderEvent);
			return;
		}

		String orderId = orderEvent.getOrderId();
		int recordStatus = ProductStoreStatus.HANDLE.getStatus();
		ProductStoreRecordDto recordDto = productStoreRecordService.findByOrderIdAndStatus(orderId, recordStatus);

		// 成功更新产品记录状态
		if (OrderStatus.ORDER_PAY_SUCCESS.getStatus() == orderStatus) {
			productStoreRecordService.updateRecordStatusById(recordDto.getId(), ProductStoreStatus.SUCCESS.getStatus());
			logger.info("订单成功,更新记录为成功orderId={}", orderId);
			return;
		}

		// 失败更新状态加回库存
		if (OrderStatus.ORDER_CANCEL.getStatus() == orderStatus) {
			// 回滚库存
			productService.increaseStock(recordDto.getProdId(), recordDto.getOrderId());
			logger.info("订单取消,回滚库存orderId={}", orderId);
		}
	}
}
