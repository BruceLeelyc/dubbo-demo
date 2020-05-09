package com.bitfty.mall.dto;

import jdk.net.SocketFlow;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderCreateReturnDto implements Serializable {

    /**
     * 订单号
     * */
    private String id;

    /**
     * 套餐名称
     * */
    private String name;

    /**
     * 套餐图片
     * */
    private String pictureUrl;

    /**
     * 	算力大小
     * */
    private BigDecimal powerSize;

    /**
     * 算力费
     */
    private String prodAmount;

    /**
     * btc汇率
     * */
    private String btcExchangeRate;

    /**
     * btc金额
     * */
    private String btcAmount;

    /**
     * 算力单价
     * */
    private BigDecimal soldAmount;

    /**
     * 合计
     */
    private String amount;

    /**
     * 购买份额
     * */
    private Integer buyCount;

    /**
     * 电费缴纳天数
     */
    private Integer electricDays;

    /**
     * 电费
     */
    private String electricFee;

    /**
     * 订单状态
     * */
    private Integer status;

}
