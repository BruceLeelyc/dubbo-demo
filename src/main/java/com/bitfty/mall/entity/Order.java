package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {

    private String id;


    /**
     * 订单编号
     * */
    private String orderNo;

    /**
     * 用户id
     * */
    private Long userId;

    /**
     * 订单金额
     * */
    private BigDecimal amount;

    /**
     * 优惠金额
     * */

    private BigDecimal discountAmount;

    /**
     * 优惠卷金额
     * */
    private BigDecimal couponAmount;

    /**
     * 服务费用
     * */
    private BigDecimal serviceFee;

    /**
     * 产品总金额
     * */
    private BigDecimal prodAmount;

    /**
     * 电费
     * */
    private BigDecimal electricFee;

    /**
     * 备注
     * */
    private String memo;

    /**
     * 状态（1：初始化 2：待支付 3：已支付 4：取消订单）
     * */
    private Integer status;

    /**
     * 创建时间
     * */
    private Date createAt;

    /**
     * 更新时间
     * */
    private Date updateAt;

    /**
     * 各币种汇率
     * */
    private String exchangeRate;

    /**
     * 支付方式
     * */
    private String currencyType;


}
