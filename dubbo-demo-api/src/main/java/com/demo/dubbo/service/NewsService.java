package com.bitfty.mall.service;

import com.bitfty.mall.dto.BannerDto;
import com.bitfty.mall.dto.NewsDto;
import com.bitfty.util.Page;

import java.util.List;
import java.util.Map;

public interface NewsService {


    Page<NewsDto> page(Integer pageNo,Integer pageSize);

    /**
     * 新cms查询接口
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param orderBy 排序字段
     * @param sortBy  排序方式
     * @param params 查询参数
     * @return
     */
    Page<BannerDto> page(Integer pageNum, Integer pageSize, String orderBy, String sortBy, Map<String, Object> params);

    Page<NewsDto> pageParams(Integer pageNo,Integer pageSize, Map<String,Object> params);

    List<NewsDto> list(Integer pageSize);

    NewsDto info(Long id);

    int save(NewsDto news);

    int update(NewsDto news);

    int delete(Long id);

}
