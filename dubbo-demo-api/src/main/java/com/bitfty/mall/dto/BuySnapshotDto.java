package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BuySnapshotDto implements Serializable {

    private String id;

    private String orderId;

    private String prodId;

    private String pictureUrl;

    private String name;

    private BigDecimal amount;

    private BigDecimal soldAmount;

    private BigDecimal electricFee;

    private BigDecimal soldElectricFee;

    private Integer days;

    private BigDecimal serviceFee;

    private BigDecimal powerSize;

    private String currencyType;

    private String prodType;

    private Date gainDate;

    private Date realGainDate;

    private Integer validDay;

    private Integer buyCount;

    private Date expireDate;

    private Date realExpireDate;

    private String memo;

    private Date createAt;

    private Date updateAt;

    private BigDecimal btcAmount;

    private BigDecimal btcExchangeRate;

    private BigDecimal yesterdayGain;

    private BigDecimal totalGain;

    private Integer electricDay;

}
