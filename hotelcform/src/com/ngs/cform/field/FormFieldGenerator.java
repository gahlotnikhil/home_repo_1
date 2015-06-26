package com.ngs.cform.field;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.ngs.cform.model.RecordModel;
import com.ngs.cform.resource.ResourceConfig;

public class FormFieldGenerator {
	
	private Properties properties;
	
	private Map<String, FormFieldPair> keyFieldMap;
	
	private ResourceConfig resourceConfig;
	
	private long recordId;
	
	public FormFieldGenerator(ResourceConfig resourceConfig, Properties properties) {
		this.resourceConfig = resourceConfig;
		this.properties = properties;
	}
	
	private List<AbstractFormField> getOrderedFieldList(RecordModel record, boolean editMode) {
		List<AbstractFormField> fields = resourceConfig.getFields("form-fields");
		for (AbstractFormField field : fields) {
			if (record != null && record.getValueByKey(field.getKey()) != null) {
				field.setValue(record.getValueByKey(field.getKey()).toString());
			}
			field.setEnable(editMode);
		}
		return fields;
	}
	
	public static void main(String[] args) {
		//new FormFieldGenerator("The hotel");
	}

	private JLabel generateLabel(String key) {
		Object valueObj = properties.get(key);
		String val = key;
		if (valueObj != null) {
			val = valueObj.toString();
		}
		JLabel label = new JLabel(val);
		label.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//label.setPreferredSize(new Dimension(100, 20));
		return label;
	}
	
	private JPanel generateTextFieldPanel(AbstractFormField orderedField) {
		JPanel panel = new JPanel();
		JTextField text = new JTextField();
		text.setPreferredSize(new Dimension(300, 20));
		text.setText(orderedField.getValue());
		text.setEnabled(orderedField.isEnable());
		text.setBorder(BorderFactory.createEmptyBorder());
		panel.add(text);
		return panel;
	}
	
	private JPanel generateTextAreaPanel(AbstractFormField orderedField) {
		JPanel panel = new JPanel();
		JTextArea text = new JTextArea();
		text.setPreferredSize(new Dimension(300, 400));
		text.setText(orderedField.getValue());
		text.setEnabled(orderedField.isEnable());
		panel.add(text);
		return panel;
	}
	
	private JPanel generateComboFieldPanel(Vector<String> selectStrings) {
		JPanel panel = new JPanel();
		JComboBox<String> combo = new JComboBox<>(selectStrings);
		panel.add(combo);
		return panel;
	}
	
	private JPanel generateRadioFieldPanel(Vector<String> selectStrings) {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		for (String str: selectStrings) {
			JRadioButton rButton = new JRadioButton(str);
			group.add(rButton);
			
			panel.add(rButton);
		}
		
		return panel;
	}
	
	private JPanel generateCheckFieldPanel(Vector<String> selectStrings) {
		JPanel panel = new JPanel();
		for (String str: selectStrings) {
			JCheckBox check = new JCheckBox(str);
			panel.add(check);
		}
		return panel;
	}
	
	public boolean isMandatoryAvailable(RecordModel record) {
//		for (String key: resourceConfig.getMandatoryFieldKeys()) {
//			Object val = record.getValueByKey(key);
//			if (val == null || val.toString().isEmpty()) {
//				return false;
//			}
//		}
		return true;
	}
	
	public Map<String, FormFieldPair> generateFields(RecordModel record, boolean editMode) {
		keyFieldMap = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return 1;
			}
		});
		
		for (AbstractFormField orderedField : getOrderedFieldList(record, editMode)) {
			JPanel panel = null;
			switch (orderedField.getType()) {
				case TEXT: {
					panel = generateTextFieldPanel(orderedField);
					break;
				}
				case TEXTAREA: {
					panel = generateTextAreaPanel(orderedField);
					break;
				}
				case CHECK: {
					panel = generateCheckFieldPanel(orderedField.getValueList());
					break;
				}
				case COMBO: {
					panel = generateComboFieldPanel(orderedField.getValueList());
					break;
				}
				case RADIO: {
					panel = generateRadioFieldPanel(orderedField.getValueList());
					break;
				}
				default: {
					panel = generateTextFieldPanel(orderedField);
					break;
				}
			}
			
			panel.setLayout(new FlowLayout());
			keyFieldMap.put(orderedField.getKey(), 
					new FormFieldPair(generateLabel(orderedField.getKey()), panel, orderedField.getType()));
		}
		
		if (record != null) {
			recordId = record.getId();
		}
		return keyFieldMap;
	}
	
	public RecordModel getDataModel() {
		RecordModel record = new RecordModel();
		
		for (Map.Entry<String, FormFieldPair> entry : keyFieldMap.entrySet()) {
			String key = entry.getKey();
			
			Object value = null;
			
			switch (entry.getValue().getType()) {
				case TEXT: {
					JTextField comp = (JTextField) entry.getValue().getPanel().getComponent(0);
					value = comp.getText();
					break;
				}
				case TEXTAREA: {
					JTextArea comp = (JTextArea) entry.getValue().getPanel().getComponent(0);
					value = comp.getText();
					break;
				}
				case CHECK: {
					StringBuilder strBld = new StringBuilder();
					for (Component comp : entry.getValue().getPanel().getComponents()) {
						JCheckBox check = (JCheckBox)comp;
						if (check.isSelected()) {
							strBld.append(',');
							strBld.append(check.getText());
						}
					}
					
					value = strBld.substring(1);
					break;
				}
				case COMBO: {
					JComboBox comp = (JComboBox) entry.getValue().getPanel().getComponent(0);
					value = comp.getSelectedItem();
					break;
				}
				case RADIO: {
					for (Component comp : entry.getValue().getPanel().getComponents()) {
						JRadioButton radio = (JRadioButton)comp;
						if (radio.isSelected()) {
							value = radio.getText();
							break;
						}
					}
					break;
				}
				default: {
					JTextField comp = (JTextField) entry.getValue().getPanel().getComponent(0);
					value = comp.getText();
					break;
				}
			}
			
			record.setValueByKey(key, value);
		}
		
		record.setValueByKey(RecordModel.KEY_ID, recordId);
		
		return record;
	}
}
