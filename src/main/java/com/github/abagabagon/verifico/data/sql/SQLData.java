package com.github.abagabagon.verifico.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abagabagon.verifico.enums.SQL;

/**
 * SQL Data manipulation and retrieval
 * 
 * @author albagabagon
 *
 */

public class SQLData {
	
	private Logger log;
	private Connection connection;
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
	 * @return SQL Connection Object.
	 */
	
	public Connection openConnection() {
		this.log.debug("I open SQL Connection.");
		this.sqlDriver = new SQLDriver(this.sqlType, this.dbServer, this.dbName, this.user, this.password);
		this.connection = this.sqlDriver.getSQLConnection();
		return this.connection;
	}
	
	/**
	 * Executes a SQL SELECT Statement and returns the ResultSet.
	 * 
	 * @param preparedStatement SQL SELECT Query in PreparedStatement Object
	 * @return	ResultSet based on the SQL SELECT Query
	 */
	
	public ResultSet select(PreparedStatement preparedStatement) {
		this.log.debug("I execute SQL SELECT Statement.");
		try {
			this.resultSet = preparedStatement.executeQuery();
		} catch (SQLTimeoutException e) {
			this.log.fatal("Encountered SQLTimeoutException while executing SQL UPDATE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while executing SQL UPDATE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while executing SQL UPDATE Statement!");
			e.printStackTrace();
		}

		return this.resultSet;
	}
	
	/**
	 * Executes a SQL UPDATE Statement.
	 * 
	 * @param preparedStatement SQL UPDATE Query in PreparedStatement Object
	 */
	
	public void update(PreparedStatement preparedStatement) {
		this.log.debug("I execute SQL UPDATE Statement.");
		int updateCount = 0;
		try {
			updateCount = preparedStatement.executeUpdate();
		} catch (SQLTimeoutException e) {
			this.log.fatal("Encountered SQLTimeoutException while executing SQL UPDATE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while executing SQL UPDATE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while executing SQL UPDATE Statement!");
			e.printStackTrace();
		}

		if (updateCount > 0) {
			this.log.debug(updateCount + " SQL Record(s) updated.");
		} else {
			this.log.error("No SQL Records were updated.");
		}
	}
	
	/**
	 * Executes a SQL INSERT Statement.
	 * 
	 * @param preparedStatement SQL INSERT Query in PreparedStatement Object
	 */
	
	public void insert(PreparedStatement preparedStatement) {
		this.log.debug("I execute SQL INSERT Statement.");
		int insertCount = 0;
		try {
			insertCount = preparedStatement.executeUpdate();
		} catch (SQLTimeoutException e) {
			this.log.fatal("Encountered SQLTimeoutException while executing SQL INSERT Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while executing SQL INSERT Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while executing SQL INSERT Statement!");
			e.printStackTrace();
		}

		if (insertCount > 0) {
			this.log.debug(insertCount + " SQL Record(s) inserted.");
		} else {
			this.log.error("No SQL Records were inserted.");
		}
	}
	
	/**
	 * Executes a SQL DELETE Statement.
	 * 
	 * @param preparedStatement SQL DELETE Query in PreparedStatement Object
	 */
	
	public void delete(PreparedStatement preparedStatement) {
		this.log.debug("I execute SQL DELETE Statement.");
		int deleteCount = 0;
		try {
			deleteCount = preparedStatement.executeUpdate();
		} catch (SQLTimeoutException e) {
			this.log.fatal("Encountered SQLTimeoutException while executing SQL DELETE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while executing SQL DELETE Statement!");
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while executing SQL DELETE Statement!");
			e.printStackTrace();
		}

		if (deleteCount > 0) {
			this.log.debug(deleteCount + " SQL Record(s) deleted.");
		} else {
			this.log.error("No SQL Records were deleted.");
		}
	}
	
	/**
	 * Closes and empties the ResultSet.
	 */
	
	private void closeResultSet() {
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
	 * Closes SQL Connection.
	 */
	
	public void closeConnection() {
		this.log.debug("I close SQL Connection.");
		this.closeResultSet();
		try {
			if (this.connection != null) {
				this.connection.close();
				this.connection = null;
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