package com.github.abagabagon.verifico.data.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abagabagon.verifico.enums.SQL;

/**
 * SQL Data configurations and connections handling
 * 
 * @author albagabagon
 *
 */

public class SQLDriver {
	
	private Logger log;
	private SQL sqlType;
	private String dbServer;
	private String dbName;
	private String user;
	private String password;
	private Connection connection;
	private String url;
	
	public SQLDriver(SQL sqlType, String dbServer, String dbName, String user, String password) {
		this.log = LogManager.getLogger(this.getClass());
		this.sqlType = sqlType;
		this.dbServer = dbServer;
		this.dbName = dbName;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Initializes SQL Connection.
	 * 
	 * @throws ExceptionInInitializerError
	 * @return SQL Connection
	 */
	
	Connection getSQLConnection() throws ExceptionInInitializerError {
		try {
			this.log.debug("Initializing " + this.sqlType + " JDBC Driver.");
			switch(this.sqlType) {
			case MySQL:
				Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
				this.connection = this.getMySQLConnection();
				break;
			case MSSQL:
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").getDeclaredConstructor().newInstance();
				this.connection = this.getMSSQLConnection();
				break;
			default:
				this.log.fatal(this.sqlType + " is an unsupported SQL Type.");
			}
		} catch (LinkageError e) {
			this.log.fatal("Encountered LinkageError while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			this.log.fatal("Encountered ClassNotFoundException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (NoSuchMethodException e) {
			this.log.fatal("Encountered NoSuchMethodException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (SecurityException e) {
			this.log.fatal("Encountered SecurityException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (IllegalAccessException e) {
			this.log.fatal("Encountered IllegalAccessException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (IllegalArgumentException e) {
			this.log.fatal("Encountered IllegalArgumentException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (InstantiationException e) {
			this.log.fatal("Encountered InstantiationException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (InvocationTargetException e) {
			this.log.fatal("Encountered InvocationTargetException while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while instantiating " + this.sqlType + " JDBC Driver!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		}
		
		return this.connection;
	}
	
	/**
	 * Initializes MySQL Connection.
	 * 
	 * @return MySQL Connection
	 */
	
	private Connection getMySQLConnection() {
		this.log.debug("Initializing MySQL Connection.");
		this.url = "jdbc:mysql://" + this.dbServer + ":3306/" + this.dbName;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.url, this.user, this.password);
			this.log.debug("Successfully initialized SQL Connection.");
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while initializing MySQL Connection!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing MySQL Connection!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		}
		return connection;
	}
	
	/**
	 * Initializes MSSQL Connection.
	 * 
	 * @return MSSQL Connection
	 */
	
	private Connection getMSSQLConnection() {
		this.log.debug("Initializing MSSQL Connection.");
		this.url = "jdbc:sqlserver://" + this.dbServer + ":1433/" + this.dbName;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(this.url, this.user, this.password);
			this.log.debug("Successfully initialized SQL Connection.");
		} catch (SQLException e) {
			this.log.fatal("Encountered SQLException while initializing MSSQL Connection!");
			this.log.fatal("SQL Exception: " + e.getMessage());
			this.log.fatal("SQL State: " + e.getSQLState());
			this.log.fatal("Error Code: " + e.getErrorCode());
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing MSSQL Connection!");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		}
		return connection;
	}

}
