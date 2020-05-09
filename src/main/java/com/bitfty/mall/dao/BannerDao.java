package com.bitfty.mall.dao;

import com.bitfty.mall.entity.Banner;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/11/28 11:20
 * @description：${description}
 */
public interface BannerDao {

     List<Banner> getBannerList(@Param("type") String type);

     List<Banner> findPageByParams(Map<String, Object> params);

     Integer countByParams(Map<String, Object> params);

    Banner findById(@Param("id")Long id);

    void save(Banner banner);

    void update(Banner banner);

    void delete(@Param("id")Long id);
}
