package com.bitfty.mall.dto;

import com.bitfty.util.DateJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NoticeDto implements Serializable {

    private Long id;

    private String title;

    private String author;

    private String noticeUrl;

    private Date startTime;

    private String dateStr;

    private Date expireTime;
    //页面保存
    private String expireTimeStr;
    //页面查询条件
    private String expireTimeStart;

    private String expireTimeEnd;

    private String summary;

    private String summaryUrl;

    private String content;

    private Date createAt;

    private String createAtStart;

    private String createAtEnd;

    private Date updateAt;
}
