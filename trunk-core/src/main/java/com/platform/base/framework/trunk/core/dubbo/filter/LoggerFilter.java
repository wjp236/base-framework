package com.platform.base.framework.trunk.core.dubbo.filter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.platform.base.frramework.trunk.util.text.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * dubbo日志跟踪过滤器
 */
public class LoggerFilter implements Filter {

    private Logger              LOG         = LoggerFactory.getLogger(getClass());

    /**
     * 请求追踪ID
     */
    private final static String LOGID_KEY   = "requestId";
    private final static String IS_PROVIDER = "provider";
    private final static String IS_CONSUMER = "consumer";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        long startTime = System.currentTimeMillis();

        RpcContext rpcContext = RpcContext.getContext();
        URL url = rpcContext.getUrl();

        LOG.debug("url=[{}]", url);

        String who = url.getParameter("side");

        if (IS_CONSUMER.equals(who)) {
            /*如果是消费者调用服务的情况,日志ID应该从serviceAOP中生成
             * 直接从MDC中获取,如果出现没有的情况,则重新生成并赋值*/
            String logID = MDC.get(LOGID_KEY);
            if (logID == null) {
                /*如果没有reguestId就重新生成一个,这种情况可能会发生漏传的情况,需要在开始就要控制好*/
                logID = UUIDUtils.randomUUID().toString();
                rpcContext.setAttachment(LOGID_KEY, logID);
                MDC.put(LOGID_KEY, logID);
            } else {
                rpcContext.setAttachment(LOGID_KEY, logID);
            }

            LOG.info("consumer: source[{}, {}], target[{}],arguments[{}]",
                rpcContext.getLocalAddressString(), url.getParameter("application"),
                rpcContext.getRemoteAddressString(), rpcContext.getArguments());

        } else if (IS_PROVIDER.equals(who)) {
            String logID = rpcContext.getAttachment(LOGID_KEY);
            /*如果是服务提供方,日志ID从dubbo.RpcContext中获取*/
            if (logID == null) {
                /*如果没有reguestId就重新生成一个,这种情况可能会发生漏传的情况,需要在开始就要控制好*/
                logID = UUIDUtils.randomUUID().toString();
                rpcContext.setAttachment(LOGID_KEY, logID);
                MDC.put(LOGID_KEY, logID);
            } else {
                MDC.put(LOGID_KEY, logID);
            }

            LOG.info("provider: source[{}], target[{}, {}],arguments[{}]",
                rpcContext.getRemoteAddressString(), rpcContext.getLocalAddressString(),
                url.getParameter("application"), rpcContext.getArguments());
        }

        Result result = invoker.invoke(invocation);

        long stopTime = System.currentTimeMillis();

        LOG.debug("service is finished, waste time {}ms ,result[{}]", stopTime - startTime, result);
        if (IS_PROVIDER.equals(who)) {
            /*避免同线程保留上一个线程日志ID,这里在服务调用结束后删除当前日志ID*/
            MDC.remove(LOGID_KEY);

        }

        return result;
    }

}
