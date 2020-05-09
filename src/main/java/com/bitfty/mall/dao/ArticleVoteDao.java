package com.bitfty.mall.dao;


import com.bitfty.mall.entity.ArticleVote;
import org.apache.ibatis.annotations.Param;

public interface ArticleVoteDao {
    int deleteByPrimaryKey(String id);

    int insert(ArticleVote record);

    ArticleVote findById(@Param("id") String id);

    int updateByPrimaryKeySelective(ArticleVote record);

    int updateByPrimaryKey(ArticleVote record);

    ArticleVote findByIdAndUserId(@Param("id") String id, @Param("uid") String uid);
}