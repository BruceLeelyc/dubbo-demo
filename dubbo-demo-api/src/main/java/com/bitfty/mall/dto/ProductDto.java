package com.bitfty.mall.dto;

import com.bitfty.enums.LoginType;
import com.bitfty.util.DateJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangmi
 */
@Data
public class ProductDto implements Serializable {

    private String id;

    /**
     * 产品类型
     */
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
     * 商品图片
     */
    private String pictureUrl;

    /**
     * 商品介绍
     */
    private String memo;


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
     * 商品库存数量
     */
    private Integer storeCount;

    /**
     * 电费
     */
    private BigDecimal electricFee;

    /**
     * 销售电费
     */
    private BigDecimal soldElectricFee;

    /**
     * 管理费
     */
    private BigDecimal serviceFee;

    /**
     * 算力大小
     */
    private BigDecimal powerSize;
    //套餐规格

    //排序

    /**
     * 功耗
     */
    private BigDecimal consumePower;

    /**
     * 预计开始收益日期
     */
    private Date gainDate;

    private String gainDateStr;
    /**
     * 期限
     */
    private Integer validDay;


    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
        this.electricMax = validDay;
    }
    /**
     * 产品详情
     */
    private String detailInfo;


    private Integer soldCount;

    private Integer days;

    private Integer buyMax;

    private Integer buyMin;

    private String labels;

    private Integer weightTotal;

    private Integer recommendStatus;

    /**
     * 状态
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
     * 矿机参数
     */
    private String machineParams;

    /**
     * 开始售卖日期
     */
    private Date soldDate;


    public Date getSoldDate() {
        return soldDate == null ? new Date() : soldDate;
    }

    /**
     * 开始售卖日期
     */
    private String soldDateStr;

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
     * 电费最大缴纳天数
     */
    private Integer electricMax;


    public Integer getElectricMax() {
        return electricMax == null ? validDay : electricMax;
    }

    public BigDecimal soldPercent;

    /**
     * 商品属性
     */
    private Integer productEfp;

    /**
     * 返回库存比例
     * @return
     */
    public BigDecimal getSoldPercent(){
        try {
            BigDecimal total = new BigDecimal(soldCount + storeCount);
            if(total.compareTo(BigDecimal.ZERO) == 0){
                return new BigDecimal(100);
            }
            BigDecimal soldBiLi = new BigDecimal(soldCount).divide(total,2,BigDecimal.ROUND_UP);
            return soldBiLi.multiply(new BigDecimal(100));
        }catch (Exception e){

        }
        return BigDecimal.ZERO;
    }
}
