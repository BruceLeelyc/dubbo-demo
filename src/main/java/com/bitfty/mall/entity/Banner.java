package com.bitfty.mall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/11/28 11:21
 * @description：${description}
 */
@Data
public class Banner implements Serializable {

    private Integer id;
    //状态
    private int status;
    //标题
    private String title;
    //图片类型
    private String type;
    //图片地址
    private String bannerUrl;
    //图片链接
    private String bannerLink;
    //备注
    private String memo;
    //创建人
    private String createBy;
    //更新人
    private String updateBy;
    //开始日期
    private Date startDate;
    //结束日期
    private Date endDate;
    //创建时间
    private Date createAt;
    //更新时间
    private Date updateAt;


}
