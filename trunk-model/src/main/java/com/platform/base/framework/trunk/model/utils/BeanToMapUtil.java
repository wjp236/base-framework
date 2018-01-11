package com.platform.base.framework.trunk.model.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: bean转map
 *
 * @author: mylover
 * @Time: 09/08/2017.
 */
public class BeanToMapUtil {

    public Map<String, Object> generateMap() throws RuntimeException {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Class type = this.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(this, new Object[0]);
                    if (result != null) {
                        if ("parameterMap".equals(propertyName)) {
                            map.putAll((Map<String, Object>) result);
                        } else {
                            map.put(propertyName, result);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return map;
    }

}
