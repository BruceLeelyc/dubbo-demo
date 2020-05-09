package com.bitfty.mall.service;

import java.math.BigDecimal;

/**
 * @ClassName: CoinService
 * @Description: 和币相关的公共类
 * @Author: lzk
 * @Date: 2019/12/16 15:52
 */
public interface CoinService {

    /**
     * 获取coinbase交易所，btc兑换美元的价格
     */
    BigDecimal getBtcToDollar();

    /**
     * 获取北京时间每日8点的币价(美元)
     */
    BigDecimal getCoinPriceEightClock(String currencyType);
}
