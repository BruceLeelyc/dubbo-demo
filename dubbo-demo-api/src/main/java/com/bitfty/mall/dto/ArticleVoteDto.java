package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleVoteDto implements Serializable {

    /**
     * id
     * */
    private String id;

    /**
     * 文章id
     * */
    private String articleId;

    /**
     * 用户id
     * */
    private String userId;

    /**
     * 状态
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
}