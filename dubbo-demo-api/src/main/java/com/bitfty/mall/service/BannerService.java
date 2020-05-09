package com.bitfty.mall.service;

import com.bitfty.mall.dto.BannerDto;
import com.bitfty.util.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/11/28 11:11
 * @description：${description}
 */
public interface BannerService {

    List<BannerDto> getBannerList(String type);

    Page<BannerDto> page(Integer pageNo, Integer pageSize, String stime, String etime,String type);

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

    BannerDto findById(Long id);

    void save(BannerDto bannerDto);

    void update(BannerDto bannerDto);

    void delete(Long id);

}
