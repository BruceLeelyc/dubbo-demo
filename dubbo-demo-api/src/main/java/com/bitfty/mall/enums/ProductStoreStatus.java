package com.bitfty.mall.enums;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
* @ClassName: ProductStoreStatus
* @Description: 库存操作记录
* @author: lixl
* @date: 2019/12/10 10:24
*/
public enum ProductStoreStatus {

    /**
	 * 产品操作记录-状态
     * -1-失败
     * 0-处理中
     * 1-成功
     */
    FAIL(-1, "失败"),
    HANDLE(0, "处理中"),
    SUCCESS(1,"成功");


    @Getter
    private int status;
    @Getter
    private String desc;

    ProductStoreStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
