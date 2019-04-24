package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

public class Excel {
	
	private static Logger log;
	private static FileInputStream excelIn;
	private static XSSFWorkbook book;
	private static XSSFCell cell;
	
	static {
		log = LogManager.getLogger(Excel.class.getName());
	}
	
	/**
	 * Initializes external input Excel File containing Test Data needed for
	 * Testing.
	 */

	private static final void initializeInputExcelFile(String filePath) {
		log.trace("Initializing Excel File.");
		try {
			excelIn = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			log.fatal("Encountered FileNotFoundException while initializing Input Excel File!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while initializing Input Excel File!");
		}
		
		try {
			book = new XSSFWorkbook(excelIn);
		} catch (IOException e) {
			log.fatal("Encountered IOException while initializing Input Excel File for Apache POI!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while initializing Input Excel File for Apache POI!");
		}
		
		log.trace("Successfully initialized Excel File.");
	}
	
	/**
	 * Closes the input Excel File.
	 */

	private static final void closeInputExcelFile() {
		log.trace("Closing input Excel File.");
		try {
			excelIn.close();
		} catch(IOException e) {
			log.error("Encountered IOException while closing Input Excel File!");
		} catch(Exception e) {
			log.error("Encountered Exception while closing Input Excel File!");
		}
		log.trace("Successfully closed input Excel File.");
	}
	
	/**
	 * Gets data from a specific Cell of an Excel File Sheet.
	 * 
	 * @param  filePath  Excel File Path.
	 * @param  sheetName Sheet Name from the Excel File where data will be retrieved.
	 * @param  rowNum    Row of cell data to retrieve.
	 * @param  columnNum Column of cell data to retrieve.
	 * @return Data in a form of an Object.
	 */
	
	public static final Object getExcelCellData(String filePath, String sheetName, int rowNum, int columnNum) {
		initializeInputExcelFile(filePath);
		log.trace("Retrieving Cell Data from \"" + sheetName + "\" Excel Sheet.");
		Object excelData = new Object();
		cell = book.getSheet(sheetName).getRow(rowNum).getCell(columnNum);
		if (cell.getCellType() == CellType.STRING) {
			excelData = cell.getStringCellValue();
			log.trace("Successfully retrieved String Value \"" + excelData.toString() + "\" from Sheet: " + sheetName);
		} else if (cell.getCellType() == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell) == true) {
				excelData = cell.getDateCellValue();
				log.trace("Successfully retrieved Date Value \"" + excelData.toString() + "\" from Sheet: " + sheetName);
			} else {
				cell.setCellType(CellType.STRING);
				excelData = cell.getStringCellValue();
				log.trace("Successfully retrieved String Value \"" + Double.parseDouble(excelData.toString()) + "\" from a Numeric Type Cell from Sheet: " + sheetName);
				}
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			excelData = cell.getBooleanCellValue();
			log.trace("Successfully retrieved String Value \"" + Boolean.parseBoolean(excelData.toString()) + "\" from a Boolean Type Cell from Sheet: " + sheetName);
		} else if (cell.getCellType() == CellType.FORMULA) {
			excelData = cell.getRawValue();
			log.trace("Successfully retrieved String Value \"" + excelData.toString() + "\" from a Formula Type Cell from Sheet: " + sheetName);
		} else {
			log.error("Encountered undefined Cell Type: " + cell.getCellType());
		}
		closeInputExcelFile();
		return excelData;
	}
	
	/**
	 * Gets data from a specific Column of an Excel File Sheet.
	 * 
	 * @param  filePath  Excel File Path.
	 * @param  sheetName Sheet Name from the Excel File where data will be retrieved.
	 * @param  rowNum    Row of cell data to retrieve.
	 * @param  columnNum Column of cell data to retrieve.
	 * @return Data in a form of an Object.
	 */
	
	public static final Object[] getExcelColumnData(String filePath, String sheetName, String columnName) {
		initializeInputExcelFile(filePath);
		log.trace("Retrieving data of the Column \"" + columnName + "\" from \"" + sheetName + "\" Excel Sheet.");
		int columnCount = book.getSheet(sheetName).getRow(0).getPhysicalNumberOfCells();
		int rowCount    = book.getSheet(sheetName).getPhysicalNumberOfRows();
		Object[] excelData = new Object[rowCount - 1];
		boolean columnExist = false;
		int columnNum;
		for (int i = 0; i < columnCount; i++) {
			cell = book.getSheet(sheetName).getRow(0).getCell(i);
			if (cell.getStringCellValue().toString().equals(columnName)) {
				columnNum = i;
				columnExist = true;
				for (int j = 0; j < rowCount - 1; j++) {
					cell = book.getSheet(sheetName).getRow(j + 1).getCell(columnNum);
					if (cell.getCellType() == CellType.STRING) {
						excelData[j] = cell.getStringCellValue();
						log.trace("Successfully retrieved String Value \"" + excelData[j].toString() + "\" from Sheet: " + sheetName);
					} else if (cell.getCellType() == CellType.NUMERIC) {
						if (DateUtil.isCellDateFormatted(cell) == true) {
							excelData[j] = cell.getDateCellValue();
							log.trace("Successfully retrieved Date Value \"" + excelData[j].toString() + "\" from Sheet: " + sheetName);
						} else {
							cell.setCellType(CellType.STRING);
							excelData[j] = cell.getStringCellValue();
							log.trace("Successfully retrieved String Value \"" + Double.parseDouble(excelData[j].toString()) + "\" from a Numeric Type Cell from Sheet: " + sheetName);
						}
					} else if (cell.getCellType() == CellType.BOOLEAN) {
						excelData[j] = cell.getBooleanCellValue();
						log.trace("Successfully retrieved String Value \"" + Boolean.parseBoolean(excelData[j].toString()) + "\" from a Boolean Type Cell from Sheet: " + sheetName);
					} else if (cell.getCellType() == CellType.FORMULA) {
						excelData[j] = cell.getRawValue();
						log.trace("Successfully retrieved String Value \"" + excelData[j].toString() + "\" from a Formula Type Cell from Sheet: " + sheetName);
					} else {
						log.error("Encountered undefined Cell Type: " + cell.getCellType());
					}
				}
				break;
			}
		}
		if (!columnExist) {
			log.fatal("The Column Name \"" + columnName + "\" specified does not exist.");
			Assert.fail("The Column Name \"" + columnName + "\" specified does not exist.");
		}
		closeInputExcelFile();
		return excelData;
	}
	
	/**
	 * Gets all data from the specified Excel File and Sheet Name.
	 * 
	 * @param  filePath  Excel File Path.
	 * @param  sheetName Sheet Name from the Excel File where data will be retrieved.
	 * @return Data in a form of an Object Array.
	 */
	
	public static final Object[][] getExcelData(String filePath, String sheetName) {
		initializeInputExcelFile(filePath);
		log.trace("Retrieving data from \"" + sheetName + "\" Excel Sheet.");
		int columnCount = book.getSheet(sheetName).getRow(0).getPhysicalNumberOfCells();
		int rowCount    = book.getSheet(sheetName).getPhysicalNumberOfRows();
		Object[][] excelData = new Object[rowCount][columnCount];
		if (rowCount > 0) {
			for (int i = 1; i < rowCount; i++) {
				for (int j = 0; j < columnCount; j++) {
					cell = book.getSheet(sheetName).getRow(i).getCell(j);
					if (cell.getCellType() == CellType.STRING) {
						excelData[i][j] = cell.getStringCellValue();
						log.trace("Successfully retrieved String Value \"" + excelData[i][j].toString() + "\" from Sheet: " + sheetName);
						continue;
					} else if (cell.getCellType() == CellType.NUMERIC) {
						if (DateUtil.isCellDateFormatted(cell) == true) {
							excelData[i][j] = cell.getDateCellValue();
							log.trace("Successfully retrieved Date Value \"" + excelData[i][j].toString() + "\" from Sheet: " + sheetName);
							continue;
						} else {
							cell.setCellType(CellType.STRING);
							excelData[i][j] = cell.getStringCellValue();
							log.trace("Successfully retrieved String Value \"" + Double.parseDouble(excelData[i][j].toString()) + "\" from a Numeric Type Cell from Sheet: " + sheetName);
							continue;
						}
					} else if (cell.getCellType() == CellType.BOOLEAN) {
						excelData[i][j] = cell.getBooleanCellValue();
						log.trace("Successfully retrieved String Value \"" + Boolean.parseBoolean(excelData[i][j].toString()) + "\" from a Boolean Type Cell from Sheet: " + sheetName);
					} else if (cell.getCellType() == CellType.FORMULA) {
						excelData[i][j] = cell.getRawValue();
						log.trace("Successfully retrieved String Value \"" + excelData[i][j].toString() + "\" from a Formula Type Cell from Sheet: " + sheetName);
						continue;
					} else {
						log.error("Encountered undefined Cell Type: " + cell.getCellType());
					}
				}
			}
		} else {
			log.fatal("Test Data is not available for Test Case: " + sheetName + ".");
		}
		closeInputExcelFile();
		return excelData;
	}

}
