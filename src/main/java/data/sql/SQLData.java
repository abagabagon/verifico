package data.sql;

public interface SQLData {
	
	/**
	 * Executes a SELECT SQL Statement defined at the File Path.
	 * 
	 * @param sqlFilePath File Path of the SQL Script with SELECT Statement
	 * @return ResultSet object that contains the data produced by the given query.
	 */
	
	public Object[][] select(String sqlFilePath);
}
