package com.bitfty.mall.enums;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author zhangmi
 * 产品库存操作
 */
public enum ProductStatus {

    /**
	 * 产品-状态
     * -1-下架
     * 0-待上架
     * 1-上架
     * 2-售罄
     */
    DOWN(-1, "下架"),
    INIT(0, "待上架"),
    SELL(1,"上架"),
    SELL_OUT(2,"售罄");

    @Getter
    private int status;
    @Getter
    private String desc;

    ProductStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
