package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExchangeRateDto implements Serializable {

    /**
     * 订单号
     * */
    private String id;

    /**
     * 基础货币
     * */
    private String base;

    /**
     * 兑换货币
     * */
    private String currency;

    /**
     * 兑换金额
     * */
    private BigDecimal amount;

    /**
     * 实际支付金额(基础货币)
     * */
    private BigDecimal baseAmount;

    /**
     * 比例（等价与多少美金）
     * */
    private BigDecimal proportion;


}
