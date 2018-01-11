package com.platform.base.frramework.trunk.util.reflect;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类
 * @author xiaojie.zhang
 * @since 2013年8月29日
 */
public class ReflectHelper {
	private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

	/**
	 * 获取obj对象fieldName的Field
	 * @param obj
	 * @param fieldName
	 * @return Field
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {}
		}
		return null;
	}

	/**
	 * 反射SET值到Map中
	 * @return Map<String, Object>
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Map<String, Object> setBeanValueToMap(Object bean) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> params = new HashMap<String, Object>();
		if (bean != null) {
			Field[] fields = bean.getClass().getDeclaredFields();
			// 属性值
			Object beanValue = null;
			for (Field field : fields) {
				// 字段名称
				String fieldName = field.getName();
				if (field.isAccessible()) {
					beanValue = field.get(bean);
				} else {
					field.setAccessible(true);
					beanValue = field.get(bean);
				}
				// 设值
				params.put(fieldName, beanValue);

			}
		}
		return params;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @return Object
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}

	/**
	 * SET对象值到REQUEST中
	 * @param request
	 */
	@SuppressWarnings("unused")
	public static void setBeanToRequest(HttpServletRequest request, Object bean) {
		try {
			Class<?> clazz = bean.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String property = field.getName();
				String methodName = property.substring(0, 1).toUpperCase() + property.substring(1);
				Method m = clazz.getMethod("get" + methodName);
				Object[] arg = null;
				Object value = m.invoke(bean, arg);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		request.setAttribute(bean.getClass().getSimpleName(), bean);
	}

	/**
	 * REQUEST中得到值，设置到对象中
	 * @param bean
	 * @param request
	 */
	public static void getReuqestValueSetBean(Object bean, HttpServletRequest request) {
		if (bean == null) return;
		try {
			PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
			for (int i = 0; i < pds.length; i++) {
				PropertyDescriptor pd = pds[i];
				Method method = PropertyUtils.getWriteMethod(pd);
				if (method == null) continue;
				Object value = request.getParameter(pd.getName());
				if (value != null) {
					if (pd.getPropertyType().equals(String.class)) {
						BeanUtils.setProperty(bean, pd.getName(), value);
					} else if (pd.getPropertyType() == Integer.class || pd.getPropertyType() == int.class) {
						if ("".equals(value)) {
							if (pd.getPropertyType() == int.class) {
								value = new Integer(0);
							} else {
								value = null;
							}
						}
						BeanUtils.setProperty(bean, pd.getName(), value);
					} else if (pd.getPropertyType() == Long.class || pd.getPropertyType() == long.class) {
						if ("".equals(value)) {
							if (pd.getPropertyType() == long.class) {
								value = new Long(0);
							} else {
								value = null;
							}
						}
						BeanUtils.setProperty(bean, pd.getName(), value);
					} else if (pd.getPropertyType() == Double.class || pd.getPropertyType() == double.class) {
						if ("".equals(value)) {
							if (pd.getPropertyType() == double.class) {
								value = new Double(0);
							} else {
								value = null;
							}
						}
						BeanUtils.setProperty(bean, pd.getName(), value);
					}
				}
			}

		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (InvocationTargetException e) {
			logger.error("", e);
		}

	}
}
