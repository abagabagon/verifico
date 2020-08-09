package com.github.abagabagon.verifico.data.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abagabagon.verifico.enums.SQL;

public class SQLData {
	
	private Logger log;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private SQL sqlType;
	private String dbServer;
	private String dbName;
	private String user;
	private String password;
	private SQLDriver sqlDriver;

	public SQLData(SQL sqlType, String dbServer, String dbName, String user, String password) {
		this.log = LogManager.getLogger(this.getClass());
		this.sqlType = sqlType;
		this.dbServer = dbServer;
		this.dbName = dbName;
		this.user = user;
		this.password = password;
	}
	
	/* ####################################################### */
	/*                     MAIN OPERATIONS                     */
	/* ####################################################### */
	
	/**
	 * Opens SQL Connection.
	 * 
	 */
	
	public void openConnection() {
		this.log.info("I open SQL Connection.");
		this.sqlDriver = new SQLDriver(this.sqlType, this.dbServer, this.dbName, this.user, this.password);
		this.connection = this.sqlDriver.getSQLConnection();
	}
	
	/**
	 * Executes a SQL SELECT Statement and returns the ResultSet.
	 * 
	 * @param sqlQuery SQL SELECT Query to be executed
	 * @return	ResultSet based on the SQL SELECT Query
	 * @throws SQLTimeoutException SQL processing timed out.
	 */
	
	public ResultSet select(String sqlQuery) throws SQLTimeoutException {
		this.log.info("I execute SQL SELECT Statement.");
		if (sqlQuery.toLowerCase().contains("select")) {
			try {
				this.statement = this.connection.createStatement();
				this.resultSet = this.statement.executeQuery(sqlQuery);
			} catch(SQLException e) {
				this.log.fatal("Encountered SQLException while executing SQL SELECT Statement!");
				this.log.fatal("SQL Exception: " + e.getMessage());
				this.log.fatal("SQL State: " + e.getSQLState());
				this.log.fatal("Error Code: " + e.getErrorCode());
			} catch(Exception e) {
				this.log.fatal("Encountered Exception while executing SQL SELECT Statement!");
				e.printStackTrace();
			}
		} else {
			this.log.error("Statement is not a SELECT Statement.");
		}

		return this.resultSet;
	}
	
	/**
	 * Executes a SQL UPDATE Statement.
	 * 
	 * @param sqlQuery SQL UPDATE Query to be executed
	 * @throws SQLTimeoutException SQL processing timed out.
	 */
	
	public void update(String sqlQuery) throws SQLTimeoutException {
		this.log.info("I execute SQL UPDATE Statement.");
		int updateCount = 0;
		if (sqlQuery.toLowerCase().contains("update")) {
			try {
				this.statement = this.connection.createStatement();
				updateCount = this.statement.executeUpdate(sqlQuery);
			} catch(SQLException e) {
				this.log.fatal("Encountered SQLException while executing SQL UPDATE Statement!");
				this.log.fatal("SQL Exception: " + e.getMessage());
				this.log.fatal("SQL State: " + e.getSQLState());
				this.log.fatal("Error Code: " + e.getErrorCode());
			} catch(Exception e) {
				this.log.fatal("Encountered Exception while executing SQL UPDATE Statement!");
				e.printStackTrace();
			}
			
			if (updateCount > 0) {
				this.log.info(updateCount + " SQL Records updated.");
			} else {
				this.log.error("No SQL Records were updated.");
			}
		} else {
			this.log.error("Statement is not an UPDATE Statement.");
		}
	}
	
	/**
	 * Executes a SQL INSERT Statement.
	 * 
	 * @param sqlQuery SQL INSERT Query to be executed
	 * @throws SQLTimeoutException SQL processing timed out.
	 */
	
	public void insert(String sqlQuery) throws SQLTimeoutException {
		this.log.info("I execute SQL INSERT Statement.");
		int insertCount = 0;
		if (sqlQuery.toLowerCase().contains("insert")) {
			try {
				this.statement = this.connection.createStatement();
				insertCount = this.statement.executeUpdate(sqlQuery);
			} catch(SQLException e) {
				this.log.fatal("Encountered SQLException while executing SQL INSERT Statement!");
				this.log.fatal("SQL Exception: " + e.getMessage());
				this.log.fatal("SQL State: " + e.getSQLState());
				this.log.fatal("Error Code: " + e.getErrorCode());
			} catch(Exception e) {
				this.log.fatal("Encountered Exception while executing SQL INSERT Statement!");
				e.printStackTrace();
			}
			
			if (insertCount > 0) {
				this.log.info(insertCount + " SQL Records inserted.");
			} else {
				this.log.error("No SQL Records were inserted.");
			}
		} else {
			this.log.error("Statement is not an INSERT Statement.");
		}
	}
	
	/**
	 * Executes a SQL DELETE Statement.
	 * 
	 * @param sqlQuery SQL DELETE Query to be executed
	 * @throws SQLTimeoutException SQL processing timed out.
	 */
	
	public void delete(String sqlQuery) throws SQLTimeoutException {
		this.log.info("I execute SQL DELETE Statement.");
		int insertCount = 0;
		if (sqlQuery.toLowerCase().contains("delete") || sqlQuery.toLowerCase().contains("where")) {
			try {
				this.statement = this.connection.createStatement();
				insertCount = this.statement.executeUpdate(sqlQuery);
			} catch(SQLException e) {
				this.log.fatal("Encountered SQLException while executing SQL DELETE Statement!");
				this.log.fatal("SQL Exception: " + e.getMessage());
				this.log.fatal("SQL State: " + e.getSQLState());
				this.log.fatal("Error Code: " + e.getErrorCode());
			} catch(Exception e) {
				this.log.fatal("Encountered Exception while executing SQL DELETE Statement!");
				e.printStackTrace();
			}
			
			if (insertCount > 0) {
				this.log.info(insertCount + " SQL Records deleted.");
			} else {
				this.log.error("No SQL Records were deleted.");
			}
		} else {
			this.log.error("Statement is not a DELETE Statement or no WHERE conditions were provided.");
		}
	}
	
	/**
	 * Closes and empties the ResultSet.
	 * 
	 */
	
	public void closeResultSet() {
		this.log.debug("Closing and Emptying ResultSet.");
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
				this.resultSet = null;	
			}
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while closing and emptying ResultSet!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while closing and emptying ResultSet!");
			e.printStackTrace();
		}
		this.log.debug("Successfully closed and emptied ResultSet.");
	}
	
	/**
	 * Closes statement.
	 * 
	 */
	
	public void closeStatement() {
		this.log.debug("Closing Statement.");
		try {
			if (this.statement != null) {
				this.statement.close();
				this.statement = null;
			}
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while closing Statement!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while closing Statement!");
			e.printStackTrace();
		}
		this.log.debug("Successfully closed Statement.");
	}
	
	/**
	 * Closes SQL Connection.
	 * 
	 */
	
	public void closeConnection() {
		this.log.debug("I close SQL Connection.");
		this.closeResultSet();
		this.closeStatement();
		try {
			if (this.connection != null) {
				this.resultSet.close();
				this.resultSet = null;
			}
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while closing SQL Connection!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while closing SQL Connection!");
			e.printStackTrace();
		}
		this.log.debug("Successfully closed all connections.");
	}
	
}