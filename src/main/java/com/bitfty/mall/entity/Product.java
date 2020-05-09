package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangmi
 */
@Data
public class Product implements Serializable {

    private String id;

    /**
     * 图片url
     */
    private String pictureUrl;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品价格
     */
    private BigDecimal amount;

    /**
     * 销售价格
     */
    private BigDecimal soldAmount;

    /**
     * 每度电价
     */
    private BigDecimal originElectric;

    /**
     * 折扣后每度电价
     */
    private BigDecimal discountElectric;


    /**
     * 库存数量
     */
    private Integer storeCount;

    /**
     * 已销售数量
     */
    private Integer soldCount;

    /**
     * 电费
     */
    private BigDecimal electricFee;

    /**
     * 销售电费
     */
    private BigDecimal soldElectricFee;

    /**
     * 服务费用
     */
    private BigDecimal serviceFee;

    /**
     * 算力大小
     */
    private BigDecimal powerSize;

    /**
     * 功耗
     */
    private BigDecimal consumePower;

    /**
     * 最高限购
     */
    private Integer buyMax;

    /**
     * 最低限购
     */
    private Integer buyMin;

    /**
     * 商品标签
     */
    private String labels;

    /**
     * 商品标签权重
     */
    private Integer weightTotal;

    /**
     * 算力类型
     */
    private String currencyType;

    /**
     * 产品类型
     */
    private String prodType;

    /**
     * 开始收益日期
     */
    private Date gainDate;

    /**
     * 开始售卖日期
     */
    private Date soldDate;

    /**
     * 期限(天)
     */
    private Integer validDay;

    /**
     * 推荐状态
     */
    private Integer recommendStatus;

    /**
     * 备注
     */
    private String memo;

    /**
     * 状态(1:上架 4：删除)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 购买阶梯数
     */
    private Integer buyStep;

    /**
     * 电费缴纳阶梯数
     */
    private Integer electricStep;

    /**
     * 电费最小缴纳天数
     */
    private Integer electricMin;

    /**
     * 属性
     */
    private Integer productEfp;
}