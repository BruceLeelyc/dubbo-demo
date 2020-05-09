package com.bitfty.mall.product.handler;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @ClassName: SochainHandler
 * @Description: soChain
 * @Author: lzk
 * @Date: 2019/12/24 11:41
 */
@Component
public class SochainHandler implements CoinPriceHandler{

    private static final Logger logger = LoggerFactory.getLogger(SochainHandler.class);

    private final String SOCHAIN_PRICE = "https://sochain.com/api/v2/get_price/BTC/USD";

    @Override
    public BigDecimal getPrice(String coinPriceType) {
        try{
            HttpResponse response = HttpUtil.createGet(SOCHAIN_PRICE).execute();
            if (response == null || StringUtils.isBlank(response.body())) {
                return null;
            }
            JSONObject jso = JSONObject.parseObject(response.body());
            if (jso.getJSONObject("data")==null) {
                return null;
            }
            if (!"BTC".endsWith(jso.getJSONObject("data").getString("network"))){
                return null;
            }
            JSONArray prices = jso.getJSONObject("data").getJSONArray("prices");
            BigDecimal coinbasePrice = null;
            for (int i = 0; i < prices.size(); i++) {
                JSONObject priceObject = prices.getJSONObject(i);
                if (!"coinbase".equals(priceObject.getString("exchange"))) {
                    continue;
                }
                coinbasePrice = priceObject.getBigDecimal("price");
                break;
            }
            return coinbasePrice;
        }catch (Exception e){
            logger.error("getSochainBtcPrice error e={}",e);
        }
        return null;
    }
}
