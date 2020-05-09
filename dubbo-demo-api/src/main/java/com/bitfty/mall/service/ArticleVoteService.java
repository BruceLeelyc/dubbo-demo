package com.bitfty.mall.service;

import com.bitfty.mall.dto.ArticleVoteDto;

import java.util.Map;

public interface ArticleVoteService {

    void insert(Map<String, Object> params);

    ArticleVoteDto findById(String id);

    ArticleVoteDto findByIdAndUserId(String id, String uid);

}
