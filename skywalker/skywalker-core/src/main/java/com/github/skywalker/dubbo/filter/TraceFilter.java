package com.github.skywalker.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.github.skywalker.common.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * <p>
 *
 * </p>
 *
 * @author Chao Shibin
 * @since 2019/10/8 11:17
 **/
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class TraceFilter implements Filter {
    public static final String REQUEST_ID = "requestId";
    public static final String SERVER_IP = "serverIp";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (RpcContext.getContext().isProviderSide()) {
            //提供方
            String requestId = RpcContext.getContext().getAttachment(REQUEST_ID);
            String serverIp = RpcContext.getContext().getAttachment(SERVER_IP);
            if (StringUtils.isBlank(requestId)) {
                String newRequestId = CodecUtils.createUuid();
                MDC.put(REQUEST_ID, newRequestId);
            }
            if (StringUtils.isBlank(serverIp)) {
                String newServerIp = RpcContext.getContext().getRemoteHost();
                MDC.put(SERVER_IP, newServerIp);
            }
        } else {
            //调用方,使用拦截器初始化 @see com.easysui.web.LogInterceptor
            String requestId = MDC.get(REQUEST_ID);
            String serverIp = MDC.get(SERVER_IP);
            if (StringUtils.isNotBlank(requestId)) {
                RpcContext.getContext().setAttachment(REQUEST_ID, requestId);
            } else {
                String newRequestId = CodecUtils.createUuid();
                MDC.put(REQUEST_ID, newRequestId);
                RpcContext.getContext().setAttachment(REQUEST_ID, newRequestId);
            }
            if (StringUtils.isNotBlank(serverIp)) {
                RpcContext.getContext().setAttachment(SERVER_IP, serverIp);
            } else {
                String newServerIp = RpcContext.getContext().getRemoteHost();
                MDC.put(SERVER_IP, newServerIp);
                RpcContext.getContext().setAttachment(SERVER_IP, newServerIp);
            }
        }
        return invoker.invoke(invocation);
    }
}