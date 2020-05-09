package com.bitfty.mall.enums;

import lombok.Getter;

public enum OrderStatus {
    ORDER_DISCARD(-2,"删除"),
    ORDER_CANCEL(-1,"取消"),
    ORDER_INIT(0,"初始化"),
    ORDER_PAY_WAIT(1,"待支付"),
    ORDER_EXEC(2,"处理中"),
    ORDER_PAY_SUCCESS(3,"已完成"),
    ;//支付失败或者异常

    OrderStatus(int status,String value){
        this.status = status;
        this.value = value;
    }

    @Getter
    private int status;
    @Getter
    private String value;

}
