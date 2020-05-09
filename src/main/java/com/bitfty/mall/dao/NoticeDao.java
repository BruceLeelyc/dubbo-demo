package com.bitfty.mall.dao;

import com.bitfty.mall.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface NoticeDao {

    Notice findById(@Param("id") Long id);

    List<Notice> findPageByParams(Map<String,Object> params);

    Integer countByParams(Map<String,Object> params);

    int update(Notice notice);

    int save(Notice notice);

    int delete(Long id);

    List<Notice> list();

}
