package com.ngs.cform.field;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ngs.cform.field.AbstractFormField.FieldType;

public class FormFieldPair {

	private JLabel label; 
	
	private JPanel panel;
	
	private FieldType type;

	public FormFieldPair(JLabel label, JPanel panel, FieldType type) {
		this.label = label;
		this.panel = panel;
		this.type = type;
	}
	
	public JLabel getLabel() {
		return label;
	}

	public JPanel getPanel() {
		return panel;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
}
