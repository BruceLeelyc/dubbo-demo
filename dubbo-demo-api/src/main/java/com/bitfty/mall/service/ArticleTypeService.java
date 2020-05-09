package com.bitfty.mall.service;

import com.bitfty.mall.dto.ArticleTypeDto;
import com.bitfty.util.Page;

import java.util.List;
import java.util.Map;

public interface ArticleTypeService {

    List<ArticleTypeDto> findByParams(Map<String, Object> params);

    int insert(ArticleTypeDto articleTypeDto);

    int update(ArticleTypeDto articleTypeDto);

    int delete(String id);

    ArticleTypeDto findById(String id);

    Page<ArticleTypeDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 新cms查询接口
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param orderBy 排序字段
     * @param sortBy  排序方式
     * @param params 查询参数
     * @return
     */
    Page<ArticleTypeDto> page(Integer pageNum, Integer pageSize, String orderBy, String sortBy, Map<String, Object> params);

    /**
     * 返回推荐文章类型
     * @param params
     * @return
     */
    List<Map<String, Object>> findTypeNameByParams(Map<String, Object> params);

    List<ArticleTypeDto> findArticleTypes();

}
