package com.platform.base.framework.trunk.core.exception.utils;

/**
 * 描述:系统错误,平台错误
 *
 * @author: mylover
 * @Time: 01/03/2017.
 */
public enum TrunkError {

    SYSTEM_ERROR("999999", "系统错误");

    private final String code;
    private final String text;

    TrunkError(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String code() {
        return this.code;
    }

    public String getText() {
        return text;
    }

    public static TrunkError codeOf(String code) {
        for (TrunkError val : TrunkError.values()) {
            if (val.code().equals(code)) {
                return val;
            }
        }
        return null;
    }

}
