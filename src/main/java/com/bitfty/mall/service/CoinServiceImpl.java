package com.bitfty.mall.service;

import com.bitfty.mall.enums.CoinPrice;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.mall.product.CoinPriceFactory;
import com.bitfty.mall.product.handler.CoinBaseHandler;
import com.bitfty.mall.product.handler.CoinPriceHandler;
import com.bitfty.util.CurrencyType;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CoinServiceImpl
 * @Description: 和币相关基础类
 * @Author: lzk
 * @Date: 2019/12/16 15:54
 */
@Service(timeout = 10000)
public class CoinServiceImpl implements CoinService{

    private final static Logger logger = LoggerFactory.getLogger(CoinServiceImpl.class);

    private static String TODAY_AIGHT_CLOCK_PRICE = "today_aight_clock_price";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${coin.price.exchange:''}")
    private String coinPriceType;

    private static final String COIN_BASE_BTC = "coin_price_btc";

    @Override
    public BigDecimal getBtcToDollar() {
        try{
            String price = redisTemplate.opsForValue().get(COIN_BASE_BTC);
            if (!StringUtils.isBlank(price)) {
                return new BigDecimal(price).stripTrailingZeros();
            }
            List<String> coinPriceExchange = new ArrayList<>();
            if (StringUtils.isNotBlank(coinPriceType) && !coinPriceType.equals("''")) {
                coinPriceExchange.add(coinPriceType);
            }else {
                List<CoinPrice> coinPrices = Arrays.asList(CoinPrice.values());
                coinPrices.forEach(coinPrice -> {coinPriceExchange.add(coinPrice.getType());});
            }
            for (String type:coinPriceExchange) {
                CoinPriceHandler coinPriceHandler = CoinPriceFactory.handler(type);
                BigDecimal coinBaseOffBtcPrice = coinPriceHandler.getPrice(CurrencyType.BTC.getType());
                if (coinBaseOffBtcPrice != null) {
                    redisTemplate.opsForValue().set(COIN_BASE_BTC,coinBaseOffBtcPrice.stripTrailingZeros().toPlainString(), 1, TimeUnit.MINUTES);
                    return coinBaseOffBtcPrice.stripTrailingZeros();
                }
            }

        }catch (Exception e){
            logger.error("getBtcToDollar error e={}",e);
        }
        return null;
    }

    @Override
    public BigDecimal getCoinPriceEightClock(String currencyType) {
        String eightClockPrice = "";
        if (currencyType.equalsIgnoreCase(CurrencyType.USDT.getType())){
            eightClockPrice = "1";
        }
        if (currencyType.equalsIgnoreCase(CurrencyType.BTC.getType())) {
            eightClockPrice = redisTemplate.opsForValue().get(TODAY_AIGHT_CLOCK_PRICE);
        }

        if (StringUtils.isBlank(eightClockPrice)) {
            return null;
        }
        return new BigDecimal(eightClockPrice);
    }
}
