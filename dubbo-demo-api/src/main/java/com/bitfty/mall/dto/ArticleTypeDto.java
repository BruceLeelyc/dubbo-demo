package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleTypeDto implements Serializable {

    /**
     * id
     * */
    private String id;

    /**
     * 名称
     * */
    private String name;

    /**
     * 创建人id
     * */
    private Long userId;

    /**
     * 创建人姓名
     * */
    private String userName;

    /**
     * 语言
     * */
    private String language;

    /**
     * 是否选中
     * */
    private boolean checked;

    /**
     * 创建时间
     * */
    private Date createAt;

    /**
     * 更新时间
     * */
    private Date updateAt;

}
