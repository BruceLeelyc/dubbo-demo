package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductStoreRecordDto implements Serializable {

    private static final long serialVersionUID = -1059003706725216059L;
    private String id;

    private String prodId;

    private String orderId;

    private String name;

    private Integer storeCount;

    private Integer soldCount;

    private Integer buyCount;

    private Integer status;

    private String memo;

    private Date createAt;

    private Date updateAt;

}
