package com.platform.base.framework.trunk.amq.handler;

import javax.jms.Message;

/**
 * 消息回调接口
 *
 * @author QIANG
 * @data 2016年6月23日下午12:22:33
 */
public interface MessageHandler {

    /**
     * 消息回调提供的调用方法
     *
     * @param message
     */
    public void handle(Message message);
}
