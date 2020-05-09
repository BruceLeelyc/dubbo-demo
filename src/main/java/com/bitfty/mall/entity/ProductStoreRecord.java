package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: ProductStoreRecord
 * @Description: 产品库存记录
 * @author: lixl
 * @date: 2019/12/6 11:55
 */
@Data
public class ProductStoreRecord implements Serializable {

	private static final long serialVersionUID = -3262978703707194994L;
    /**
     * 1-减库存
     */
	public static final int TYPE_SUB = 1;
    /**
     * 2-加库存
     */
	public static final int TYPE_ADD = 2;

	private String id;

	private String prodId;

	private String orderId;

	private String name;

	private Integer storeCount;

	private Integer soldCount;

	private Integer buyCount;

	private Integer type;

	private Integer status;

	private String memo;

	private Date createAt;

	private Date updateAt;
}