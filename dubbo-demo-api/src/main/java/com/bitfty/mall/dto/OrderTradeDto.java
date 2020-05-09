package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderTradeDto implements Serializable {

    /**
     * id
     * */
    private String orderId;

    /**
     * 交易状态
     * */
    private Integer tradeStatus;

}
