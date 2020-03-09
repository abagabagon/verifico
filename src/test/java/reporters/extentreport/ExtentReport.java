package reporters.extentreport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utilities.OperatingSystem;

public class ExtentReport {
	
	private static ExtentReports report;
	private static ExtentTest test;
	private static Logger log;
	
	static {
		log = LogManager.getLogger(ExtentReport.class);
		log.debug("Initializing ExtentReport Class.");
		log.debug("Successfully initialized ExtentReport Class.");
	}
	
	/**
	 * Initializes ExtentReports instance.
	 * 
	 * @param  testSuiteName Name of the Test Suite
	 */
	
	public static final void setReport(String testSuiteName) {
		log.debug("Initializing ExtentReport Instance.");
		String filePath = "./reports/" + testSuiteName + " " + OperatingSystem.getCurrentDate("yyyyMMddHHmmss") + ".html";
		report = new ExtentReports(filePath, true);
		log.debug("Successfully initialized ExtentReport Instance.");
	}
	
	/**
	 * Gets an instance of ExtentReports
	 * 
	 * @return Instance of ExtentReports
	 */
	
	public static final ExtentReports getReport() {
		log.debug("Retrieving ExtentReport Instance.");
		return report;
	}
	
	/**
	 * Gets an instance of ExtentTest
	 * 
	 * @return Instance of ExtentTest
	 */
	
	public static final ExtentTest getTest() {
		log.debug("Retrieving ExtentTest Instance.");
		return test;
	}

	/**
	 * Initializes ExtentTest instance.
	 * 
	 * @param testCaseName Name of Test Case being executed.
	 * @param author       Name of the Tester executing current Test Case.
	 * @param category     Name of the Category current Test Case is under.
	 * @param description  Description of the current Test Case 
	 */
	
	public static final void setTest(String testCaseName) {
		log.debug("Initializing ExtentTest Instance.");
		test = report.startTest(testCaseName);
		log.debug("Successfully initialized ExtentTest Instance.");
	}
	
	/**
	 * Sets the Author of the current Test being executed.
	 * 
	 * @param author Name of the Tester executing current Test Case.
	 */
	
	public static final void setAuthor(String author) {
		log.debug("Setting Author for ExtentTest.");
		test.assignAuthor(author);
		log.debug("Successfully set Author for ExtentTest.");
	}
	
	/**
	 * Sets the Category of the current Test being executed.
	 * 
	 * @param category Name of the Category of current Test Case.
	 */
	
	public static final void setCategory(String category) {
		log.debug("Setting Category for ExtentTest.");
		test.assignCategory(category);
		log.debug("Successfully set Category for ExtentTest.");
	}

	/**
	 * Sets the Description of the current Test being executed.
	 * 
	 * @param description Description of current Test Case.
	 */
	
	public static final void setDescription(String description) {
		log.debug("Setting Description for ExtentTest.");
		test.setDescription(description);
		log.debug("Successfully set Description for ExtentTest.");
	}
	
	/**
	 * Logs test event as PASSED
	 * 
	 * @param details Details of the Test Event.
	 */
	
	public static final void logPassed(String details) {
		boolean isTestNull = ExtentReport.isTestNull(test);
		if(isTestNull) {
			log.fatal("ExtentTest Instance is NULL.");
		} else {
			test.log(LogStatus.PASS, details);
		}
	}

	/**
	 * Logs test event as having ERRORS
	 * 
	 * @param details Details of the Test Event.
	 */
	
	public static final void logError(String details) {
		boolean isTestNull = ExtentReport.isTestNull(test);
		if(isTestNull) {
			log.fatal("ExtentTest Instance is NULL.");
		} else {
			test.log(LogStatus.ERROR, details);
		}
	}

	/**
	 * Logs test event as FAILED
	 * 
	 * @param details Details of the Test Event.
	 */
	
	public static final void logFailed(String details) {
		boolean isTestNull = ExtentReport.isTestNull(test);
		if(isTestNull) {
			log.fatal("ExtentTest Instance is NULL.");
		} else {
			test.log(LogStatus.FAIL, details);
		}
	}

	/**
	 * Logs test event as SKIPPED
	 * 
	 * @param details Details of the Test Event.
	 */
	
	public static final void logSkipped(String details) {
		boolean isTestNull = ExtentReport.isTestNull(test);
		if(isTestNull) {
			log.fatal("ExtentTest Instance is NULL.");
		} else {
			test.log(LogStatus.SKIP, details);
		}
	}
	
	/**
	 * Ends the Test.
	 * 
	 */
	
	public static final void endTest() {
		log.debug("Ending Test.");
		report.endTest(test);
		log.debug("Successfully ended Test.");
	}
	
	/**
	 * Generates the ExtentReport HTML Report File.
	 * 
	 */
	
	public static final void generateExtentReport() {
		log.debug("Generating ExtentReport.");
		try {
			report.flush();
			report.close();
		} catch(Exception e) {
			System.out.println("An Exception occurred while closing ExtentReport");
		}
		log.debug("Successfully generated ExtentReport.");
	}
	
	private static boolean isTestNull(ExtentTest test) {
		log.trace("Checking if ExtentTest is NULL");
		if(test == null) {
			log.trace("ExtentTest Instance is NULL.");
			return true;
		} else {
			return false;
		}
	}
	
}
