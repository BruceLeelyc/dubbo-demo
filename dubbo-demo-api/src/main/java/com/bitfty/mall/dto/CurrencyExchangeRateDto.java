package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CurrencyExchangeRateDto implements Serializable {

    /**
     * 比例（USD）
     * */
    private BigDecimal proportion;

    /**
     * 支付金额
     * */
    private BigDecimal amount;

}
