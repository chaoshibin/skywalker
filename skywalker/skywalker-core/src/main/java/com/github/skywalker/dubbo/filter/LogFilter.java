package com.github.skywalker.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.skywalker.annotation.EasyLog;
import com.github.skywalker.common.util.AspectUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 异常日志过滤器
 *
 * @author Chao Shibin
 * @since 2019-10-12 20:55:36
 */
@Slf4j
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class LogFilter implements Filter {

    @SuppressWarnings("unchecked")
    @Override
    public Result invoke(final Invoker<?> invoker, final Invocation invocation) {
        Class clazz = invoker.getInterface();
        Class[] args = invocation.getParameterTypes();
        final Object[] arguments = invocation.getArguments();
        EasyLog easyLogAnnotation = null;
        String methodName = StringUtils.EMPTY;
        Method method = null;
        try {
            method = clazz.getMethod(invocation.getMethodName(), args);
            easyLogAnnotation = AnnotationUtils.findAnnotation(method, EasyLog.class);
            methodName = clazz.getName() + "." + method.getName();
        } catch (Exception e) {
            log.error("获取注解方法异常", e);
        }
        //没有注解不生效
        if (Objects.isNull(easyLogAnnotation)) {
            return invoker.invoke(invocation);
        }
        Stopwatch stopWatch = Stopwatch.createStarted();
        log.info("[{}] RpcMethod={}, 请求报文={}", easyLogAnnotation.title(), methodName, arguments);
        Result result;
        //消费者
        if (RpcContext.getContext().isConsumerSide()) {
            try {
                result = invoker.invoke(invocation);
            } catch (Throwable e) {
                String serverIp = RpcContext.getContext().getRemoteHost();
                log.error("[{}##异常##] serverIp={}, RpcMethod={}, 请求报文={}, 耗时{}", easyLogAnnotation.title(), serverIp, methodName, arguments, stopWatch, e);
                return buildErrorRpcResult(easyLogAnnotation, method);
            }
            log.info("[{}] RpcMethod={}, 请求报文={}, 响应报文={}, 耗时{}", easyLogAnnotation.title(), methodName, arguments, result.getValue(), stopWatch);
            return result;
        }
        //提供者
        result = invoker.invoke(invocation);
        if (result.hasException()) {
            log.error("[{}##异常##] RpcMethod={}, 请求报文={}, 耗时{}", easyLogAnnotation.title(), methodName, arguments, stopWatch, result.getException());
            RpcResult errorRpcResult = buildErrorRpcResult(easyLogAnnotation, method);
            //拷贝附件
            errorRpcResult.setAttachments(result.getAttachments());
            return errorRpcResult;
        }
        log.info("[{}] RpcMethod={}, 请求报文={}, 响应报文={}, 耗时{}", easyLogAnnotation.title(), methodName, arguments, result.getValue(), stopWatch);
        return result;
    }

    private RpcResult buildErrorRpcResult(EasyLog easyLogAnnotation, Method method) {
        Class<?> returnType = method.getReturnType();
        //构建错误对象
        Object errorResult = AspectUtil.buildErrorResult(returnType, easyLogAnnotation.codeField(), easyLogAnnotation.msgField());
        return new RpcResult(errorResult);
    }
}
