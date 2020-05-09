package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
public class OrderDetailDto implements Serializable {

    /**
     * 订单id
     */
    private String id;

    /**
     * 下单时间
     */
    private Date createAt;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户名称
     * */
    private String realName;

    /**
     * 支付方式
     */
    private String payment;

    /**
     * 算力费
     * */
    private String prodAmount;

    /**
     * 订单实际支付金额
     * */
    private String amount;

    /**
     * 订单状态
     * */
    private Integer status;

    /**
     * 商品图片
     */
    private String pictureUrl;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 算力类型
     */
    private String currencyType;
    /**
     * 算力大小
     */
    private String powerSize;

    /**
     * 期限
     */
    private Integer validDay;

    /**
     * 商品单价
     */
    private String soldAmount;

    /**
     * 份数
     */
    private Integer buyCount;

    /**
     * 电费缴纳天数
     */
    private Integer electricDays;

    /**
     * 电费/周期
     */
    private String electricFee;

    /**
     * 电费/T/天
     * */
    private String soldElectricFee;

    /**
     * 订单实际金额
     */
    private String originalAmount;

    /**
     * 优惠卷
     */
    private String couponAmount;

    /**
     * 产品id
     * */
    private String prodId;

    /**
     * 多币种汇率以及实际支付金额
     * */
    private Map exchangeRate;

    private Integer productType;
}
