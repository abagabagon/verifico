package utilities;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Parser {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(Parser.class);
		log.debug("Initializing Parser Class.");
		log.debug("Successfully initialized Parser Class.");
	}
	
	/**
	 * Parses an Object Array to a String Array
	 * 
	 * @param object Object Array for parsing
	 * @return parsed String Array
	 */
	
	public static final String[] parseObjectArrayToStringArray(Object[] object) {
		int objectCount = object.length;
		String[] parsed = new String[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = object[i].toString();
			log.trace("Successfully parsed Object to String: " + parsed[i]);
		}
		return parsed;
	}
	
	/**
	 * Parses an Object Array to an int Array
	 * 
	 * @param object Object Array for parsing
	 * @return parsed int Array
	 */
	
	public static final int[] parseObjectArrayToIntArray(Object[] object) {
		int objectCount = object.length;
		int[] parsed = new int[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Integer.parseInt(object[i].toString());
			log.trace("Successfully parsed Object to Integer: " + parsed[i]);
		}
		return parsed;
	}
	
	/**
	 * Parses an Object Array to an double Array
	 * 
	 * @param object Object Array for parsing
	 * @return parsed double Array
	 */
	
	public static final double[] parseObjectArrayToDoubleArray(Object[] object) {
		int objectCount = object.length;
		double[] parsed = new double[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Double.parseDouble(object[i].toString());
			log.trace("Successfully parsed Object to Double: " + parsed[i]);
		}
		return parsed;
	}
	
	/**
	 * Parses an Object Array to an boolean Array
	 * 
	 * @param object Object Array for parsing
	 * @return parsed boolean Array
	 */
	
	public static final boolean[] parseObjectArrayToBooleanArray(Object[] object) {
		int objectCount = object.length;
		boolean[] parsed = new boolean[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Boolean.parseBoolean(object[i].toString());
			log.trace("Successfully parsed Object to Boolean: " + parsed[i]);
		}
		return parsed;
	}
	
	/**
	 * Parses an input ResultSet to an Object Array.
	 * 
	 * @param resultSet 	Input ResultSet Object
	 * @param columnCount	Column Count
	 * @param rowCount		Row Count
	 * @return Parsed Object Array
	 */
	
	public static Object[][] parseResultSetToObjectArray(ResultSet resultSet, int columnCount, int rowCount) {
		Object[][] sqlData = null;
		try {
			sqlData = new Object[rowCount][columnCount];
			if (rowCount > 0) {
				for (int i = 0; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						resultSet.next();
						sqlData[i][j] = resultSet.getObject(j + 1);
						continue;
					}
				}
			} else {
				log.fatal("No Data is available.");
			}
		} catch(SQLException e) {
			log.fatal("Encountered SQLException while retrieving SQL Data!");
			e.printStackTrace();
		} catch(Exception e) {
			log.fatal("Encountered Exception while retrieving SQL Data!");
			e.printStackTrace();
		}
		return sqlData;
	}
}
