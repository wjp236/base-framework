package com.platform.base.frramework.trunk.util.constants;

import java.io.File;

/**
 * Created by mylover on 21/11/2016.
 */
public class TrunkConstans {

    public static final String HOST_NAME = "hostName";

    /**
     * 请求id
     */
    public static final String REQUESTID = "requestId";


    /**
     *  错误码文件名称
     */
    public static final String ERROR_FILE_NAME = "local/ErrorCode.xml";

    /**
     * 非校验url
     */
    public static final String UNCHECKRIGHT_NAME = "local/UnCheckRight.xml";

    /**
     *  配置文件
     */
    public static final String GENERAL_FILE_NAME = "local/GeneralConfig.xml";


    /**
     * view.xml中定义的视图名称
     */
    public static final String JSON_VIEW_NAME = "jsonView";

    /**
     * view.xml中定义的视图名称
     */
    public static final String XML_VIEW_NAME = "xmlView";

    /**
     * XML根元素
     */
    public static final String ROOT_ELEMENT = "Response";


    public static final String SUCCESS = "000000";

    public static final String SUCCESS_MSG = "成功";

    public static final String X_REAL_IP = "x-real-system";


    /**
     * sig失效时间(默认是24小时，毫秒表示，属于不经常变化常量）
     */
    public static final long SIG_DISABLED_TIME = 86400000;

    /**
     *  Script目录
     */
    public static final String SCRIPT_DIR_MATCH = File.separator + "script" + File.separator;

    public static final String AUTH_TYPE_ACCOUNT = "Account";

    public static final String AUTH_TYPE_APP = "Application";

}
