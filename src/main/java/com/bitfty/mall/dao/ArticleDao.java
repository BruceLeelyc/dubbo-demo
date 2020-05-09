package com.bitfty.mall.dao;

import com.bitfty.mall.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDao extends ElasticsearchRepository<Article, String> {
}
