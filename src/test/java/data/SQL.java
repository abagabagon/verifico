package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SQL {
	
	private static Logger log;
	private static String url;
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	private static final String DRIVER_MYSQL = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		
	static {
		log = LogManager.getLogger(SQL.class.getName());
	}
	
	/**
	 * Gets data from SQL Database.
	 * 
	 * @param  dbServer IP Address of Database Server where database to be accessed resides.
	 * @param  dbName   Database Name of SQL Database.
	 * @param  user     User for accessing SQL Database.
	 * @param  password Password of user accessing the SQL Database.
	 * @param  sqlScript SQL Script to be executed.
	 * @return SQL Data retrieved based on SQL Script.
	 */

	public static ResultSet getSqlData(String dbServer, String dbName, String user, String password, String sqlScript) {
		log.debug("Initializing MySQL JDBC Driver.");
		try {
			Class.forName(DRIVER_MYSQL).newInstance();
			url = "jdbc:mysql://" + dbServer + ":3306/" + dbName;
			log.debug("Successfully initialized MySQL JDBC Driver.");
		} catch (InstantiationException e) {
			log.fatal("Encountered InstantiationException while initializing MySQL JDBC Driver!");
		} catch (IllegalAccessException e) {
			log.fatal("Encountered IllegalAccessException while initializing MySQL JDBC Driver!");
		} catch (ClassNotFoundException e) {
			log.fatal("Encountered ClassNotFoundException while initializing MySQL JDBC Driver!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while initializing MySQL JDBC Driver!");
		}
		log.debug("Getting SQL Connection.");
		try {
			conn = DriverManager.getConnection(url, user, password);
			log.debug("Successfully retrieved SQL Connection.");
		} catch (SQLException e) {
			log.fatal("Encountered SQLException while getting MySQL Connection!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while getting MySQL Connection!");
		}
		log.debug("Creating SQL Statement.");
		try {
			stmt = conn.createStatement();
			log.debug("Successfully created SQL Statement.");
		} catch (SQLException e) {
			log.fatal("Encountered SQLException while creating SQL Statement!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while creating SQL Statement!");
		}
		log.info("Getting SQL Data.");
		if (sqlScript.toLowerCase().trim().startsWith("select")) {
			try {
				rs = stmt.executeQuery(sqlScript);
				log.info("Successfully retrieved SQL Data.");
			} catch(SQLException e) {
				log.fatal("Encountered SQLException while retrieving SQL Data!");
			} catch(Exception e) {
				log.fatal("Encountered Exception while retrieving SQL Data!");
			}
		} else {
			log.error("Invalid SQL Script! Only \"SELECT\" Statements are allowed.");
		}
		return rs;
	}
}
