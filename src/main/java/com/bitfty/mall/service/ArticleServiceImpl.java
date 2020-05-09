package com.bitfty.mall.service;

import com.alibaba.fastjson.JSON;
import com.bitfty.mall.bridge.ElasticBaseBridgeServiceImpl;
import com.bitfty.mall.dao.ArticleDao;
import com.bitfty.mall.dao.ArticleTypeDao;
import com.bitfty.mall.dto.ArticleDto;
import com.bitfty.mall.entity.Article;
import com.bitfty.mall.entity.ArticleType;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private static final String INDEX_NAME = "tbl_article";

    @Autowired
    private ElasticBaseBridgeServiceImpl elasticBaseBridgeService;

    @Autowired
    private ArticleTypeDao articleTypeDao;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public ArticleDto findById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        Article article = articleDao.findById(id).orElse(null);
        return BeanMapper.map(article, ArticleDto.class);
    }

    @Override
    public Page<Map<String, Object>> pageFieldsByParams(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        String[] fields = new String[]{"id","title","content","summaryContent","typeId"};
        Page page =elasticBaseBridgeService.pageByParams(INDEX_NAME,Map.class,pageNo, pageSize,params,fields);
        return page;
    }


    @Override
    public Page<ArticleDto> findArticles(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        QueryBuilder queryBuilder = getSearchConditions(params);
        List<String> fields = Lists.newArrayList("title", "summaryContent");
        SearchResponse response;
        try {
            response = elasticBaseBridgeService.search(INDEX_NAME, queryBuilder, pageNo, pageSize, fields);
        } catch (Exception e) {
            log.info("elastic search exception e={}", e);
            throw new ServiceException(ServiceCode.ELASTIC_SEARCH_EXCEPTION);
        }

        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        if (hits == null || hits.length <= 0) {
            return null;
        }

        List<Article> result = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            Article article = JSON.parseObject(hit.getSourceAsString(), Article.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField titleField = highlightFields.get("title");
            if (titleField != null) {
                article.setTitle(titleField.fragments()[0].string());
            }
            HighlightField contentField = highlightFields.get("summaryContent");
            if (contentField != null) {
                article.setSummaryContent(contentField.fragments()[0].string());
            }
            result.add(article);
        }

        Integer numHits = (int) searchHits.getTotalHits().value;
        return new Page<>(numHits, pageSize, pageNo, BeanMapper.mapList(result, ArticleDto.class));
    }

    @Override
    public Page<ArticleDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        QueryBuilder conditions = getPageConditions(params);
        Page<ArticleDto> page;
        try {
            page = elasticBaseBridgeService.page(INDEX_NAME, conditions, pageNo, pageSize, ArticleDto.class);

            if(page == null){
                return null;
            }
            List<ArticleDto> articleDtos = page.getContent();
            List<ArticleType> articleTypes = articleTypeDao.findArticleTypes();
            Map<String, ArticleType> collect = articleTypes.stream().collect(Collectors.toMap(ArticleType::getId, Function.identity()));

            for(ArticleDto articleDto : articleDtos){
                ArticleType articleType = collect.get(articleDto.getTypeId());
                articleDto.setTypeName(articleType!=null?articleType.getName():"");
            }
            page.setContent(articleDtos);
        } catch (Exception e) {
            log.info("article page exception e={}", e);
            throw new ServiceException(ServiceCode.FAILD, "article page exception");
        }
        return page;
    }

    private QueryBuilder getSearchConditions(Map<String, Object> params){
        String content = (String) params.get("query");
        String language = (String) params.get("language");

        if(content.length() > 20) {
            content = content.substring(0, 20);
        }

        MatchQueryBuilder matchTitle = QueryBuilders.matchQuery("title", content).boost(10);
        MatchQueryBuilder matchContent = QueryBuilders.matchQuery("summaryContent", content).boost(1);
        MatchQueryBuilder matchLanguage= QueryBuilders.matchQuery("language", language);

        BoolQueryBuilder conditions = QueryBuilders.boolQuery().should(matchTitle).should(matchContent);
        return QueryBuilders.boolQuery().must(conditions).must(matchLanguage);
    }

    private QueryBuilder getPageConditions(Map<String, Object> params){

        String title = (String)params.get("title");
        String typeId = (String)params.get("typeId");
        String language = (String)params.get("language");
        String startTime = (String)params.get("createAtStart");
        String endTime = (String)params.get("createAtEnd");

        BoolQueryBuilder conditions = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(title)){
            conditions.must(QueryBuilders.matchQuery("title", title).fuzziness(Fuzziness.AUTO));
        }

        if(StringUtils.isNotBlank(typeId)){
            conditions.must(QueryBuilders.matchQuery("typeId", typeId));
        }

        if (StringUtils.isNotBlank(language)) {
            conditions.must(QueryBuilders.matchQuery("language", language));
        }

        if(StringUtils.isNotBlank(startTime)){
            Long startTimeStamp = getTimeStamp(startTime);
            conditions.must(QueryBuilders.rangeQuery("createAt").gte(startTimeStamp));
        }

        if(StringUtils.isNotBlank(endTime)){
            Long endTimeStamp = getTimeStamp(endTime);
            conditions.must(QueryBuilders.rangeQuery("createAt").lte(endTimeStamp));
        }

        return conditions;
    }

    @Override
    public void insert(ArticleDto articleDto) {
        try {
            if (articleDto != null) {
                articleDao.save(BeanMapper.map(articleDto, Article.class));
            }
        } catch (Exception e) {
            log.info("article insert exception,articleDto={}, e={}", articleDto.toString(), e);
            throw new ServiceException(ServiceCode.FAILD);
        }
    }

    @Override
    public void update(ArticleDto articleDto) {
        try {
            if(articleDto!=null && StringUtils.isNotBlank(articleDto.getId())){
                articleDao.save(BeanMapper.map(articleDto, Article.class));
            }
        } catch (Exception e) {
            log.info("article update exception, articleDto={}, e={}", articleDto.toString(), e);
            throw new ServiceException(ServiceCode.FAILD);
        }
    }

    @Override
    public void delete(String id) {
        try {
            if(StringUtils.isNotBlank(id)){
                articleDao.deleteById(id);
            }
        } catch (Exception e) {
            log.info("article delete exception, articleDto={}, e={}", e);
            throw new ServiceException(ServiceCode.FAILD);
        }

    }

    private Long getTimeStamp(String time){
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(time, ftf);
        return LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }



}
