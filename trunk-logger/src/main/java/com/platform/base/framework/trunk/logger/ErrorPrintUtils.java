package com.platform.base.framework.trunk.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorPrintUtils {

	/**
	 * 打印异常信息
	 * 
	 * @param exception
	 * @return
	 */
	public static String printStackTrace(Throwable exception) {

		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			exception.printStackTrace(pw);

		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
				}
			}
			if (pw != null) {
				pw.close();
			}

		}
		return sw.toString();
	}
}
