package com.bitfty.mall.dao;


import com.bitfty.mall.entity.ArticleType;
import com.bitfty.mall.entity.Banner;

import java.util.List;
import java.util.Map;

public interface ArticleTypeDao {
    int deleteByPrimaryKey(String id);

    int insert(ArticleType record);

    int insertSelective(ArticleType record);

    ArticleType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ArticleType record);

    int updateByPrimaryKey(ArticleType record);

    List<ArticleType> findByParams(Map<String, Object> params);

    Integer countByParams(Map<String, Object> params);


    List<Map<String, Object>> findTypeNameByParams(Map<String, Object> params);

    List<ArticleType> findArticleTypes();

}