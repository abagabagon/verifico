package com.github.abagabagon.data.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

public class XLSExcelData implements ExcelData {
	
	private Logger log;
	private FileInputStream excelIn;
	private HSSFWorkbook book;
	private HSSFCell cell;
	private String filePath;

	public XLSExcelData(String filePath) {
		this.log = LogManager.getLogger(this.getClass());
		this.filePath = filePath;
	}

	/**
	 * Initializes external input Excel File.
	 */

	private void initializeInputFile() {
		this.log.trace("Initializing Excel File.");
		try {
			this.excelIn = new FileInputStream(this.filePath);
		} catch (FileNotFoundException e) {
			this.log.fatal("Encountered FileNotFoundException while initializing Input Excel File!");
		} catch (SecurityException e) {
			this.log.fatal("Encountered FileNotFoundException while initializing Input Excel File!");
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing Input Excel File!");
		}

		try {
			this.book = new HSSFWorkbook(this.excelIn);
		} catch (IOException e) {
			this.log.fatal("Encountered IOException while initializing Input Excel File for Apache POI!");
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing Input Excel File for Apache POI!");
		}
		
		this.log.trace("Successfully initialized Excel File.");
	}
	
	/**
	 * Closes the input Excel File.
	 */

	private void closeInputFile() {
		this.log.trace("Closing input Excel File.");
		try {
			this.excelIn.close();
		} catch(IOException e) {
			this.log.error("Encountered IOException while closing Input Excel File!");
		} catch(Exception e) {
			this.log.error("Encountered Exception while closing Input Excel File!");
		}
		this.log.trace("Successfully closed input Excel File.");
	}
	
	@Override
	public Object getCellData(String sheetName, int rowNum, int columnNum) {
		initializeInputFile();
		this.log.trace("Retrieving Cell Data from \"" + sheetName + "\" Excel Sheet.");
		Object excelData = new Object();
		this.cell = this.book.getSheet(sheetName).getRow(rowNum).getCell(columnNum);
		excelData = this.getCellValue(this.cell);
		closeInputFile();
		return excelData;
	}
	
	@Override
	public Object[] getColumnData(String sheetName, String columnName) {
		initializeInputFile();
		this.log.trace("Retrieving data of the Column \"" + columnName + "\" from \"" + sheetName + "\" Excel Sheet.");
		int columnCount = this.book.getSheet(sheetName).getRow(0).getPhysicalNumberOfCells();
		int rowCount = this.book.getSheet(sheetName).getPhysicalNumberOfRows();
		Object[] excelData = new Object[rowCount - 1];
		boolean columnExist = false;
		int columnNum;
		for (int i = 0; i < columnCount; i++) {
			this.cell = this.book.getSheet(sheetName).getRow(0).getCell(i);
			if (this.cell.getStringCellValue().toString().equals(columnName)) {
				columnNum = i;
				columnExist = true;
				for (int j = 0; j < rowCount - 1; j++) {
					this.cell = this.book.getSheet(sheetName).getRow(j + 1).getCell(columnNum);
					excelData[j] = this.getCellValue(this.cell);
				}
				break;
			}
		}
		if (!columnExist) {
			this.log.fatal("The Column Name \"" + columnName + "\" specified does not exist.");
		}
		closeInputFile();
		return excelData;
	}
	
	@Override
	public Object[][] getSheetData(String sheetName, boolean withHeader) {
		initializeInputFile();
		this.log.trace("Retrieving data from \"" + sheetName + "\" Excel Sheet.");
		int columnCount = this.book.getSheet(sheetName).getRow(0).getPhysicalNumberOfCells();
		int rowCount    = this.book.getSheet(sheetName).getPhysicalNumberOfRows();
		Object[][] excelData = null;
		
		if(withHeader) {
			excelData = new Object[rowCount - 1][columnCount];
			if (rowCount > 0) {
				for (int i = 1; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						this.cell = this.book.getSheet(sheetName).getRow(i).getCell(j);
						excelData[i - 1][j] = this.getCellValue(this.cell);
						continue;
					}
				}
			} else {
				this.log.fatal("Test Data is not available for Test Case: " + sheetName + ".");
			}
		} else {
			excelData = new Object[rowCount][columnCount];
			if (rowCount > 0) {
				for (int i = 0; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						this.cell = this.book.getSheet(sheetName).getRow(i).getCell(j);
						excelData[i][j] = this.getCellValue(this.cell);
						continue;
					}
				}
			} else {
				this.log.fatal("No Data is available for Sheet Name: " + sheetName + ".");
			}
		}

		closeInputFile();
		return excelData;
	}
	
	/**
	 * Get value of a Excel Cell.
	 * 
	 * @param cell HSSFCell Object
	 * @return Cell data in a form of an Object.
	 */
	
	private Object getCellValue(HSSFCell cell) {
		Object excelData = null;
		switch(this.cell.getCellType()) {
		case STRING:
			excelData = getStringCellTypeData(cell);
			break;
		case NUMERIC:
			excelData = this.getNumericCellTypeData(cell);
			break;
		case BOOLEAN:
			excelData = getBooleanCellTypeData(cell);
			break;
		case FORMULA:
			excelData = this.getFormulaCellTypeData(cell);
		default:
			this.log.error("Encountered undefined Cell Type: " + cell.getCellType());
		}
		return excelData;
	}
	
	/**
	 * Get value of a String Type Excel Cell.
	 * 
	 * @param cell HSSFCell Object
	 * @return Cell data in a form of an Object.
	 */
	
	private Object getStringCellTypeData(HSSFCell cell) {
		Object excelData = cell.getStringCellValue();
		this.log.trace("Successfully retrieved String Value \"" + excelData.toString() + "\"");
		return excelData;
	}

	/**
	 * Get value of a Numeric Type Excel Cell.
	 * 
	 * @param cell HSSFCell Object
	 * @return Cell data in a form of an Object.
	 */
	
	private Object getNumericCellTypeData(HSSFCell cell) {
		Object excelData = null;
		if (DateUtil.isCellDateFormatted(cell) == true) {
			excelData = this.cell.getDateCellValue();
			this.log.trace("Successfully retrieved Date Value \"" + excelData.toString() + "\"");
		} else {
			BigDecimal bigdDecimal = new BigDecimal(this.cell.getNumericCellValue());
			excelData = bigdDecimal;
			this.log.trace("Successfully retrieved Numeric Value \"" + Double.parseDouble(excelData.toString()) + "\" from a Numeric Type Cell");
		}
		return excelData.toString();
	}
	
	/**
	 * Get value of a Boolean Type Excel Cell.
	 * 
	 * @param cell HSSFCell Object
	 * @return Cell data in a form of an Object.
	 */
	
	private Object getBooleanCellTypeData(HSSFCell cell) {
		Object excelData = cell.getBooleanCellValue();
		this.log.trace("Successfully retrieved Boolean Value \"" + Boolean.parseBoolean(excelData.toString()) + "\" from a Boolean Type Cell.");
		return excelData;
	}
	
	/**
	 * Get value of a Formula Type Excel Cell.
	 * 
	 * @param cell HSSFCell Object
	 * @return Cell data in a form of an Object.
	 */
	
	private Object getFormulaCellTypeData(HSSFCell cell) {
		Object excelData = cell.getStringCellValue();
		this.log.trace("Successfully retrieved Formula Value \"" + excelData.toString() + "\" from a Formula Type Cell.");
		return excelData;
	}
	
}
