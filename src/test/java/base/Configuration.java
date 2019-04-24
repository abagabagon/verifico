package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class Configuration {
	
	private static String basePath;
	private static String tester;
	
	private static int implicitWaitDuration;
	private static int explicitWaitDuration;
	
	private static String environment;
	private static String eventType;
	
	private static String baseUrl;
	private static String sqlServer;
	private static String sqlDbName;
	private static String sqlUser;
	private static String sqlPassword;
	
	private static String adminEMail;
	private static String adminPassword;
	private static String adminNewPassword;
	private static String adminFirstName;
	private static String adminLastName;
	private static String adminMobile;
	private static String adminBirth;
	private static String adminGender;
	private static String adminLanguage;
	
	private static String guestEMail;
	private static String guestPassword;
	private static String guestNewPassword;
	private static String guestFirstName;
	private static String guestLastName;
	private static String guestMobile;
	private static String guestBirth;
	private static String guestGender;
	private static String guestLanguage;
	
	private static String eoEMail;
	private static String eoPassword;
	private static String eoNewPassword;
	private static String eoFirstName;
	private static String eoLastName;
	private static String eoMobile;
	private static String eoBirth;
	private static String eoGender;
	private static String eoLanguage;
	
	private static String fbEMail;
	private static String fbPassword;
	private static String fbNewPassword;
	private static String fbFirstName;
	private static String fbLastName;
	private static String fbMobile;
	private static String fbBirth;
	private static String fbGender;
	private static String fbLanguage;
	
	private static String unregEMail;
	private static String unregPassword;
	private static String unregNewPassword;
	private static String unregFirstName;
	private static String unregLastName;
	private static String unregMobile;
	private static String unregBirth;
	private static String unregGender;
	private static String unregLanguage;
	
	private static String signUpEMail;
	private static String signUpPassword;
	private static String signUpNewPassword;
	private static String signUpFirstName;
	private static String signUpLastName;
	private static String signUpMobile;
	private static String signUpBirth;
	private static String signUpGender;
	private static String signUpLanguage;
	
	private static Logger log;
	private static Properties prop;
	private static FileInputStream propIn;
	
	static final void initialize() {
		log = LogManager.getLogger(Configuration.class.getName());
		log.debug("Initializing PouchCue Test Configuration.");
		initializePropertiesFile("./config/config.properties");
		setBasePath();
		setTester();
		setImplicitWaitDuration();
		setExplicitWaitDuration();
		setEnvironment();
		setEventType();
		setBaseUrl();
		setSqlServer();
		setSqlDbName();
		setSqlUser();
		setSqlPassword();
		setAdminEMail();
		setAdminPassword();
		setAdminNewPassword();
		setAdminFirstName();
		setAdminLastName();
		setAdminMobile();
		setAdminBirth();
		setAdminGender();
		setAdminLanguage();
		setGuestEMail();
		setGuestPassword();
		setGuestNewPassword();
		setGuestFirstName();
		setGuestLastName();
		setGuestMobile();
		setGuestBirth();
		setGuestGender();
		setGuestLanguage();
		setEoEMail();
		setEoPassword();
		setEoNewPassword();
		setEoFirstName();
		setEoLastName();
		setEoMobile();
		setEoBirth();
		setEoGender();
		setEoLanguage();
		setFbEMail();
		setFbPassword();
		setFbNewPassword();
		setFbFirstName();
		setFbLastName();
		setFbMobile();
		setFbBirth();
		setFbGender();
		setFbLanguage();
		setUnregEMail();
		setUnregPassword();
		setUnregNewPassword();
		setUnregFirstName();
		setUnregLastName();
		setUnregMobile();
		setUnregBirth();
		setUnregGender();
		setUnregLanguage();
		setSignUpEMail();
		setSignUpPassword();
		setSignUpNewPassword();
		setSignUpFirstName();
		setSignUpLastName();
		setSignUpMobile();
		setSignUpBirth();
		setSignUpGender();
		setSignUpLanguage();
		log.debug("Successfully initialized PouchCue Test Configuration.");
	}
	
	private static void initializePropertiesFile(String propertiesFilePath) {
		log.trace("Initializing Properties File.");
		prop = new Properties();
		try {
			propIn = new FileInputStream(propertiesFilePath);
		} catch (FileNotFoundException e) {
			log.fatal("Encountered FileNotFoundException while initializing Properties File!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while initializing Properties File!");
		}
		try {
			prop.load(propIn);
		} catch (IOException e) {
			log.fatal("Encountered IOException while loading Properties File!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while loading Properties File!");
		}
		log.trace("Successfully initialized Properties File.");
	}
	
	private static String checkAndSetProperty(String property) {
		String value = prop.getProperty(property);
		if (value == null) {
			log.fatal("The Property, \"" + property + "\" does not exist. Check if Properties File contains the property.");
			Assert.fail();
		}
		return value;
	}
	
	public static final String getBasePath() {
		return basePath;
	}
	
	public static final String getTester() {
		return tester;
	}
	
	public static final int getImplicitWaitDuration() {
		return implicitWaitDuration;
	}
	
	public static final int getExplicitWaitDuration() {
		return explicitWaitDuration;
	}
	
	public static final String getEnvironment() {
		return environment;
	}
	
	public static final String getEventType() {
		return eventType;
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
	
	public static final String getAdminEMail() {
		return adminEMail;
	}
	
	public static final String getAdminPassword() {
		return adminPassword;
	}
	
	public static final String getAdminNewPassword() {
		return adminNewPassword;
	}
	
	public static final String getAdminFirstName() {
		return adminFirstName;
	}
	
	public static final String getAdminLastName() {
		return adminLastName;
	}
	
	public static final String getAdminMobile() {
		return adminMobile;
	}
	
	public static final String getAdminBirth() {
		return adminBirth;
	}
	
	public static final String getAdminGender() {
		return adminGender;
	}
	
	public static final String getAdminLanguage() {
		return adminLanguage;
	}
	
	public static final String getGuestEMail() {
		return guestEMail;
	}
	
	public static final String getGuestPassword() {
		return guestPassword;
	}
	
	public static final String getGuestNewPassword() {
		return guestNewPassword;
	}
	
	public static final String getGuestFirstName() {
		return guestFirstName;
	}
	
	public static final String getGuestLastName() {
		return guestLastName;
	}
	
	public static final String getGuestMobile() {
		return guestMobile;
	}
	
	public static final String getGuestBirth() {
		return guestBirth;
	}
	
	public static final String getGuestGender() {
		return guestGender;
	}
	
	public static final String getGuestLanguage() {
		return guestLanguage;
	}
	
	public static final String getEoEMail() {
		return eoEMail;
	}
	
	public static final String getEoPassword() {
		return eoPassword;
	}
	
	public static final String getEoNewPassword() {
		return eoNewPassword;
	}
	
	public static final String getEoFirstName() {
		return eoFirstName;
	}
	
	public static final String getEoLastName() {
		return eoLastName;
	}
	
	public static final String getEoMobile() {
		return eoMobile;
	}
	
	public static final String getEoBirth() {
		return eoBirth;
	}
	
	public static final String getEoGender() {
		return eoGender;
	}
	
	public static final String getEoLanguage() {
		return eoLanguage;
	}
	
	public static final String getFbEMail() {
		return fbEMail;
	}
	
	public static final String getFbPassword() {
		return fbPassword;
	}
	
	public static final String getFbNewPassword() {
		return fbNewPassword;
	}
	
	public static final String getFbFirstName() {
		return fbFirstName;
	}
	
	public static final String getFbLastName() {
		return fbLastName;
	}
	
	public static final String getFbMobile() {
		return fbMobile;
	}
	
	public static final String getFbBirth() {
		return fbBirth;
	}
	
	public static final String getFbGender() {
		return fbGender;
	}
	
	public static final String getFbLanguage() {
		return fbLanguage;
	}
	
	public static final String getUnregEMail() {
		return unregEMail;
	}
	
	public static final String getUnregPassword() {
		return unregPassword;
	}
	
	public static final String getUnregNewPassword() {
		return unregNewPassword;
	}
	
	public static final String getUnregFirstName() {
		return unregFirstName;
	}
	
	public static final String getUnregLastName() {
		return unregLastName;
	}
	
	public static final String getUnregMobile() {
		return unregMobile;
	}
	
	public static final String getUnregBirth() {
		return unregBirth;
	}
	
	public static final String getUnregGender() {
		return unregGender;
	}
	
	public static final String getUnregLanguage() {
		return unregLanguage;
	}
	
	public static final String getSignUpEMail() {
		return signUpEMail;
	}
	
	public static final String getSignUpPassword() {
		return signUpPassword;
	}
	
	public static final String getSignUpNewPassword() {
		return signUpNewPassword;
	}
	
	public static final String getSignUpFirstName() {
		return signUpFirstName;
	}
	
	public static final String getSignUpLastName() {
		return signUpLastName;
	}
	
	public static final String getSignUpMobile() {
		return signUpMobile;
	}
	
	public static final String getSignUpBirth() {
		return signUpBirth;
	}
	
	public static final String getSignUpGender() {
		return signUpGender;
	}
	
	public static final String getSignUpLanguage() {
		return signUpLanguage;
	}
	
	private static final void setBasePath() {
		basePath = checkAndSetProperty("path");
	}
	
	private static final void setTester() {
		tester = checkAndSetProperty("tester");
	}
	
	private static final void setImplicitWaitDuration() {
		implicitWaitDuration = Integer.parseInt(checkAndSetProperty("wait.implicit.duration"));
	}
	
	private static final void setExplicitWaitDuration() {
		explicitWaitDuration = Integer.parseInt(checkAndSetProperty("wait.explicit.duration"));
	}
	
	private static final void setEnvironment() {
		environment = checkAndSetProperty("environment");
	}
	
	private static final void setEventType() {
		eventType = checkAndSetProperty("event.type");
	}
	
	private static final void setBaseUrl() {
		if (environment.equals("dev")) {
			baseUrl = checkAndSetProperty("url.base.dev");
		} else if (environment.equals("uat")) {
			baseUrl = checkAndSetProperty("url.base.uat");
		} else {
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static final void setSqlServer() {
		if (environment.equals("dev")) {
			sqlServer = checkAndSetProperty("sql.server.dev");
		} else if (environment.equals("uat")) {
			sqlServer = checkAndSetProperty("sql.server.uat");
		} else {
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static final void setSqlDbName() {
		if (environment.equals("dev")) {
			sqlDbName = checkAndSetProperty("sql.dbname.dev");
		} else if (environment.equals("uat")) {
			sqlDbName = checkAndSetProperty("sql.dbname.uat");
		} else {
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static final void setSqlUser() {
		if (environment.equals("dev")) {
			sqlUser = checkAndSetProperty("sql.user.dev");
		} else if (environment.equals("uat")) {
			sqlUser = checkAndSetProperty("sql.user.uat");
		} else {
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static final void setSqlPassword() {
		if (environment.equals("dev")) {
			sqlPassword = checkAndSetProperty("sql.password.dev");
		} else if (environment.equals("uat")) {
			sqlPassword = checkAndSetProperty("sql.password.uat");
		} else {
			log.fatal("The set \"environment\" Property is invalid.");
			Assert.fail();
		}
	}
	
	private static final void setAdminEMail() {
		adminEMail = checkAndSetProperty("user.admin.email");
	}
	
	private static final void setAdminPassword() {
		adminPassword = checkAndSetProperty("user.admin.pass");
	}
	
	private static final void setAdminNewPassword() {
		adminNewPassword = checkAndSetProperty("user.admin.passnew");
	}
	
	private static final void setAdminFirstName() {
		adminFirstName = checkAndSetProperty("user.admin.fname");
	}
	
	private static final void setAdminLastName() {
		adminLastName = checkAndSetProperty("user.admin.lname");
	}
	
	private static final void setAdminMobile() {
		adminMobile = checkAndSetProperty("user.admin.mobile");
	}
	
	private static final void setAdminBirth() {
		adminBirth = checkAndSetProperty("user.admin.birth");
	}
	
	private static final void setAdminGender() {
		adminGender = checkAndSetProperty("user.admin.gender");
	}
	
	private static final void setAdminLanguage() {
		adminLanguage = checkAndSetProperty("user.admin.lang");
	}
	
	private static final void setGuestEMail() {
		guestEMail = checkAndSetProperty("user.guest.email");
	}
	
	private static final void setGuestPassword() {
		guestPassword = checkAndSetProperty("user.guest.pass");
	}
	
	private static final void setGuestNewPassword() {
		guestNewPassword = checkAndSetProperty("user.guest.passnew");
	}
	
	private static final void setGuestFirstName() {
		guestFirstName = checkAndSetProperty("user.guest.fname");
	}
	
	private static final void setGuestLastName() {
		guestLastName = checkAndSetProperty("user.guest.lname");
	}
	
	private static final void setGuestMobile() {
		guestMobile = checkAndSetProperty("user.guest.mobile");
	}
	
	private static final void setGuestBirth() {
		guestBirth = checkAndSetProperty("user.guest.birth");
	}
	
	private static final void setGuestGender() {
		guestGender = checkAndSetProperty("user.guest.gender");
	}
	
	private static final void setGuestLanguage() {
		guestLanguage = checkAndSetProperty("user.guest.lang");
	}
	
	private static final void setEoEMail() {
		eoEMail = checkAndSetProperty("user.eo.email");
	}
	
	private static final void setEoPassword() {
		eoPassword = checkAndSetProperty("user.eo.pass");
	}
	
	private static final void setEoNewPassword() {
		eoNewPassword = checkAndSetProperty("user.eo.passnew");
	}
	
	private static final void setEoFirstName() {
		eoFirstName = checkAndSetProperty("user.eo.fname");
	}
	
	private static final void setEoLastName() {
		eoLastName = checkAndSetProperty("user.eo.lname");
	}
	
	private static final void setEoMobile() {
		eoMobile = checkAndSetProperty("user.eo.mobile");
	}
	
	private static final void setEoBirth() {
		eoBirth = checkAndSetProperty("user.eo.birth");
	}
	
	private static final void setEoGender() {
		eoGender = checkAndSetProperty("user.eo.gender");
	}
	
	private static final void setEoLanguage() {
		eoLanguage = checkAndSetProperty("user.eo.lang");
	}
	
	private static final void setFbEMail() {
		fbEMail = checkAndSetProperty("user.fb.email");
	}
	
	private static final void setFbPassword() {
		fbPassword = checkAndSetProperty("user.fb.pass");
	}
	
	private static final void setFbNewPassword() {
		fbNewPassword = checkAndSetProperty("user.fb.passnew");
	}
	
	private static final void setFbFirstName() {
		fbFirstName = checkAndSetProperty("user.fb.fname");
	}
	
	private static final void setFbLastName() {
		fbLastName = checkAndSetProperty("user.fb.lname");
	}
	
	private static final void setFbMobile() {
		fbMobile = checkAndSetProperty("user.fb.mobile");
	}
	
	private static final void setFbBirth() {
		fbBirth = checkAndSetProperty("user.fb.birth");
	}
	
	private static final void setFbGender() {
		fbGender = checkAndSetProperty("user.fb.gender");
	}
	
	private static final void setFbLanguage() {
		fbLanguage = checkAndSetProperty("user.fb.lang");
	}
	
	private static final void setUnregEMail() {
		unregEMail = checkAndSetProperty("user.unreg.email");
	}
	
	private static final void setUnregPassword() {
		unregPassword = checkAndSetProperty("user.unreg.pass");
	}
	
	private static final void setUnregNewPassword() {
		unregNewPassword = checkAndSetProperty("user.unreg.passnew");
	}
	
	private static final void setUnregFirstName() {
		unregFirstName = checkAndSetProperty("user.unreg.fname");
	}
	
	private static final void setUnregLastName() {
		unregLastName = checkAndSetProperty("user.unreg.lname");
	}
	
	private static final void setUnregMobile() {
		unregMobile = checkAndSetProperty("user.unreg.mobile");
	}
	
	private static final void setUnregBirth() {
		unregBirth = checkAndSetProperty("user.unreg.birth");
	}
	
	private static final void setUnregGender() {
		unregGender = checkAndSetProperty("user.unreg.gender");
	}
	
	private static final void setUnregLanguage() {
		unregLanguage = checkAndSetProperty("user.unreg.lang");
	}
	
	private static final void setSignUpEMail() {
		signUpEMail = checkAndSetProperty("user.signup.email");
	}
	
	private static final void setSignUpPassword() {
		signUpPassword = checkAndSetProperty("user.signup.pass");
	}
	
	private static final void setSignUpNewPassword() {
		signUpNewPassword = checkAndSetProperty("user.signup.passnew");
	}
	
	private static final void setSignUpFirstName() {
		signUpFirstName = checkAndSetProperty("user.signup.fname");
	}
	
	private static final void setSignUpLastName() {
		signUpLastName = checkAndSetProperty("user.signup.lname");
	}
	
	private static final void setSignUpMobile() {
		signUpMobile = checkAndSetProperty("user.signup.mobile");
	}
	
	private static final void setSignUpBirth() {
		signUpBirth = checkAndSetProperty("user.signup.birth");
	}
	
	private static final void setSignUpGender() {
		signUpGender = checkAndSetProperty("user.signup.gender");
	}
	
	private static final void setSignUpLanguage() {
		signUpLanguage = checkAndSetProperty("user.signup.lang");
	}
}
