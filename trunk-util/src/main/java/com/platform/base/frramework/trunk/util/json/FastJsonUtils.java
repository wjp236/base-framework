package com.platform.base.frramework.trunk.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by mylover on 25/11/2016.
 */
public class FastJsonUtils {

    /**
     * obj to jsonStr
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * jsonStr to JSONObject
     * @param var1
     * @return
     */
    public static JSONObject parseObject(String var1) {
        return JSON.parseObject(var1);
    }

    /**
     * jsonStr to obj
     * @param var1
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String var1, Class<T> clazz) {
        return JSON.parseObject(var1, clazz);
    }

}
