package com.platform.base.frramework.trunk.util.serialize;

import java.io.*;

/**
 * JDK序列化工具类
 * @author wangjp
 * @since 2014年3月6日
 */
public class JdkSerializeUtils {

    /**
     * 序列化
     * @param object
     * @return
     */
	public static byte[] serialize(Object object) {
		if (object == null) return new byte[0];
		if (!(object instanceof Serializable)) {
			throw new IllegalArgumentException(object.getClass().getSimpleName() + " requires a Serializable payload " + "but received an object of type ["
					+ object.getClass().getName() + "]");
		}
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(128)) {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
			return baos.toByteArray();
		} catch (Throwable e) {
			throw new RuntimeException("Failed to serialize object using " + object.getClass().getSimpleName(), e);
		}
	}

    /**
     * 反序列化
     * @param data
     * @return
     */
	public static Object deserialize(byte[] data) {
		if (data == null || data.length == 0) return null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data);) {
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Throwable e) {
			throw new RuntimeException("Failed to deserialize payload. " + "Is the byte array a result of corresponding serialization for Class ?", e);
		}
	}
}
