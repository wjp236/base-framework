package com.platform.base.frramework.trunk.util.constants;

/**
 * 服务相应码
 * @author LL
 *
 */
public enum ResponseCode {

    SUCCESS("000000", "成功"),
    FAILE("999999", "失败"),
    UNKNOWN("-1", "未知错误"),
    SYS_SITE_CLOSED("001000", "暂停服务"),
    SYS_SITE_MAINTENANCE("001001", "系统维护暂停服务"),
    SYS_ACCESS_NOTALLOWED("001002", "系统不允许访问"),
    SYS_ACCESS_UNAUTHORIZED("001003", "未经授权不允许访问"),
    SYS_ACCESS_IPBOUNDED("001004", "系统不允许访问，IP地址受限"),
    SYS_ACCESS_INVALID("001005", "非法访问请求"),
    SYS_PARAM_INVALID("001006", "非法请求参数"),
    SYS_SESSION_EXPIRED("001007", "SESSION过期失效"),
    SYS_SESSION_INVALID("001008", "SESSION无效"),
    SYS_DB_ERROR("001009", "数据库错误");

    private String code;
    private String desc;// 描述

    private ResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {

        return desc;
    }

    public void setDesc(String desc) {

        this.desc = desc;
    }

    public String getCode() {

        return code;
    }
}
