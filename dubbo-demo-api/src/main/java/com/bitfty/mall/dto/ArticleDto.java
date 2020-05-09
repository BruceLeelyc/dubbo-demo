package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleDto implements Serializable {

    /**
     * id
     * */
    private String id;

    /**
     * 标题
     * */
    private String title;

    /**
     * 文章内容
     * */
    private String content;

    /**
     * 文章概要内容
     * */
    private String summaryContent;

    /**
     * 类型id
     * */
    private String typeId;

    /**
     * 类型名称
     * */
    private String typeName;

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
     * 状态 （-1：不可用 0：正常 1：推荐 ）
     * */
    private Integer status;

    /**
     * 创建时间
     * */
    private Date createAt;

    /**
     * 创建时间
     * */
    private String createAtStr;

    /**
     * 更新时间
     * */
    private Date updateAt;

    /**
     * 点击量
     * */
    private Integer clicks;

    /**
     * 赞数
     * */
    private Integer tops;

    /**
     * 是否觉得有帮助
     * */
    private Boolean isHelp;

}
