package data.sql;

import java.sql.ResultSet;
import java.util.List;

public interface SQLData {
	
	/**
	 * Initializes SQL Connection.
	 */
	
	public void initializeConnection();
	
	/**
	 * Executes a SELECT SQL Statement defined at the File Path.
	 * 
	 * @param sqlFilePath
	 * @return ResultSet object that contains the data produced by the given query.
	 */
	
	public ResultSet select(String sqlFilePath);
	
	/**
	 * Parses ResultSet Object to Object Array List (List<Object[]>).
	 * 
	 * @return Parsed ResultSet Object in Object Array List Format.
	 */
	
	public List<Object[]> parseResultSetToObjectArrayList();
	
	/**
	 * Display SQL Object Data.
	 */
	
	public void displaySQLObject();
	
	/**
	 * Closes SQL Connection.
	 */
	
	public void closeConnection();
}
