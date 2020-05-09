package com.bitfty.mall.enums;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/12/4 11:51
 * @description：${description}
 */
public enum TradeChannel {

    TRADE_BALANCE("1","北京云尔");

    TradeChannel(String chanmel,String desc){
        this.chanmel = chanmel;
        this.desc = desc;
    }

    private String chanmel;

    private String desc;

    public String getChanmel() {
        return chanmel;
    }

    public String getDesc() {
        return desc;
    }
}
