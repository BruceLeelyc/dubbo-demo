package com.bitfty.mall.product;

import com.bitfty.enums.ProductType;
import com.bitfty.mall.enums.ProductOperate;
import com.bitfty.mall.product.handler.PowerHandler;
import com.bitfty.mall.product.handler.ProductHandler;
import com.bitfty.mall.utils.SpringContextUtil;

/**
 * @author zhangmi
 */
public class ProductFactory {

	public static ProductHandler handler(ProductType product) {
		ProductHandler handler = null;
		if (product.getType().equals(ProductType.POWER.getType())) {
			handler = SpringContextUtil.getBean(PowerHandler.class);
		}
		return handler;

	}

}
