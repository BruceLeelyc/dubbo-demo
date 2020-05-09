package com.bitfty.mall.enums;

import lombok.Getter;

public enum OrderStatus {
    ORDER_STATUS(1,"name1"),
    ORDER_FAIL(2,"name2"),
    ORDER_PAY_SUCCESS(3,"name3");

    OrderStatus(int status,String value){
        this.status = status;
        this.value = value;
    }

    @Getter
    private int status;
    @Getter
    private String value;

}
