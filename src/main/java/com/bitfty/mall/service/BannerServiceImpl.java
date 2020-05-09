package com.bitfty.mall.service;

import com.bitfty.mall.dao.BannerDao;
import com.bitfty.mall.dto.BannerDto;
import com.bitfty.mall.entity.Banner;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/11/28 11:11
 * @description：${description}
 */

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerDao bannerDao;

    @Override
    public List<BannerDto> getBannerList(String type){
        List<Banner> bannerList = bannerDao.getBannerList(type);
        return  BeanMapper.mapList(bannerList, BannerDto.class);
    }

    @Override
    public Page<BannerDto> page(Integer pageNo, Integer pageSize, String stime,String etime, String type) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("type",type);
        params.put("start",(pageNo-1)*pageSize);
        params.put("ends",pageSize);
        params.put("startTimeBegin",stime);
        params.put("startTimeEnd",etime);
        List<Banner> admins = bannerDao.findPageByParams(params);
        Integer total = bannerDao.countByParams(params);
        return new Page(total,pageSize,pageNo, BeanMapper.mapList(admins, BannerDto.class));
    }

    @Override
    public Page<BannerDto> page(Integer pageNum, Integer pageSize, String orderBy, String sortBy, Map<String, Object> params) {
        if (null == params) {
            params = Maps.newHashMap();
        }
        params.put("start", (pageNum -1)*pageSize);
        params.put("ends", pageSize);
        params.put("orderBy", orderBy);
        params.put("sortBy", sortBy);
        List<Banner> admins = bannerDao.findPageByParams(params);
        Integer total = bannerDao.countByParams(params);
        return new Page(total,pageSize,pageNum, BeanMapper.mapList(admins, BannerDto.class));
    }

    @Override
    public BannerDto findById(Long id) {
        Banner banner = bannerDao.findById(id);
        return BeanMapper.map(banner,BannerDto.class);
    }

    @Override
    public void save(BannerDto bannerDto) {
        Banner banner = BeanMapper.map(bannerDto,Banner.class);
        bannerDao.save(banner);
    }

    @Override
    public void update(BannerDto bannerDto) {
        Banner banner = BeanMapper.map(bannerDto,Banner.class);
        bannerDao.update(banner);
    }

    @Override
    public void delete(Long id) {
        bannerDao.delete(id);
    }


}
