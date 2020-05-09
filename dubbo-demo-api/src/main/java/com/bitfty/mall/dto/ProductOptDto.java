package com.bitfty.mall.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @program ProductOptDto
 * @description: 操作库存dto
 * @author: lixl
 * @create: 2019/12/04 14:13
 */
@Data
@Builder
public class ProductOptDto {

	/**
	 * 产品ID
	 */
	private String id;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 操作库存
	 */
	private int sellCount;
}
