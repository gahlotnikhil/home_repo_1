package com.ngs.cform.manager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ngs.cform.model.RecordModel;
import com.ngs.cform.resource.ResourceConfig;
import com.ngs.cform.util.GeneralUtils;

public class ExcelDataManager {
	
	private  Logger logger = Logger.getLogger(ExcelDataManager.class);
	
	public static String CUSTOMER_SHEET = "customer";
	
	public static String IMAGE_SHEET = "image";
	
	public static String DEFAULT_IMAGE = "image.jpeg";
	
	private Workbook workbook;
	
	private Sheet sheet;
	
	private Sheet imageSheet;
	
	private File file;
	
	private ResourceConfig resourceConfig;
	
	public ExcelDataManager(File file, ResourceConfig resourceConfig) {
		this.resourceConfig = resourceConfig;
		intialize(file);
	}

	private void intialize(File file) {
		this.file = file;
		
		try {
			FileInputStream inputStream = new FileInputStream(file);
			workbook = new XSSFWorkbook(inputStream);
			sheet = workbook.getSheet(CUSTOMER_SHEET);
			if (sheet == null) {
				sheet = workbook.createSheet(CUSTOMER_SHEET);
			}
			
			Row headingRow = sheet.getRow(0);
			if (headingRow == null) {
				headingRow = sheet.createRow(0);
			}
			
			intitializeHeading(headingRow);
			
			// Image sheet
			imageSheet = workbook.getSheet(IMAGE_SHEET);
			if (imageSheet == null) {
				imageSheet = workbook.createSheet(IMAGE_SHEET);
			}
			
			Row imageHeadingRow = imageSheet.getRow(0);
			if (imageHeadingRow == null) {
				imageHeadingRow = imageSheet.createRow(0);
			}
			
			intitializeImageHeading(imageHeadingRow); 
			
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			
			try {
				inputStream.close();
			} catch (Exception e) {
				logger.error("Error occurred", e);
			}
			
			try {
				outputStream.close();
			} catch (Exception e) {
				logger.error("Error occurred", e);
			}
		} catch (Exception e) {
			logger.error("Error occurred while initializing excel.", e);
		}
	}
	
	private void intitializeHeading(Row headingRow) {
		int index = 0;
		
		for (String key : resourceConfig.getDataFieldKeys()) {
			Cell headerCell = headingRow.createCell(index);
			headerCell.setCellValue(key);
			
			index++;
		}
	}
	
	private void intitializeImageHeading(Row headingRow) {
		Cell idCell = headingRow.createCell(0);
		idCell.setCellValue(RecordModel.KEY_ID);
		
		Cell imageCell = headingRow.createCell(1);
		imageCell.setCellValue(RecordModel.KEY_IMAGE);
	}
	
	private void updateSheet(RecordModel record, Row newRow) throws IOException {
		int index = 0;
		
		for (String key : resourceConfig.getDataFieldKeys()) {
			Cell headerCell = newRow.createCell(index);
			Object value = record.getValueByKey(key);
			if (value != null) {
				setCellValue(headerCell, value);
			}
			
			index++;
		}
		
		writeToStream();
	}
	
	private void writeToStream() throws IOException {
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);
		
		try {
			outputStream.close();
			
			// refresh the document with latest content
			intialize(file);
		} catch (Exception e) {
			logger.error("Error occurred writing to stream.", e);
		}
	}

	private long generateUniqueId() {
		return System.currentTimeMillis();
	}

	private Row getNewRow() {
		int rowIndex = sheet.getLastRowNum() + 1;
		Row newRow = sheet.createRow(rowIndex);
		return newRow;
	}
	
	private Row getRow(long id, Sheet sheet) {
		int index = 1;
		int lastRowNum = sheet.getLastRowNum();
		while (index <= lastRowNum) {
			Row row = sheet.getRow(index);
			if (row.getLastCellNum() > 0) {
				Cell cell = row.getCell(0);
				if (id == cell.getNumericCellValue()) {
					return row;
				}
			}
			
			index++;
		}
		return null;
	}
	
	private RecordModel getRecordModel(Row row) {
		List<String> keyList = new ArrayList<>(resourceConfig.getDataFieldKeys());
		
		RecordModel record = new RecordModel();
		if (row.getLastCellNum() > 0) {
			int cindex = 0;
			while (cindex <= row.getLastCellNum()) {
				Cell cell = row.getCell(cindex);
				if (cell != null) {
					Object cellValue = getCellValue(cell);
					String key = keyList.get(cindex);
					
					record.setValueByKey(key, cellValue);
				}
				
				cindex++;
			}
		}
		
		return record;
	}
	
	private void setCellValue(Cell cell, Object value) {
		if (value instanceof Long) {
			cell.setCellValue(Long.parseLong(value.toString()));
		} else if (value instanceof Boolean) {
			cell.setCellValue(Boolean.parseBoolean(value.toString()));
		} else {
			cell.setCellValue(value.toString());
		}
	}

	private Object getCellValue(Cell cell) {
		Object cellValue = "";
		switch (cell.getCellType()) {
        case XSSFCell.CELL_TYPE_NUMERIC:
        	cellValue = new Double(cell.getNumericCellValue()).longValue();
            break;
        case XSSFCell.CELL_TYPE_STRING:
        	cellValue = cell.getRichStringCellValue().toString();
            break;
        case XSSFCell.CELL_TYPE_BOOLEAN:
        	cellValue = cell.getBooleanCellValue();
            break;
        default:
        	cellValue = cell.getRichStringCellValue().toString();
            break;
        }
		return cellValue;
	}
	
	public boolean add(RecordModel record, BufferedImage avatarImage) {
		Row newRow = getNewRow();
		long id = generateUniqueId();
		record.setValueByKey(RecordModel.KEY_ID, id);
		
		try {
			saveImage(id, avatarImage);
			updateSheet(record, newRow);
			return true;
		} catch (IOException e) {
			logger.error("Error occurred while adding record.", e);
		}
		return false;
	}
	
	public byte[] getAvatarImage(long id) {
		Row row = getRow(id, imageSheet);
		if (row != null && row.getLastCellNum() > 1) {
			double imgIndex = row.getCell(2).getNumericCellValue();
			List<? extends PictureData> pictures = workbook.getAllPictures();
			if(pictures != null) {
				XSSFPictureData pData = (XSSFPictureData) pictures.get((int) imgIndex);
				return pData.getData();
			}
		}
		
		try {
			InputStream defImageIS = GeneralUtils.getResourceAsStream(DEFAULT_IMAGE);
			return IOUtils.toByteArray(defImageIS);
		} catch (IOException e) {
			logger.error("Error occurred while getting avatar image.", e);
		}
		return null;
	}
	
	private void saveImage(long id, BufferedImage avatarImage) {
		//TODO
		Row row = getRow(id, imageSheet);
		if (row == null) {
			int rowIndex = imageSheet.getLastRowNum() + 1;
			row = imageSheet.createRow(rowIndex);
		}
		
		Cell idCell = row.getCell(0);
		if (idCell == null) {
			idCell = row.createCell(0);
		}
		idCell.setCellValue(id);
		
//		Cell imageCell = row.getCell(1);
//		if (imageCell == null) {
//			imageCell = row.createCell(1);
//		}
		//imageCell.setCellValue(avatarImage.);
		
		if (avatarImage != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(avatarImage, "jpg", baos);
				
				XSSFDrawing patriarch = (XSSFDrawing) imageSheet.createDrawingPatriarch();
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 30, 30,
						(short) 1, row.getRowNum(), (short) 2, row.getRowNum());
				
				int imageIndex = workbook.addPicture(
						baos.toByteArray(),
						XSSFWorkbook.PICTURE_TYPE_JPEG);
				patriarch
				.createPicture(anchor, imageIndex);
				
				Cell imageIndexCell = row.getCell(2);
				if (imageIndexCell == null) {
					imageIndexCell = row.createCell(2);
				}
				imageIndexCell.setCellValue(imageIndex);
				
			} catch (IOException e) {
				logger.error("Error occurred saving image.", e);
			}
		}
	}

	public boolean update(RecordModel record, BufferedImage avatarImage) {
		Object idStr = record.getValueByKey(RecordModel.KEY_ID);
		Long id = null;
		if (idStr != null) {
			id = Long.parseLong(idStr.toString());
		}
		if (id != null) {
			Row row = getRow(id, sheet);
			if (row != null) {
				try {
					saveImage(id, avatarImage);
					updateSheet(record, row);
					return true;
				} catch (IOException e) {
					logger.error("Error occurred while updating record.", e);
				}
			}
		}
		return false;
	}
	
	public boolean remove(long id) {
		Row row = getRow(id, sheet);
		if (row != null) {
			int rowIndex = row.getRowNum();
			sheet.removeRow(row);
			
	        int lastRowNum = sheet.getLastRowNum();
	        if (rowIndex >= 0 && rowIndex < lastRowNum)
	        {
	            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
	        }
			
			try {
				writeToStream();
				return true;
			} catch (IOException e) {
				logger.error("Error occurred while removing record.", e);
			}
		}
		return false;
	}
	
	public List<RecordModel> find(RecordModel record) {
		List<RecordModel> list = new ArrayList<RecordModel>();
		int index = 1;
		int lastRowNum = sheet.getLastRowNum();
		while (index <= lastRowNum) {
			Row row = sheet.getRow(index);
			RecordModel model = getRecordModel(row);
			
			// Consider only when it matches the criteria
			if (matches(model, record)) {
				list.add(model);
			}
			index++;
		}
		
		return list;
	}
	
	private boolean matches(RecordModel model, RecordModel record) {
		List<String> searchableKeyList = new ArrayList<String>();
		
//		searchableKeyList.add(RecordModel.KEY_HOTEL_NAME);
//		searchableKeyList.add(RecordModel.KEY_CUSTOMER_NAME);
//		searchableKeyList.add(RecordModel.KEY_NATIONALITY);
//		searchableKeyList.add(RecordModel.KEY_PASSPORT_NO);
		// TODO
		

		boolean matched = true;
		//boolean allEmpty
		for (String key : searchableKeyList) {
			if (model.getValueByKey(key) != null && record.getValueByKey(key) != null) {
				
				matched = model.getValueByKey(key).equals(record.getValueByKey(key));
				
				if (!matched) {
					break;
				}
			}
		}
		
		return matched;
	}

	public List<RecordModel> findAll() {
		List<RecordModel> list = new ArrayList<RecordModel>();
		int index = 1;
		int lastRowNum = sheet.getLastRowNum();
		while (index <= lastRowNum) {
			Row row = sheet.getRow(index);
			RecordModel model = getRecordModel(row);
			
			list.add(model);
			index++;
		}
		
		return list;
	}
	
	public RecordModel find(Long id) {
		Row row = getRow(id, sheet);
		return getRecordModel(row);
	}

}
