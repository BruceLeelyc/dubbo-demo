package com.bitfty.mall.service;

import com.bitfty.mall.dto.NoticeDto;
import com.bitfty.util.Page;

import java.util.List;
import java.util.Map;

public interface NoticeService {

    Page<NoticeDto> page(Integer pageNo, Integer pageSize);

    Page<NoticeDto> pageParams(Integer pageNo, Integer pageSize, Map<String,Object> params);

    NoticeDto info(Long id);

    /**
     * 获取最新的10条公告
     * @return
     */
    List<NoticeDto> list();

    int update(NoticeDto notice);

    int save(NoticeDto notice);

    int delete(Long id);


}
