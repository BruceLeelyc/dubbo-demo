package com.bitfty.mall.enums;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author zhangmi
 * 产品库存操作
 */
public enum ProductOperate {

    /**
     * 产品-操作
     */
    POWER_DEDUCT(1, 1, "算力扣除"),
    POWER_INCREASE(1, 2,"算力增加");

    @Getter
    private int productType;
    @Getter
    private int operType;
    @Getter
    private String desc;

    ProductOperate(int productType, int operType, String desc) {
        this.productType = productType;
        this.operType = operType;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
