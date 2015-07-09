package com.ngs.cform.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.ngs.cform.model.RecordModel;


public class WordManager {
	
	private  Logger logger = Logger.getLogger(WordManager.class);
	
	public WordManager(File template, Properties properties) {
		this.template = template;
		initialize(template);
		this.properties = properties;
	}
	
	private File template;
	
	private CustomXWPFDocument templateDoc;
	
	private Properties properties;
	
	private void initialize(File template) {
		try {
			FileInputStream is = new FileInputStream(template);
			templateDoc = new CustomXWPFDocument(is);
			try {
				is.close();
			} catch (Exception e) {
				logger.error("Error occurred while closing stream.", e);
			}
		} catch (IOException e) {
			logger.error("Error occurred while initialiaing doc template.", e);
		}
	}
	
	public XWPFDocument createWordDoc(RecordModel model, byte[] image) {
		// Make sure document has the original template and not use the overwritten one
		initialize(template);
		
		for (XWPFTable table : templateDoc.getTables()) {
			for (XWPFTableRow row : table.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					for (XWPFParagraph para : cell.getParagraphs()) {
						
						boolean imagePlaceholder = false;
						
						if (!para.getRuns().isEmpty()) {
							StringBuilder plcHolderBld = new StringBuilder();
							String placeholder = "";
							for (XWPFRun run : para.getRuns()) {
								plcHolderBld.append(run.getText(0));
							}
							
							placeholder = plcHolderBld.toString();
							
							if (placeholder != null && placeholder.startsWith("$")) {
								if (placeholder.startsWith("$label_")) {
									placeholder = placeholder.substring("$label_".length());
									
									Object placeholderValObject = properties.get(placeholder);
									if (placeholderValObject != null) {
										removeRuns(para);
										XWPFRun run = para.createRun();
										run.setText(placeholderValObject.toString(), 0);
									}
								} else {
									// expression identified
									placeholder = placeholder.substring(1);
									if(RecordModel.KEY_IMAGE.equals(placeholder)) {
										imagePlaceholder = true;
									} else {
										Object placeholderValObject = model.getValueByKey(placeholder);
										if (placeholderValObject != null) {
											removeRuns(para);
											XWPFRun run = para.createRun();
											run.setText(placeholderValObject.toString(), 0);
										}
									}
								}
							}
						}
						
						if (imagePlaceholder && image != null) {
							try {
								removeRuns(para);
//								String imgFile = "avatar_" + model.getId() + ".jpeg";
								XWPFRun xwpfRun = para.createRun();
//								xwpfRun.setText(imgFile);
//					            xwpfRun.addBreak();
//					            xwpfRun.addPicture(new ByteArrayInputStream(image), 
//					            		XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(200), Units.toEMU(200));
								
								String blipId = templateDoc.addPictureData(image, XWPFDocument.PICTURE_TYPE_JPEG);
								templateDoc.createPicture(xwpfRun, blipId, para.getDocument().getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_JPEG), 150, 150);
								
							} catch (InvalidFormatException e) {
								logger.error("Error occurred while creating picture object.", e);
							}
						}
					}
				}
			}
		}
		
		return templateDoc;
	}
	
	private void removeRuns(XWPFParagraph para) {
		int index = para.getRuns().size() - 1;
		while (index >= 0) {
			para.removeRun(index);
			index--;
		}
	}

}
