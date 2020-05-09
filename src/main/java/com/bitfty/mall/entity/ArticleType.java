package com.bitfty.mall.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ArticleType {

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
     * 创建时间
     * */
    private Date createAt;

    /**
     * 更新时间
     * */
    private Date updateAt;



}
