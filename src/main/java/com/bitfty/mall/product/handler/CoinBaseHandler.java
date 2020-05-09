package com.bitfty.mall.product.handler;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @ClassName: CoinBaseHandler
 * @Description: 获取coinbase的价格
 * @Author: lzk
 * @Date: 2019/12/24 10:29
 */
@Component
public class CoinBaseHandler implements CoinPriceHandler{

    private final String COINBASE = "https://api.coinbase.com/v2/prices/spot?currency=USD";

    private final static Logger logger = LoggerFactory.getLogger(CoinBaseHandler.class);

    @Override
    public BigDecimal getPrice(String currencyType) {
        try{
            HttpResponse response = HttpUtil.createGet(COINBASE).execute();
            if (response == null || StringUtils.isBlank(response.body())) {
                return null;
            }
            JSONObject jso = JSONObject.parseObject(response.body());
            if (jso.getJSONObject("data")==null) {
                return null;
            }
            if (!"BTC".endsWith(jso.getJSONObject("data").getString("base"))){
                return null;
            }
            return jso.getJSONObject("data").getBigDecimal("amount");
        }catch (Exception e){
            logger.error("getCoinBaseBtcPrice error e={}",e);
        }
        return null;
    }

}
