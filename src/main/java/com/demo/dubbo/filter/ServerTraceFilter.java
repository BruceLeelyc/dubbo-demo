package com.demo.dubbo.filter;

import com.google.common.base.Strings;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

@Activate(group = CommonConstants.PROVIDER)
public class ServerTraceFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(ServerTraceFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.info("####className:{}", invocation.getClass().getName());
        logger.info("####methodName:{}", invocation.getMethodName());

        //获取reqId，若没有，则通过UUID生成一个；然后将reqId放到MDC中，便于日志中打印
        String traceId = RpcContext.getContext().getAttachment("traceId");
        traceId = !Strings.isNullOrEmpty(traceId) ? traceId : UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        logger.info("traceId:{}", traceId);
        return invoker.invoke(invocation);
    }
}
