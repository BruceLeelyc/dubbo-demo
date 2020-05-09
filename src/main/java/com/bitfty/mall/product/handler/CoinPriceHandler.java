package com.bitfty.mall.product.handler;

import java.math.BigDecimal;

/**
 * @ClassName: CoinPriceHandler
 * @Description: 币价类
 * @Author: lzk
 * @Date: 2019/12/24 10:30
 */
public interface CoinPriceHandler {

    /**
     * 获取交易所的币价，
     * 传入CoinPrice枚举的类型
     */
    BigDecimal getPrice(String coinPriceType);
}
