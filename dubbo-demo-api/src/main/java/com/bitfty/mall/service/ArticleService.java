package com.bitfty.mall.service;

import com.bitfty.mall.dto.ArticleDto;
import com.bitfty.util.Page;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    void insert(ArticleDto articleDto);

    void update(ArticleDto articleDto);

    void delete(String id);

    ArticleDto findById(String id);

    Page<Map<String, Object>> pageFieldsByParams(Integer pageNo, Integer pageSize, Map<String, Object> params);

    Page<ArticleDto> findArticles(Integer pageNo, Integer pageSize, Map<String, Object> params);

    Page<ArticleDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params);

}
