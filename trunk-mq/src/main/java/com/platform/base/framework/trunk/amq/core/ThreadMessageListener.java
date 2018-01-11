package com.platform.base.framework.trunk.amq.core;

import com.platform.base.framework.trunk.amq.handler.MessageHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息消费者中使用的多线程消息监听服务
 *
 * @author wangjp
 * @data 2016年6月23日下午12:23:46
 */
public abstract class ThreadMessageListener implements MessageListener {

    public static final Logger LOG = LoggerFactory.getLogger(ThreadMessageListener.class);

    // 默认线程池数量
    public final static int    DEFAULT_HANDLE_THREAD_POOL = 100;

    // 提供消息回调调用接口
    private MessageHandler messageHandler;

    private ThreadPoolExecutor executorService;

    public ThreadMessageListener(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public ThreadMessageListener(MessageHandler messageHandler, ThreadPoolExecutor executor) {
        this.messageHandler = messageHandler;
        executorService = null == executor ? newFixedThreadPool(DEFAULT_HANDLE_THREAD_POOL)
            : executor;
    }

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads) {

        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 监听程序中自动调用的方法
     */
    @Override
    public void onMessage(final Message message) {

        executorService.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    LOG.info("接收到消息体 ,线程名称:[{}],时间:[{}],线程池:[{}],队列情况:[{}]",
                        Thread.currentThread().getName(),
                        DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),
                        executorService.getPoolSize(), executorService.getQueue().size());
                    messageHandler.handle(message);
                } catch (Exception e) {
                    LOG.error(" run messageHandler error,msg:{}", e.getMessage());
                }
            }
        });
    }
}
