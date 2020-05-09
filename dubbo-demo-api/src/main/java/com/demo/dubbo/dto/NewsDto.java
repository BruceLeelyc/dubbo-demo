package com.bitfty.mall.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsDto implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String newsUrl;

    private String summary;

    private String summaryUrl;

    private String link;

    private String content;

    private Date createAt;

    private String createAtStr;

    private Date updateAt;
}
