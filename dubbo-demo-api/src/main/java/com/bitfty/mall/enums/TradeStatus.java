package com.bitfty.mall.enums;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/12/2 16:01
 * @description：${description}
 */
public enum TradeStatus {

    TRADE_FAIL(-1,"交易取消"),
    TRADE_INIT(0,"初始化"),
    TRADE_WAIT(1,"处理中"),
    TRADE_SUCC(2,"交易成功"),
    ;

    TradeStatus(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }

    private Integer type;

    private String desc;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
