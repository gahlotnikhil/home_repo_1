package com.ngs.cform.field;

import java.util.Vector;

public abstract class AbstractFormField {

	public AbstractFormField(String key) {
		this.key = key;
	}

	public enum FieldType {
		TEXT, RADIO, COMBO, CHECK, TEXTAREA
	}

	private String key;
	
	private String value;
	
	private boolean enable = true;

	private Vector<String> valueList;

	public String getKey() {
		return key;
	}

	public Vector<String> getValueList() {
		return valueList;
	}

	public void setValueList(Vector<String> valueList) {
		this.valueList = valueList;
	}

	abstract public FieldType getType();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
