package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderPayDetailDto implements Serializable {

    /**
     * 订单id
     * */
    private String id;

    /**
     * 购买数量
     * */
    private Integer buyCount;

    /**
     * 算力总量
     * */
    private BigDecimal totalPowerSize;

    /**
     * 算力费
     * */
    private BigDecimal prodAmount;

    /**
     * 电费缴纳周期
     * */
    private Integer days;

    /**
     * 电费/周期
     * */
    private BigDecimal electricFee;

    /**
     * 商品名称
     * */
    private String name;

    /**
     * btc汇率
     * */
    private BigDecimal btcExchangeRate;

    /**
     * btc金额
     * */
    private BigDecimal btcAmount;


}
