package com.platform.base.framework.trunk.model.utils;


import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 覆写Object的toString()方法，打印子类对象时输出属性和值对
 */
public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 6238288651571870265L;

	@Override
	public String toString() {

		String value = this.getClass().getSimpleName() + ":【";
		try {
			Field[] fs = this.getClass().getDeclaredFields();

			for (int i = 0; i < fs.length; i++) {

				Field f = fs[i];
				if ("serialVersionUID".equals(f.getName())) {
					continue;
				}
				f.setAccessible(true);

				Object val;

				val = f.get(this);
				if (val == null)
					continue;

				value += f.getName() + "=" + val + ";";

			}
		} catch (Exception e) {
			return super.toString();
		}
		return value + "】";
	}
}
