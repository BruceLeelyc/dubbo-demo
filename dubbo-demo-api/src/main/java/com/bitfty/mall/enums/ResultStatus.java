package com.bitfty.mall.enums;


import lombok.Getter;

public enum ResultStatus {

    FAIL(-1,"失败"),
    HANDLE(0,"处理中"),
    SUCCESS(1,"成功"),
    ;

    ResultStatus(int status,String value){
        this.status = status;
        this.value = value;
    }

    @Getter
    private int status;
    @Getter
    private String value;
}
