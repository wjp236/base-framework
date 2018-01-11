/**
 * 
 */
package com.platform.base.framework.trunk.core.exception;

/**
 * Controller层的业务异常（消息应该是具体的错误描述）
 * 
 */
public class ControllerException extends ServiceException {

	/**
	 *
	 * @param message
	 */
	public ControllerException(String message) {
		super(message);
	}

}
