package settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import data.enums.Environment;

public class Settings {
	
	/*#######################################################*/
	/*                       VARIABLES                       */
	/*#######################################################*/

	private static int implicitWaitDuration;
	private static int explicitWaitDuration;

	private static String automationTool;
	private static String baseUrl;
	private static String sqlServer;
	private static String sqlDbName;
	private static String sqlUser;
	private static String sqlPassword;
	
	private static Logger log;
	private static Properties prop;
	private static FileInputStream propIn;
	
	public Settings(Environment environment) {
		log = LogManager.getLogger(Settings.class);
		log.debug("Initializing Settings Class.");
		initializePropertiesFile("./src/main.resources/config/config.properties");
		setImplicitWaitDuration();
		setExplicitWaitDuration();
		setAutomationTool();
		setBaseUrl(environment);
		setSqlServer(environment);
		setSqlDbName(environment);
		setSqlUser(environment);
		setSqlPassword(environment);
		log.debug("Successfully initialized Settings Class.");
	}
	
	/*#######################################################*/
	/*                        METHODS                        */
	/*#######################################################*/
	
	private static void initializePropertiesFile(String propertiesFilePath) {
		log.trace("Initializing Properties File.");
		prop = new Properties();
		try {
			propIn = new FileInputStream(propertiesFilePath);
		} catch (FileNotFoundException e) {
			log.fatal("Encountered FileNotFoundException while initializing Properties File!");
		} catch (SecurityException e) {
			log.fatal("Encountered SecurityException while initializing Properties File!");
		}catch (Exception e) {
			log.fatal("Encountered Exception while initializing Properties File!");
		}
		try {
			prop.load(propIn);
		} catch (IOException e) {
			log.fatal("Encountered IOException while loading Properties File!");
		} catch (IllegalArgumentException e) {
			log.fatal("Encountered IOException while loading Properties File!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while loading Properties File!");
		}
		log.trace("Successfully initialized Properties File.");
	}
	
	private static String checkAndSetProperty(String property) {
		String value = prop.getProperty(property);
		if (value == null) {
			log.fatal("The Property Key, \"" + property + "\" does not exist or has a NULL Value. Check if Properties File contains the property or if value is NULL.");
			Assert.fail();
		}
		return value;
	}
	
	/*#######################################################*/
	/*                       GETTERS                         */
	/*#######################################################*/
	
	public static final int getImplicitWaitDuration() {
		return implicitWaitDuration;
	}
	
	public static final int getExplicitWaitDuration() {
		return explicitWaitDuration;
	}
	
	public static final String getAutomationTool() {
		return automationTool;
	}
	
	public static final String getBaseUrl() {
		return baseUrl;
	}
	
	public static final String getSqlServer() {
		return sqlServer;
	}
	
	public static final String getSqlDbName() {
		return sqlDbName;
	}
	
	public static final String getSqlUser() {
		return sqlUser;
	}
	
	public static final String getSqlPassword() {
		return sqlPassword;
	}
	
	/*#######################################################*/
	/*                       SETTERS                         */
	/*#######################################################*/
	
	private static void setImplicitWaitDuration() {
		try {
			implicitWaitDuration = Integer.parseInt(checkAndSetProperty("wait.implicit.duration"));
		} catch (NumberFormatException e) {
			log.warn("Encountered NumberFormatException while setting Implicit Wait Duration");
		} catch (Exception e) {
			log.warn("Encountered Exception while setting Implicit Wait Duration");
		} 
		
	}
	
	private static void setExplicitWaitDuration() {
		try {
			explicitWaitDuration = Integer.parseInt(checkAndSetProperty("wait.explicit.duration"));
		} catch (NumberFormatException e) {
			log.warn("Encountered NumberFormatException while setting Explicit Wait Duration");
		} catch (Exception e) {
			log.warn("Encountered Exception while setting Explicit Wait Duration");
		} 
	}
	
	private static void setAutomationTool() {
		automationTool = checkAndSetProperty("automation.tool");
	}
	
	private static void setBaseUrl(Environment environment) {
		switch (environment) {
		case DEVELOPMENT:
			baseUrl = checkAndSetProperty("url.base.development");
			break;
		case STAGING:
			baseUrl = checkAndSetProperty("url.base.staging");
			break;
		case PRODUCTION:
			baseUrl = checkAndSetProperty("url.base.production");
			break;
		default:
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static void setSqlServer(Environment environment) {
		switch (environment) {
		case DEVELOPMENT:
			sqlServer = checkAndSetProperty("sql.server.development");
			break;
		case STAGING:
			sqlServer = checkAndSetProperty("sql.server.staging");
			break;
		case PRODUCTION:
			baseUrl = checkAndSetProperty("url.base.production");
			break;
		default:
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static void setSqlDbName(Environment environment) {
		switch (environment) {
		case DEVELOPMENT:
			sqlDbName = checkAndSetProperty("sql.dbname.development");
			break;
		case STAGING:
			sqlDbName = checkAndSetProperty("sql.dbname.staging");
			break;
		case PRODUCTION:
			sqlDbName = checkAndSetProperty("sql.dbname.production");
			break;
		default:
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static void setSqlUser(Environment environment) {
		switch (environment) {
		case DEVELOPMENT:
			sqlUser = checkAndSetProperty("sql.user.development");
			break;
		case STAGING:
			sqlUser = checkAndSetProperty("sql.user.staging");
			break;
		case PRODUCTION:
			sqlUser = checkAndSetProperty("sql.user.production");
			break;
		default:
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static void setSqlPassword(Environment environment) {
		switch (environment) {
		case DEVELOPMENT:
			sqlPassword = checkAndSetProperty("sql.password.development");
			break;
		case STAGING:
			sqlPassword = checkAndSetProperty("sql.password.staging");
			break;
		case PRODUCTION:
			sqlPassword = checkAndSetProperty("sql.password.production");
			break;
		default:
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}

}
