package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Notice implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String noticeUrl;

    private Date startTime;

    private Date expireTime;

    private String summary;

    private String  summaryUrl;

    private String content;

    private Date createAt;

    private Date updateAt;
}
