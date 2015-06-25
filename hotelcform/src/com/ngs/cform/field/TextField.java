package com.ngs.cform.field;

public class TextField extends AbstractFormField {

	public TextField(String key) {
		super(key);
	}
	
	@Override
	public FieldType getType() {
		return FieldType.TEXT;
	}

}
