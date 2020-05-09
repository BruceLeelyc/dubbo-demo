package com.bitfty.mall.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSnapshotDto implements Serializable {

    /**
     * 订单id
     * */
    private String id;

    /**
     * 用户id
     * */
    private Long userId;

    /**
     * 商品id
     * */
    private String prodId;

    /**
     * 购买数量
     * */
    private Integer buyCount;

}
