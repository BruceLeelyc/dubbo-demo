package com.bitfty.mall.dao;

import com.bitfty.mall.entity.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NewsDao {

    News findById(@Param("id") Long id);

    List<News> findPageByParams(Map<String,Object> params);

    Integer countByParams(Map<String,Object> params);

    int save(News news);

    int update(News news);

    int delete(Long id);

    List<News> list(@Param("pageSize") Integer pageSize);
}
