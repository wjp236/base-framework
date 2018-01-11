/**
 * 
 */
package com.platform.base.framework.trunk.core.exception;


/**
 * 持久层异常（消息应该是具体的错误描述）
 * 
 * @version 0.1
 */
public class DaoException extends AbstractException {


    /**
     * 格式：message = {"errorCode":"999999",
     * "errorMsg":"未知错误","alarmLevel":"IMPORTANT","customMsg":"商户未注册"}
     *
     * @param message
     */
	public DaoException(String message) {
		super(message);
	}

    @Override
    public ErrorDesc getErrorDesc(Throwable var1) {
        return null;
    }

}
