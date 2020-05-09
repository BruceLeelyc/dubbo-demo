package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderBackstageDto implements Serializable {

    private String id;

    private Date createAt;

    private Long userId;

    private BigDecimal amount;

    private String currencyType;

    private Integer status;

}
