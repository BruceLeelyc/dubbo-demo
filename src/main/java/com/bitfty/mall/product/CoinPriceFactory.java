package com.bitfty.mall.product;

import com.bitfty.mall.enums.CoinPrice;
import com.bitfty.mall.product.handler.CoinBaseHandler;
import com.bitfty.mall.product.handler.CoinPriceHandler;
import com.bitfty.mall.product.handler.SochainHandler;
import com.bitfty.mall.utils.SpringContextUtil;

/**
 * @ClassName: CoinPriceFactory
 * @Description: 获取交易所的币价
 * @Author: lzk
 * @Date: 2019/12/24 10:29
 */
public class CoinPriceFactory {

    public static CoinPriceHandler handler(String coinType) {

        if (CoinPrice.COIN_BASE.getType().equals(coinType)) {
            return SpringContextUtil.getBean(CoinBaseHandler.class);
        }
        if (CoinPrice.SO_CHAIN.getType().equals(coinType)) {
            return SpringContextUtil.getBean(SochainHandler.class);
        }
        return null;

    }
}
