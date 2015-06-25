package com.ngs.cform.field;

import java.util.Vector;

public class CheckField extends AbstractFormField {

	public CheckField(String key, Vector<String> valueList) {
		super(key);
		setValueList(valueList);
	}
	
	@Override
	public FieldType getType() {
		return FieldType.CHECK;
	}

}
