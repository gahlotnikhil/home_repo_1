package com.ngs.cform.model;

import java.util.HashMap;
import java.util.Map;

public class RecordModel {
	
	private Map<String, Object> keyValues = new HashMap<>();
	
	public static String KEY_ID = "id";
	
	public static String KEY_IMAGE = "image";
//	
//	public static String KEY_HOTEL_NAME = "hotel_name";
//	public static String KEY_HOTEL_ADDRESS = "hotel_address";
//	public static String KEY_HOTEL_PHONE = "hotel_phone";
//	
// 	public static String KEY_CUSTOMER_NAME = "customer_name";
//	public static String KEY_DATE_OF_BIRTH = "date_of_birth";
//	public static String KEY_CUSTOMER_ADDRESS = "customer_address";
//	
//	public static String KEY_NATIONALITY = "nationality";
//	public static String KEY_PASSPORT_NO = "passport_no";
//	public static String KEY_PASSPORT_ISSUE_PLACE = "passport_issue_place";
//	public static String KEY_PASSPORT_ISSUE_DATE = "passport_issue_date";
//	public static String KEY_PASSPORT_EXPIRY_DATE = "passport_expiry_date";
//	public static String KEY_PASSPORT_ISSUE_AUTHORITY = "passport_issue_authority";
//	
//	public static String KEY_VISA_NUMBER = "visa_number";
//	public static String KEY_VISA_ISSUE_DATE = "visa_issue_date";
//	public static String KEY_VISA_EXPIRY_DATE = "visa_expiry_date";
//	public static String KEY_VISA_TYPE = "visa_type";
//	public static String KEY_VISA_ISSUE_PLACE = "visa_issue_place";
//	
//	public static String KEY_ARRIVED_FROM = "arrived_from";
//	public static String KEY_ARRIVAL_DATE = "arrival_date";
//	public static String KEY_ARRIVAL_DATE_HOTEL = "arrival_date_hotel";
//	public static String KEY_ARRIVAL_TIME = "arrival_time";
//	public static String KEY_STAY_DURATION = "stay_duration";
//	
//	public static String KEY_EMPLOYED_IN_INDIA = "employed_in_india";
//	public static String KEY_PURPOSE_OF_VISIT = "purpose_of_visit";
//	public static String KEY_NEXT_DESTINATION = "next_destination";
//	
//	
//	//For image
//	public static String KEY_IMAGE = "image";
	
	
//	public static List<String> getHeadingKeyList() {
//		return headingKeyList;
//	}
//	
//	public static List<String> getTableKeyList() {
//		List<String> headingKeyList = new ArrayList<String>();
//		headingKeyList.add(KEY_ID);
//		headingKeyList.add(KEY_HOTEL_NAME);
//		headingKeyList.add(KEY_CUSTOMER_NAME);
//		headingKeyList.add(KEY_NATIONALITY);
//		headingKeyList.add(KEY_PASSPORT_NO);
//		headingKeyList.add(KEY_DATE_OF_BIRTH);
//		return headingKeyList;
//	}
//	
//	public static List<String> getMandatoryKeyList() {
//		List<String> headingKeyList = new ArrayList<String>();
//		headingKeyList.add(KEY_HOTEL_NAME);
//		headingKeyList.add(KEY_CUSTOMER_NAME);
//		headingKeyList.add(KEY_NATIONALITY);
//		headingKeyList.add(KEY_PASSPORT_NO);
//		headingKeyList.add(KEY_DATE_OF_BIRTH);
//		return headingKeyList;
//	}
	
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}
