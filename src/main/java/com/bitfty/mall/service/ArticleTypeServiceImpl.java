package com.bitfty.mall.service;

import com.bitfty.mall.dao.ArticleTypeDao;
import com.bitfty.mall.dto.ArticleTypeDto;
import com.bitfty.mall.dto.BannerDto;
import com.bitfty.mall.entity.ArticleType;
import com.bitfty.mall.entity.Banner;
import com.bitfty.user.dto.LoginRecordDto;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Autowired
    private ArticleTypeDao questionTypeDao;

    @Override
    public List<ArticleTypeDto> findByParams(Map<String, Object> params) {
        List<ArticleType>  questionTypes = questionTypeDao.findByParams(params);
        return BeanMapper.mapList(questionTypes, ArticleTypeDto.class);
    }

    @Override
    public List<Map<String, Object>> findTypeNameByParams(Map<String, Object> params) {
        List<Map<String, Object>>   typeNames = questionTypeDao.findTypeNameByParams(params);
        return typeNames;
    }


    @Override
    public int insert(ArticleTypeDto articleTypeDto) {
        ArticleType record = BeanMapper.map(articleTypeDto, ArticleType.class);
        return questionTypeDao.insertSelective(record);
    }

    @Override
    public int update(ArticleTypeDto articleTypeDto) {
        ArticleType record = BeanMapper.map(articleTypeDto, ArticleType.class);
        return questionTypeDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int delete(String id) {
        return  questionTypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public ArticleTypeDto findById(String id) {
        return  BeanMapper.map(questionTypeDao.selectByPrimaryKey(id), ArticleTypeDto.class);
    }

    @Override
    public Page<ArticleTypeDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params) {
            params.put("start",(pageNo-1)*pageSize);
            params.put("ends",pageSize);
            List<ArticleType> articleTypes = questionTypeDao.findByParams(params);
            Integer total = questionTypeDao.countByParams(params);
            return new Page(total,pageSize,pageNo, BeanMapper.mapList(articleTypes, ArticleTypeDto.class));
    }

    @Override
    public Page<ArticleTypeDto> page(Integer pageNum, Integer pageSize, String orderBy, String sortBy, Map<String, Object> params) {
        if (null == params) {
            params = Maps.newHashMapWithExpectedSize(4);
        }
        params.put("start", (pageNum -1)*pageSize);
        params.put("ends", pageSize);
        params.put("orderBy", orderBy);
        params.put("sortBy", sortBy);
        Integer total = questionTypeDao.countByParams(params);
        List<ArticleType> articleTypes = questionTypeDao.findByParams(params);
        return new Page(total,pageSize,pageNum, BeanMapper.mapList(articleTypes, ArticleTypeDto.class));

    }

    @Override
    public List<ArticleTypeDto> findArticleTypes() {
        List<ArticleType> articleTypes = questionTypeDao.findArticleTypes();
        return BeanMapper.mapList(articleTypes, ArticleTypeDto.class);
    }


}
