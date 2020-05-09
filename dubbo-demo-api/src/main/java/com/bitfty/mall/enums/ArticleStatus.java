package com.bitfty.mall.enums;

import lombok.Getter;

/**
 * @ClassName: ArticleStatus
 * @Description: 文章状态
 * @Author: gjw
 * @Date: 2020/01/14
 */
public enum ArticleStatus {

    //文章状态
    ARTICLE_DELETE(-1,"已删除"),
    ARTICLE_ORDINARY(0,"普通"),
    ARTICLE_RECOMMEND(1,"推荐"),
    //文章投票状态
    ARTICLE_VOTE_NO(0,"没有帮助"),
    ARTICLE_VOTE_YES(1,"有帮助")
    ;

    ArticleStatus(int type,String detail){
        this.type = type;
        this.detail = detail;
    }

    @Getter
    private int type;
    @Getter
    private String detail;


}
