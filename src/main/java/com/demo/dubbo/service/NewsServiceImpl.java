package com.demo.dubbo.service;

import com.demo.dubbo.dao.NewsDao;
import com.bitfty.mall.dto.BannerDto;
import com.bitfty.mall.dto.NewsDto;
import com.demo.dubbo.entity.News;
import com.bitfty.mall.service.NewsService;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Slf4j
@Service(timeout = 3000)
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Override
    public Page<NewsDto> page(Integer pageNo, Integer pageSize) {
        Map<String,Object> params = Maps.newHashMap();
        params.put("start",(pageNo-1)*pageSize);
        params.put("ends",pageSize);
        List<News> admins = newsDao.findPageByParams(params);
        Integer total = newsDao.countByParams(params);
        return new Page(total,pageSize,pageNo, BeanMapper.mapList(admins, NewsDto.class));
    }

    @Override
    public Page<BannerDto> page(Integer pageNum, Integer pageSize, String orderBy, String sortBy, Map<String, Object> params) {
        if (null == params) {
            params = Maps.newHashMapWithExpectedSize(4);
        }
        params.put("start", (pageNum -1)*pageSize);
        params.put("ends", pageSize);
        params.put("orderBy", orderBy);
        params.put("sortBy", sortBy);
        List<News> admins = newsDao.findPageByParams(params);
        Integer total = newsDao.countByParams(params);
        return new Page(total,pageSize,pageNum, BeanMapper.mapList(admins, NewsDto.class));
    }

    @Override
    public Page<NewsDto> pageParams(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("start",(pageNo-1)*pageSize);
        params.put("ends",pageSize);
        List<News> admins = newsDao.findPageByParams(params);
        Integer total = newsDao.countByParams(params);
        log.info("----NewsDto-------"+total);
        return new Page(total,pageSize,pageNo, BeanMapper.mapList(admins, NewsDto.class));

    }


    @Override
    public List<NewsDto> list(Integer pageSize) {
        List<News> news = newsDao.list(pageSize);
        return BeanMapper.mapList(news, NewsDto.class);
    }

    @Override
    public NewsDto info(Long id) {
        if (id == null){
            return null;
        }
        News news = newsDao.findById(id);
        return BeanMapper.map(news,NewsDto.class);
    }

    @Override
    public int save(NewsDto newsDto) {
        if(newsDto!=null){
            News news = BeanMapper.map(newsDto,News.class);
            return newsDao.save(news);
        }else{
            return 0;
        }
    }

    @Override
    public int update(NewsDto newsDto) {
        if(newsDto!=null && newsDto.getId()!=null){
            log.info("---newsDto:"+newsDto.getSummaryUrl());
            News news = BeanMapper.map(newsDto,News.class);
            log.info("---news:"+news.getSummaryUrl());
            return newsDao.update(news);
        }else{
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        if(id!=null){
            return newsDao.delete(id);
        }else{
            return 0;
        }
    }


}
