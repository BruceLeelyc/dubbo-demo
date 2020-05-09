package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderListDto implements Serializable {

    /**
     * 订单id
     */
    private String id;

    /**
     * 商品id
     * */
    private String prodId;

    /**
     * 商品类型
     * */
    private String prodType;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 算力类型
     */
    private String currencyType;

    /**
     * 订单支付金额类型
     */
    private String orderCurrencyType;

    /**
     * 算力大小
     */

    private BigDecimal powerSize;

    /**
     * 购买份额
     */
    private Integer buyCount;

    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    private BigDecimal prodAmount;

    private BigDecimal electricFee;

    /**
     * 汇率多币种
     */
    private String exchangeRate;


    /**
     * 预计到期日
     */
    private Date expireDate;

    /**
     * 开始收益日期
     * */
    private Date gainDate;

    /**
     * 购买时间
     */
    private Date createAt;

    /**
     * 支付币种
     */
    private String payType;

    /**
     * 实际支付金额
     * */
    private String payAmount;

    /**
     * 备注
     */
    private String memo;

    private String pictureUrl;

    /**
     * 订单状态
     * */
    private Integer status;

    private Integer firstPeriodDay;

    /**
     * 首期交电费天数
     */
    private Integer days;
}
