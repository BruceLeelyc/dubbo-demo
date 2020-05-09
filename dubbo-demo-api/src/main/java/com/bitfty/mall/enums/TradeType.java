package com.bitfty.mall.enums;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/12/4 11:48
 * @description：${description}
 */
public enum TradeType {

    TRADE_BALANCE(1,"余额");

    TradeType(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }

}
