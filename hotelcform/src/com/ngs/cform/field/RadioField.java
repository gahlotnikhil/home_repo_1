package com.ngs.cform.field;

import java.util.Vector;

public class RadioField extends AbstractFormField {

	public RadioField(String key, Vector<String> valueList) {
		super(key);
		setValueList(valueList);
	}
	
	@Override
	public FieldType getType() {
		return FieldType.RADIO;
	}

}
