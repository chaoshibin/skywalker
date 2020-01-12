package com.github.skywalker.distribute.lock;

import com.github.skywalker.common.enums.ResultEnum;
import com.github.skywalker.common.utils.AspectUtils;
import com.github.skywalker.common.utils.CodecUtils;
import com.github.skywalker.common.utils.StringFormatUtils;
import com.github.skywalker.distribute.lock.service.DistributeLockService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author CHAO 2019/4/16
 */
@Slf4j
@Aspect
@Order(-1)
public class EasyLockAspect {
    private static final String LOCK_ERROR_MSG_FORMAT = ResultEnum.DISTRIBUTE_LOCK_FAIL.getMsg() + ",lockKey=%s";
    private Map<LockEnum, DistributeLockService> lockServiceMap = Maps.newHashMap();

    @Resource
    public void setLockService(DistributeLockService service) {
        lockServiceMap.put(service.type(), service);
    }

    @Pointcut("@annotation(com.github.skywalker.distribute.lock.EasyLock)")
    public void pointCut() {
        //pointcut
    }

    @Around("pointCut()")
    public Object methodProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        EasyLock annotation = AspectUtils.getAnnotationOnMethod(joinPoint, EasyLock.class);
        String lockKey = buildLockKey(joinPoint, annotation);
        String requestId = CodecUtils.createUuid();
        log.debug("redis分布式锁lockKey={},requestId={}", lockKey, requestId);
        DistributeLockService lockService = lockServiceMap.get(annotation.type());
        if (!lockService.lock(lockKey, requestId, annotation.expireSeconds())) {
            //返回类型
            String msg = String.format(LOCK_ERROR_MSG_FORMAT, lockKey);
            log.warn("获取redis分布式锁失败，lockKey={}", lockKey);
            Class<?> returnType = AspectUtils.getReturnType(joinPoint);
            //争夺锁失败，构建失败对象
            return AspectUtils.buildResult(returnType, annotation.codeField(), annotation.msgField(), annotation.code(), msg);
        }
        Object result = joinPoint.proceed();
        if (!lockService.unLock(lockKey, requestId)) {
            log.error("redis分布式锁解锁失败，lockKey={},requestId={}", lockKey, requestId);
            throw new IllegalStateException("redis分布式锁解锁失败");
        }
        return result;
    }

    private String buildLockKey(JoinPoint joinPoint, EasyLock annotation) {
        String contactKey = AspectUtils.contactBusinessValue(joinPoint, annotation.key());
        return StringFormatUtils.format(annotation.name(), contactKey);
    }
}
