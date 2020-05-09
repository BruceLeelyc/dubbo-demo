package com.bitfty.mall.enums;

import lombok.Getter;

/**
 * @ClassName: CoinPrice
 * @Description: 交易所名称
 * @Author: lzk
 * @Date: 2019/12/24 10:31
 */
public enum CoinPrice {

    COIN_BASE("COIN_BASE","CoinBase"),
    SO_CHAIN("SO_CHAIN","soChain"),

    ;

    CoinPrice(String type,String detail){
        this.type = type;
        this.detail = detail;
    }

    @Getter
    private String type;
    @Getter
    private String detail;
}
