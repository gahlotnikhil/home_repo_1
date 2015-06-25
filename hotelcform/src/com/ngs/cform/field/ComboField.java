package com.ngs.cform.field;

import java.util.Vector;

public class ComboField extends AbstractFormField {

	public ComboField(String key, Vector<String> valueList) {
		super(key);
		setValueList(valueList);
	}
	
	@Override
	public FieldType getType() {
		return FieldType.COMBO;
	}

}
