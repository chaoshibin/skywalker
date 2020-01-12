package com.github.skywalker.cache;


import com.github.skywalker.common.constant.StrConst;
import com.github.skywalker.common.utils.AspectUtils;
import com.github.skywalker.common.utils.StringFormatUtils;
import com.github.skywalker.redis.service.RedisService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author CHAO 2019/4/16
 */
@Slf4j
@Aspect
@Order
public class EasyCacheAspect {
    @Resource
    private RedisService redisService;

    @Pointcut("@annotation(com.github.skywalker.cache.EasyCachePut)")
    public void cachePutPointCut() {
        //pointcut
    }

    @Pointcut("@annotation(com.github.skywalker.cache.EasyCacheExpire)")
    public void cacheExpirePointCut() {
        //pointcut
    }

    @Around("cachePutPointCut()")
    public Object cachePutProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheInfo cacheInfo = buildCachePutInfo(joinPoint);
        Object cacheValue = redisService.get(cacheInfo.getKey(), AspectUtils.getReturnType(joinPoint));
        if (Objects.nonNull(cacheValue)) {
            //1.1和1.2处协同解决缓存击穿     1.1
            return this.defaultIfNull(cacheValue);
        }
        return this.doCachePutProceed(joinPoint, cacheInfo);
    }

    @Around("cacheExpirePointCut()")
    public Object cacheExpireProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        //注解
        EasyCacheExpire annotation = AspectUtils.getMethod(joinPoint).getAnnotation(EasyCacheExpire.class);
        //缓存key
        String cacheKey = AspectUtils.contactBusinessValue(joinPoint, annotation.key());
        redisService.delete(this.buildKey(annotation.cacheName(), cacheKey));
        return joinPoint.proceed();
    }

    private Object doCachePutProceed(ProceedingJoinPoint joinPoint, CacheInfo cacheInfo) throws Throwable {
        Object result = joinPoint.proceed();
        if (Objects.nonNull(result) || cacheInfo.isCacheNull()) {
            // 1.1和1.2处协同解决缓存击穿      1.2
            Serializable value = (Serializable) this.putIfNull(result);
            redisService.set(cacheInfo.getKey(), value, cacheInfo.getExpireSeconds());
        }
        return result;
    }

    private Object putIfNull(Object value) {
        return Objects.isNull(value) ? StrConst.NULL : value;
    }

    private Object defaultIfNull(Object value) {
        return Objects.equals(StrConst.NULL, value) ? null : value;
    }

    private String buildKey(String cacheName, String cacheKey) {
        return StringFormatUtils.format(cacheName, cacheKey);
    }

    private CacheInfo buildCachePutInfo(ProceedingJoinPoint pjp) {
        EasyCachePut annotation = AspectUtils.getMethod(pjp).getAnnotation(EasyCachePut.class);
        long expireSeconds = annotation.expireSeconds();
        if (expireSeconds == 0L) {
            //随机数防止缓存雪崩 30-60分钟
            expireSeconds = RandomUtils.nextLong(1800, 3600);
        }
        //缓存key
        String contactKey = AspectUtils.contactBusinessValue(pjp, annotation.key());
        String key = this.buildKey(annotation.cacheName(), contactKey);
        return CacheInfo.builder().key(key).expireSeconds(expireSeconds).cacheNull(annotation.cacheNull()).build();
    }

    @Setter
    @Getter
    @Builder
    private static class CacheInfo {
        private String key;
        private long expireSeconds;
        private boolean cacheNull;
    }
}
