package com.platform.base.frramework.trunk.util.json;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mylover on 5/17/16.
 */
public class JsonUtil {

    /**
     * 对象转map
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap4Obj(Object obj) {
        HashMap map = new HashMap();

        try {
            Field[] e = obj.getClass().getDeclaredFields();
            Field[] var6 = e;
            int var5 = e.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Field field = var6[var4];
                String key = field.getName();
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                Object val = field.get(obj);
                if(val == null) {
                    val = "";
                }

                map.put(key, val);
                field.setAccessible(accessFlag);
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return map;
    }

}
