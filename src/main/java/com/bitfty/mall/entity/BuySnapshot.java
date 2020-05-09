package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BuySnapshot implements Serializable {

    private String id;

    /**
     * 订单id
     * */
    private String orderId;

    /**
     * 产品ID
     * */
    private String prodId;

    /**
     * 用户id
     * */
    private String userId;

    /**
     * 图片url
     * */
    private String pictureUrl;

    /**
     * 产品名称
     * */
    private String name;

    /**
     * 产品价格
     * */
    private BigDecimal amount;

    /**
     * 销售价格
     * */
    private BigDecimal soldAmount;

    /**
     * 电费
     * */
    private BigDecimal electricFee;

    /**
     * days
     * */
    private Integer days;

    /**
     * 销售电费
     * */
    private BigDecimal soldElectricFee;

    /**
     * 服务费用
     * */
    private BigDecimal serviceFee;

    /**
     * 算力大小
     * */
    private BigDecimal powerSize;

    /**
     * 算力类型
     * */
    private String currencyType;

    /**
     * 产品类型
     * */
    private String prodType;

    /**
     * 产品收益日期
     * */
    private Date gainDate;

    /**
     * 用户实际开始收益日期
     * */
    private Date realGainDate;

    /**
     * 用户实际结束收益日期
     * */
    private Date realExpireDate;

    /**
     * 期限(天)
     * */
    private Integer validDay;

    /**
     * 购买数量
     * */
    private Integer buyCount;

    /**
     * 到期期限
     * */
    private Date expireDate;

    /**
     * 备注
     * */
    private String memo;

    /**
     * 创建时间
     * */
    private Date createAt;

    /**
     * 更新时间
     * */
    private Date updateAt;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 实际支付BTC金额
     * */
    private BigDecimal btcAmount;

    /**
     * btc汇率
     * */
    private BigDecimal btcExchangeRate;
}
