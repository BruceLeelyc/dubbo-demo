package com.bitfty.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class News implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String newsUrl;

    private String summary;

    private String  summaryUrl;

    private String link;

    private String content;

    private Date createAt;

    private Date updateAt;
}
