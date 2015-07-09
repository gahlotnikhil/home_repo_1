package com.ngs.cform.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class RecordModel {
	
	private  Logger logger = Logger.getLogger(RecordModel.class);
	
	private Map<String, Object> keyValues = new HashMap<>();
	
	public static String KEY_ID = "id";
	
	public static String KEY_IMAGE = "image";
	
	public Object getValueByKey(String key) {
		return keyValues.get(key);
	}
	
	public void setValueByKey(String key, Object value) {
		keyValues.put(key, value);
	}
	
	public Long getId() {
		Object idObj = keyValues.get(KEY_ID);
		if (idObj != null) {
			try {
				return Long.parseLong(idObj.toString());
			} catch (NumberFormatException e) {
				logger.error("Error occurred while getting id.", e);
			}
		}
		
		return null;
	}
	
}
