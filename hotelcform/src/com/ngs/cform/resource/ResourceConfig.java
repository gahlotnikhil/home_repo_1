package com.ngs.cform.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ngs.cform.field.AbstractFormField;
import com.ngs.cform.field.CheckField;
import com.ngs.cform.field.ComboField;
import com.ngs.cform.field.RadioField;
import com.ngs.cform.field.TextAreaField;
import com.ngs.cform.field.TextField;
import com.ngs.cform.field.AbstractFormField.FieldType;
import com.ngs.cform.util.GeneralUtils;

public class ResourceConfig {
	
	public static String RESOURCE_XML = "../resource.xml";
	
	private Document document;
	
	public ResourceConfig(String resourceXML) {
		if (resourceXML == null || resourceXML.trim().isEmpty()) {
			resourceXML = RESOURCE_XML;
		}
		loadDocument(resourceXML);
	}
	
	private Collection<Node> getFieldNodes(String category) {
		
		List<Node> dataKeys = new ArrayList<Node>();
		NodeList dList = document.getElementsByTagName(category);
		 
		if (dList.getLength() > 0) {
			Node dListNode = dList.item(0);
			NodeList dataKeyNodes = dListNode.getChildNodes();
			for (int i=0 ; i < dataKeyNodes.getLength() ; i++) {
				Node node = dataKeyNodes.item(i);
				dataKeys.add(node);
			}
		}
		
		return dataKeys;
	}
	
	public List<String> getSearchFieldKeys() {
		return getFieldKeys("search-fields");
	}
	
	public List<String> getSearchFieldLabelKeys() {
		return getFieldLabelKeys("search-fields");
	}
	
	public List<String> getMandatoryFieldKeys() {
		return getFieldKeys("mandatory-fields");
	}
	
	public List<String> getDataFieldKeys() {
		return getFieldKeys("data-fields");
	}
	
	public List<String> getFieldLabelKeys(String category) {
		List<String> fieldLabelKeys = new ArrayList<>();
		Collection<String> keys = getFieldKeys(category);
		
		for (Node node: getFieldNodes("fields")) {
			if ("field".equalsIgnoreCase(node.getNodeName())) {
				Node key = getChildNode(node, "key");
				if (keys.contains(getNodeValue(key))) {
					Node labelKeyNode = getChildNode(node, "label-key");
					String labelKey = getNodeValue(labelKeyNode);
					fieldLabelKeys.add(labelKey);
				}
			}
		}
		
		return fieldLabelKeys;
	}
	
	private List<String> getFieldKeys(String category) {
		List<String> keys = new ArrayList<String>();
		for (Node node: getFieldNodes(category)) {
			if ("key".equalsIgnoreCase(node.getNodeName())) {
				keys.add(getNodeValue(node));
			}
		}
		
		return keys;
	}
	
	public List<AbstractFormField> getFields(String category) {
		List<AbstractFormField> fields = new ArrayList<>();
		Collection<String> keys = getFieldKeys(category);
		
		for (Node node: getFieldNodes("fields")) {
			if ("field".equalsIgnoreCase(node.getNodeName())) {
				Node key = getChildNode(node, "key");
				if (keys.contains(getNodeValue(key))) {
					fields.add(constructField(node));
				}
			}
		}
		
		return fields;
	}
	
	private AbstractFormField constructField(Node fieldNode) {
		Node type = getChildNode(fieldNode, "type");
		Node key = getChildNode(fieldNode, "key");
		Node value = getChildNode(fieldNode, "value");
		Node enable = getChildNode(fieldNode, "enable");
		Node valueList = getChildNode(fieldNode, "value-list");
		
		Vector<String> listValues = new Vector<String>();
		if (valueList != null) {
			NodeList valueListNodeList = valueList.getChildNodes();
			for (int i=0 ; i < valueListNodeList.getLength() ; i++) {
				Node node = valueListNodeList.item(i);
				if ("value".equalsIgnoreCase(node.getNodeName())) {
					listValues.add(getNodeValue(node));
				}
			}
		}
		
		AbstractFormField field = null;
		if (FieldType.TEXT.toString().equalsIgnoreCase(getNodeValue(type))) {
			field = new TextField(getNodeValue(key));
		} else if (FieldType.TEXTAREA.toString().equalsIgnoreCase(getNodeValue(type))) {
			field = new TextAreaField(getNodeValue(key));
		} else if (FieldType.RADIO.toString().equalsIgnoreCase(getNodeValue(type))) {
			field = new RadioField(getNodeValue(key), listValues);
		} else if (FieldType.CHECK.toString().equalsIgnoreCase(getNodeValue(type))) {
			field = new CheckField(getNodeValue(key), listValues);
		} else if (FieldType.COMBO.toString().equalsIgnoreCase(getNodeValue(type))) {
			field = new ComboField(getNodeValue(key), listValues);
		}
		
		if (field != null) {
			field.setEnable(!"false".equalsIgnoreCase(getNodeValue(enable)));
			field.setValue(getNodeValue(value));
		}
		
		return field;
	}
	
	private String getNodeValue(Node node) {
		if (node == null || node.getTextContent() == null) {
			return "";
		}
		return node.getTextContent().trim();
	}
	
	private Node getChildNode(Node node, String childName) {
		Node found = null;
		
		NodeList childNodes = node.getChildNodes();
		for (int i=0 ; i < childNodes.getLength() ; i++) {
			Node child = childNodes.item(i);
			if (childName.equalsIgnoreCase(child.getNodeName())) {
				found = child;
				break;
			}
		}
		
		return found;
	}

	private void loadDocument(String resourceXML) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			document = db.parse(new File(GeneralUtils.getResourcePath(resourceXML).getPath()));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
