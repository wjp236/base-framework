package com.platform.base.frramework.trunk.util.constants;

/**
 * HTTP 公共返回状态码
 *
 */
public enum HttpCode {

    OK(200, "成功"),

    BAD_REQUEST(400, "错误请求"),

    FORBIDDEN(403, "服务拒绝执行"),

    INTERNAL_ERROR(500, "服务器内部错误"),

    NOT_SUPPORTED(505, "非法连接"),

    PAGE_NOT_FOUND(404, "页面未找到"),

    UNKNOWN(-1, "未知错误");

    private int    code;

    private String desc;// 描述

    private HttpCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {

        return desc;
    }

    public void setDesc(String desc) {

        this.desc = desc;
    }

    public int getCode() {

        return code;
    }
}
