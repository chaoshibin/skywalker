package com.github.skywalker.log;

import com.github.skywalker.annotation.EasyLog;
import com.github.skywalker.common.utils.AspectUtils;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @author Chao Shibin 2019/4/16
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE)
public class EasyLogAspect {

    @Pointcut("@annotation(com.github.skywalker.annotation.EasyLog)")
    public void pointCut() {
        //pointcut
    }

    @Around("pointCut()")
    public Object methodProcess(ProceedingJoinPoint joinPoint) {
        //注解方法
        Method sourceMethod = AspectUtils.getMethod(joinPoint);
        EasyLog easyLogAnnotation = sourceMethod.getAnnotation(EasyLog.class);
        //获取日志标识
        String title = easyLogAnnotation.title();
        String methodName = AspectUtils.getMethodName(joinPoint);
        Stopwatch stopWatch = Stopwatch.createStarted();
        try {
            log.info("[{}] method={}，请求报文={}", title, methodName, joinPoint.getArgs());
            Object result = joinPoint.proceed();
            log.info("[{}] method={}，请求报文={}，响应报文={}，耗时{}", title, methodName, joinPoint.getArgs(), result, stopWatch);
            return result;
        } catch (Throwable e) {
            log.error("[{}异常] method={}，请求报文={}，耗时{}", title, methodName, joinPoint.getArgs(), stopWatch, e);
            //返回类型
            Class<?> returnType = AspectUtils.getReturnType(joinPoint);
            // 返回码属性域
            String codeField = easyLogAnnotation.codeField();
            // 返回信息属性域
            String msgField = easyLogAnnotation.msgField();
            return AspectUtils.buildErrorResult(returnType, codeField, msgField);
        }
    }
}
