package com.demo.dubbo.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangmi
 * 分布式锁
 */
public class DistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private static RedisTemplate<String, String> redisTemplate;


    static {
        if(redisTemplate == null){
            redisTemplate = SpringContextUtil.getBean("redisTemplate");
        }
        logger.info("redis init");
    }

    public static boolean lock(String key, long expireTime) {
        try {
            if (redisTemplate.opsForValue().setIfAbsent(key, "NX", expireTime, TimeUnit.SECONDS)) {
                return true;
            }
        } catch (Exception e) {
            logger.info("mall redis lock failed", e);
        }
        return false;
    }


    public static boolean unLock(String key) {
        try {
            if(StringUtils.isNotBlank(key) && redisTemplate.hasKey(key)){
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            logger.info("mall redis unlock failed", e);
        } finally {
            redisTemplate.delete(key);
        }
        return false;
    }
}
