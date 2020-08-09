package com.github.abagabagon.verifico.data.excel;

public interface ExcelData {
	
	/**
	 * Gets data from a specific Cell of an Excel File Sheet.
	 * 
	 * @param  sheetName Sheet Name from the Excel File where data will be retrieved.
	 * @param  rowNum    Row of cell data to retrieve.
	 * @param  columnNum Column of cell data to retrieve.
	 * @return Data in a form of an Object.
	 */
	
	public Object getCellData(String sheetName, int rowNum, int columnNum);
	
	/**
	 * Gets data from a specific Column of an Excel File Sheet.
	 * 
	 * @param  sheetName	Sheet Name from the Excel File where data will be retrieved.
	 * @param  columnName 	Column Name from the Excel File where data will be retrieved.
	 * @return Data in a form of an Object.
	 */
	
	public Object[] getColumnData(String sheetName, String columnName);
	
	/**
	 * Gets all data from the specified Excel Sheet Name.
	 * 
	 * @param  filePath  Excel File Path.
	 * @param  sheetName Sheet Name from the Excel File where data will be retrieved.
	 * @return Data in a form of an Object Array.
	 */
	
	/**
	 * Gets all data from the specified Excel Sheet Name.
	 * 
	 * @param  sheetName  Sheet Name from the Excel File where data will be retrieved.
	 * @param  withHeader <code>true</code> if Sheet Name has a header column.
	 * 					  <code>false</code> if Sheet Name has no header column.
	 * @return Data in a form of an Object Array.
	 */
	
	public Object[][] getSheetData(String sheetName, boolean withHeader);

}
