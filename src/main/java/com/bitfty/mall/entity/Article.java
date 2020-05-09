package com.bitfty.mall.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "tbl_article")
public class Article implements Serializable {

    /**
     * id
     * */
    private String id;

    /**
     * 标题
     * */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    /**
     * 内容
     * */
    private String content;

    /**
     * 概要内容
     * */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String summaryContent;

    /**
     * 类型id
     * */
    private String typeId;

    /**
     * 创建人id
     * */
    private Long userId;

    /**
     * 语言
     * */
    private String language;

    /**
     * 状态 （-1：不可用 1：正常 2：推荐 ）
     * */
    private Integer status;

    /**
     * 创建时间
     * */
    private Date createAt;

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

}
