package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author zhangmi
 */
@Data
public class ProductDetail implements Serializable {
    private String id;

    private String prodId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 产品详情
     */
    private String detailInfo;

    /**
     * 矿机参数
     */
    private String machineParams;

}