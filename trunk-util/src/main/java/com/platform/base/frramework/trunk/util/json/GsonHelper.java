package com.platform.base.frramework.trunk.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Gson帮助类
 * 
 */
public class GsonHelper {
	
	public static Map<String, String> convertMapFromBean(Object bean) {
		Gson gson = new Gson();

		return gson.fromJson(gson.toJson(bean),
				new TypeToken<Map<String, String>>() {
				}.getType());
	}

	public static Map<String, String> convertMapFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	public static List<Map<String, String>> convertToList(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {
		}.getType());
	}

	public static String convertMapToJson(Map<String, String> map) {
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	public static String toJsonExcludeBlank(Object bean) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(bean);
	}
	
	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}
	
	public static Object jsonToJavaBean(String body,Class<?> classType) {
		Gson gson = new Gson();
		return gson.fromJson(body, classType);
	}
	/**
	 * @Title: convertJsonArrayToList 
	 * @Description: 将jsonArray字符串转成list
	 * @author chenming<chenmingf@ennew.cn>
	 * @param jsonArray
	 * @return List    返回类型
	 */
	public static List convertJsonArrayToList(String jsonArray){
		Gson gson = new Gson();
		return gson.fromJson(jsonArray,new TypeToken<List>(){}.getType());
	}
}
