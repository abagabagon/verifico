package data.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SQLData {
	
	private Logger log;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String sqlType;
	private String dbServer;
	private String dbName;
	private String user;
	private String password;
	private SQLDriver sqlDriver;

	public SQLData(String sqlType, String dbServer, String dbName, String user, String password) {
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
	
	
	public void openConnection() {
		this.log.info("I open SQL Connection.");
		this.sqlDriver = new SQLDriver(this.sqlType, this.dbServer, this.dbName, this.user, this.password);
		this.connection = this.sqlDriver.getSQLConnection();
	}
	
	public ResultSet execute(String sqlQuery) throws SQLTimeoutException {
		this.log.info("I execute SQL Statement.");
		try {
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(sqlQuery);
		} catch(SQLException e) {
			this.log.fatal("Encountered SQLException while executing SQL Statement!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
		} catch(Exception e) {
			this.log.fatal("Encountered Exception while executing SQL Statement!");
			e.printStackTrace();
		}
		return this.resultSet;
	}
	
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