package com.bitfty.mall.service;

import com.bitfty.mall.bridge.ElasticBaseBridgeServiceImpl;
import com.bitfty.mall.dao.ArticleVoteDao;
import com.bitfty.mall.dto.ArticleVoteDto;
import com.bitfty.mall.entity.ArticleVote;
import com.bitfty.mall.enums.ArticleStatus;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.util.BeanMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

@Service
public class ArticleVoteServiceImpl implements ArticleVoteService {

    private static final Logger log = LoggerFactory.getLogger(ArticleVoteServiceImpl.class);

    private static final String INDEX_NAME = "tbl_article";

    @Autowired
    private ArticleVoteDao articleVoteDao;


    @Autowired
    private ElasticBaseBridgeServiceImpl elasticBaseBridgeService;

    @Override
    public void insert(Map<String, Object> params) {
        ArticleVote articleVote = initArticleVote(params);
        Integer num;
        try {
            num = articleVoteDao.insert(articleVote);
        } catch (Exception e) {
            log.info("article vote insert exception articleVote={}, e={}", articleVote.toString(), e);
            throw new ServiceException(ServiceCode.ARTICLE_VOTE_REPEAT);
        }
        if (num <= 0) {
            return;
        }
        String command = getCommand(articleVote.getStatus(), articleVote.getArticleId());
        try {
            elasticBaseBridgeService.updateByQuery(INDEX_NAME, "id", articleVote.getArticleId(), command);
        } catch (Exception e) {
            log.info("update article clicks exception articleVote={}, command={}, e={}" ,articleVote.toString(), command, e);
            throw new ServiceException(ServiceCode.ARTICLE_VOTE_UPDATE_BY_QUERY);
        }

    }

    @Override
    public ArticleVoteDto findById(String id) {
        ArticleVote articleVote = articleVoteDao.findById(id);
        return BeanMapper.map(articleVote, ArticleVoteDto.class);
    }

    @Override
    public ArticleVoteDto findByIdAndUserId(String id, String uid) {
        ArticleVote articleVote = articleVoteDao.findByIdAndUserId(id, uid);
        return BeanMapper.map(articleVote, ArticleVoteDto.class);
    }

    private ArticleVote initArticleVote(Map<String, Object> params) {
        String articleId = (String) params.get("articleId");
        String userId = (String) params.get("userId");
        String id = DigestUtils.md5Hex(userId + articleId);

        ArticleVote articleVote = new ArticleVote();
        articleVote.setId(id);
        articleVote.setArticleId(articleId);
        articleVote.setUserId(userId);
        articleVote.setStatus((Integer) params.get("status"));
        articleVote.setCreateAt(new Date());
        articleVote.setUpdateAt(new Date());
        return articleVote;
    }

    private String getCommand(Integer status, String key) {
        String command = "";
        if (ArticleStatus.ARTICLE_VOTE_YES.getType() == status) {
            command = "if (ctx._source.id == '" + key + "') {ctx._source.tops++; ctx._source.clicks++;}";
        } else if (ArticleStatus.ARTICLE_VOTE_NO.getType() == status) {
            command = "if (ctx._source.id == '" + key + "') {ctx._source.clicks++;}";
        }
        return command;
    }
}
