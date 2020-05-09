package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDto implements Serializable {

    private String id;

    private String orderNo;

    private Long userId;

    private BigDecimal amount;

    private BigDecimal couponAmount;

    private BigDecimal serviceFee;

    private BigDecimal prodAmount;

    private BigDecimal electricFee;

    private String memo;

    private Integer status;

    private Date createAt;

    private Date updateAt;

    private String exchangeRate;

    private String currencyType;

}
