package com.bitfty.mall.bridge;

import com.alibaba.fastjson.JSON;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.util.Page;
import com.google.common.collect.Maps;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: elasticsearch
 * @Description: 基础类
 */

@Component
public class ElasticBaseBridgeServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ElasticBaseBridgeServiceImpl.class);

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 数据检索
     *
     * @param indexName 索引名称
     * @param builder   条件聚合
     * @param from    分页
     * @param size  条数
     * @param fields    高亮字段
     */
    public SearchResponse search(String indexName, QueryBuilder builder, Integer from, Integer size, List<String> fields) {

        SearchSourceBuilder searchSourceBuilder = initSearchSourceBuilder(builder, from, size, 15, fields);
        SearchRequest request = new SearchRequest(indexName).source(searchSourceBuilder);
        return getSearchResponse(request);
    }

    /**
     * 插入/更新
     * @param request
     */
    public void insertOrUpdateOne(IndexRequest request) {
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除
     * @param indexName 索引名称
     * @param id        对象
     */
    public void deleteOne(String indexName, String id) {
        DeleteRequest request = new DeleteRequest(indexName);
        request.id(id);
        try {
            restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新部分数据
     *
     * @param  indexName 索引名称
     * @param  key       字段名称
     * @param  value     查询内容
     * @param  command   执行命令
     */
    public void updateByQuery(String indexName, String key, String value, String command){

        UpdateByQueryRequest request = new UpdateByQueryRequest(indexName);
        request.setQuery(new MatchQueryBuilder(key, value));

        Script script = new Script(ScriptType.INLINE, "painless", command, Collections.emptyMap());
        request.setScript(script);

        try {
            restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
            request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取记录信息
     * @param indexName
     * @param id
     * @throws IOException
     */
    public  Map<String, Object> getData(String indexName, String id){
        GetRequest getRequest = new GetRequest(indexName, id);
        GetResponse getResponse = null;
        Map<String, Object>  resultMap = Maps.newHashMap();
        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            resultMap  = getResponse.getSource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 分页
     *
     * @param  indexName    索引名称
     * @param  conditions   条件聚合
     * @param  from         分页
     * @param  size         每页条数
     */
    public <T> Page<T> page(String indexName, QueryBuilder conditions, Integer from, Integer size, Class<T> c) {

        SearchSourceBuilder searchSourceBuilder = initSearchSourceBuilder(conditions, from, size, 15, null);
        SearchRequest request = new SearchRequest(indexName).source(searchSourceBuilder);

        SearchResponse response = getSearchResponse(request);
        if(response == null){
            return null;
        }

        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        if (hits == null || hits.length <= 0) {
            return null;
        }

        List<T> result = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            result.add(JSON.parseObject(hit.getSourceAsString(), c));
        }

        Integer numHits = (int)searchHits.getTotalHits().value;
        return new Page<>(numHits, size, from, result);

    }

    private SearchResponse getSearchResponse(SearchRequest request) {
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private SearchSourceBuilder initSearchSourceBuilder(QueryBuilder builder, Integer from, Integer size, Integer timeout, List<String> fields) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        sourceBuilder.from((from - 1) * size);
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(timeout, TimeUnit.SECONDS));

        if(fields == null || fields.size() <= 0){
            return sourceBuilder;
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String field : fields) {
            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(field).requireFieldMatch(false);
            highlightBuilder.field(highlightTitle);
        }
        highlightBuilder.preTags("<span style='color: red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        return sourceBuilder;
    }


    /**
     *
     * @param indexName  索引名称
     * @param clazz  返回类型
     * @param pageNo 起始页
     * @param pageSize 条数
    //   * @param builder 检索条件
     * @param fields 输出字段
     * @return
     */
    public <T>  Page<T>  pageByParams(String indexName, Class<T> clazz , Integer pageNo, Integer pageSize, Map<String, Object> params ,String[] fields) {
        if(pageNo==null || pageNo<=0){
            pageNo = 1;
        }
        if(pageSize==null || pageSize<=0){
            pageSize = 10;
        }
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            if (null != fields && fields.length > 0) {
                sourceBuilder.fetchSource(fields, null);
            }
            // sourceBuilder.query(QueryBuilders.matchQuery("language","ZH"));
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            if (null != params && params.size() > 0) {
                for (String key : params.keySet()) {
                    builder.must(QueryBuilders.matchQuery(key, params.get(key)));
                }
            }
            sourceBuilder.query(builder);
            sourceBuilder.from((pageNo - 1) * pageSize);
            sourceBuilder.size(pageSize);
            sourceBuilder.timeout(new TimeValue(15, TimeUnit.SECONDS));
            SearchRequest request = new SearchRequest(indexName).source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            List list = new ArrayList<>();
            searchHits.forEach(item -> list.add(JSON.parseObject(item.getSourceAsString(), clazz )));
            Integer total = (int)searchHits.getTotalHits().value;
            return new Page(total, pageSize, pageNo, list);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("method pageByParams , elastic search exception e={}", e);
            throw new ServiceException(ServiceCode.ELASTIC_SEARCH_EXCEPTION);
        }
    }

}