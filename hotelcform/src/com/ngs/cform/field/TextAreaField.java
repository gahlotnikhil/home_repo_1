package com.ngs.cform.field;


public class TextAreaField extends AbstractFormField {

	public TextAreaField(String key) {
		super(key);
	}
	
	@Override
	public FieldType getType() {
		return FieldType.TEXTAREA;
	}

}
