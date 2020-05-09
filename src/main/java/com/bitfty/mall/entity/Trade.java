package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/11/29 10:18
 * @description：${description}
 */
@Data
public class Trade implements Serializable {

    private int id;

    //交易编号
    private String tradeNo;

    //订单编号
    private String orderId;

    //支付账号
    private String accountId;

    //用户ID
    private Long userId;

    //交易金额
    private BigDecimal amount;

    //交易币种
    private String currencyType;

    //交易类型，可能设计电费的交易类型
    private int tradeType;

    //交易渠道，可能设计第三方的交易渠道，比如 余额，cobo钱包...
    private String channel;

    //状态,可能设计第三方的交易状态
    private int status;

    //创建时间
    private Date createAt;

    //更新时间
    private Date updateAt;

}
