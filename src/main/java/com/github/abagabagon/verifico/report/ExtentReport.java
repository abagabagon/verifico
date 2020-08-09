package com.github.abagabagon.verifico.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReport {
	
	private static ExtentReports report;
	private static ExtentSparkReporter reporter;
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
	 * @param testSuiteName Test Suite Name
	 * @param organization	Organization Name
	 */
	
	public static final void setExtentReport(String testSuiteName, String organization) {
		log.debug("Initializing ExtentReport Instance.");
		setExtentSparkReporter(testSuiteName);
		report = new ExtentReports();
		report.setSystemInfo("Organization", organization);
		report.attachReporter(reporter);
		log.debug("Successfully initialized ExtentReport Instance.");
	}
	
	/**
	 * Gets an instance of ExtentReports
	 * 
	 * @return Instance of ExtentReports
	 */
	
	public static final ExtentReports getReport() {
		log.debug("Retrieving ExtentReports Instance.");
		if (isExtentReportsNull()) {
			log.warn("Unable to retrieve ExtentReports Instance. Make sure an instance of ExtentReports has been created.");
		}
		return report;
	}
	
	/**
	 * Initializes ExtentSparkReporter instance.
	 * 
	 * @param fileName Output File Name
	 */
	
	private static final void setExtentSparkReporter(String fileName) {
		log.debug("Initializing ExtentSparkReporter Instance.");
		String filePath = "./reports/";
		reporter = new ExtentSparkReporter(filePath + fileName);
		reporter.config().setTheme(Theme.DARK);
		reporter.config().setDocumentTitle(fileName);
		reporter.config().setEncoding("utf-8");
		reporter.config().setReportName(fileName.toString());
		log.debug("Successfully initialized ExtentSparkReporter Instance.");
	}
	
	/**
	 * Initializes ExtentTest instance.
	 * 
	 * @param testName Name of Test Case being executed.
	 * @param description  Description of the current Test Case 
	 */
	
	public static final void setTest(String testName, String description) {
		log.debug("Initializing ExtentTest Instance.");
		if(isExtentReportsNull()) {
			log.warn("Unable to set test. Make sure an instance of ExtentReports has been created.");
		} else {
			test = report.createTest(testName, description);
			log.debug("Successfully initialized ExtentTest Instance.");
		}
	}
	
	/**
	 * Gets an instance of ExtentTest
	 * 
	 * @return Instance of ExtentTest
	 */
	
	public static final ExtentTest getTest() {
		log.debug("Retrieving ExtentTest Instance.");
		if (isExtentTestNull()) {
			log.warn("Unable to retrieve ExtentTest Instance. Make sure an instance of ExtentTest has been created.");
		}
		return test;
	}
	
	/**
	 * Sets the Author of the current Test being executed.
	 * 
	 * @param author Name of the Tester executing current Test Case.
	 */
	
	public static final void setAuthor(String author) {
		log.debug("Setting Author for ExtentTest.");
		if (isExtentTestNull()) {
			log.warn("Unable to assign the Author for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			test.assignAuthor(author);
			log.debug("Successfully set Author for ExtentTest.");
		}
	}
	
	/**
	 * Sets the Category of the current Test being executed.
	 * 
	 * @param category Name of the Category of current Test Case.
	 */
	
	public static final void setCategory(String category) {
		log.debug("Setting Category for ExtentTest.");
		if (isExtentTestNull()) {
			log.warn("Unable to assign the Category for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			test.assignCategory(category);
			log.debug("Successfully set Category for ExtentTest.");
		}
	}
	
	/**
	 * Sets the Device for the test.
	 * 
	 */
	
	public static final void setDevice() {
		log.debug("Setting Device for ExtentTest.");
		if (isExtentTestNull()) {
			log.warn("Unable to assign the Device for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			String device = System.getProperty("os.version");
			test.assignDevice(device);
			log.debug("Successfully set Device for ExtentTest.");
		}
	}
	
	/**
	 * Logs test event as INFO
	 * 
	 * @param details Details of the Test Event.
	 */
	
	public static final void logInfo(String details) {
		if(isExtentTestNull()) {
			log.warn("\"INFO\" Status could not be logged at ExtentReports.");
		} else {
			log.info(details);
			test.log(Status.INFO, details);
		}
	}
	
	/**
	 * Logs test event as PASSED
	 * 
	 * @param testCaseName Name of the Test Case that PASSED
	 */
	
	public static final void logPassed(String testCaseName) {
		if(isExtentTestNull()) {
			log.warn("\"PASS\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "PASSED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.GREEN);
			test.log(Status.PASS, markUp);
		}
	}

	/**
	 * Logs test event as FAILED
	 * 
	 * @param testCaseName Name of the Test Case that FAILED
	 */
	
	public static final void logFailed(String testCaseName) {
		if(isExtentTestNull()) {
			log.warn("\"FAIL\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "FAILED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.RED);
			test.log(Status.FAIL, markUp);
		}
	}

	/**
	 * Logs test event as SKIPPED
	 * 
	 * @param testCaseName Name of the Test Case that was SKIPPED
	 */
	
	public static final void logSkipped(String testCaseName) {
		if(isExtentTestNull()) {
			log.warn("\"SKIP\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "SKIPPED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.YELLOW);
			test.log(Status.SKIP, markUp);
		}
	}
	
	/**
	 * Generates the ExtentReports Report File.
	 * 
	 */
	
	public static final void generateExtentReport() {
		log.debug("Generating ExtentReports.");
		if(isExtentReportsNull()) {
			log.warn("Unable to generate ExtentReports. Make sure an instance of ExtentReports has been created.");
		} else {
			try {
				report.flush();
				report = null;
			} catch(Exception e) {
				System.out.println("An Exception occurred while closing ExtentReports.");
			}
			log.debug("Successfully generated ExtentReports.");
		}
	}
	
	/**
	 * Checks if ExtentReports is NULL
	 * 
	 * @return	<code>true</code> if ExtentReports is NULL.
	 * 			<code>false</code> if ExtentReports is not NULL.
	 */
	
	private static boolean isExtentReportsNull() {
		log.trace("Checking if ExtentReport is NULL");
		if(report == null) {
			log.debug("ExtentReport Instance is NULL.");
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if ExtentTest is NULL
	 * 
	 * @return	<code>true</code> if ExtentTest is NULL.
	 * 			<code>false</code> if ExtentTest is not NULL.
	 */
	
	private static boolean isExtentTestNull() {
		log.trace("Checking if ExtentTest is NULL");
		if(test == null) {
			log.debug("ExtentTest Instance is NULL.");
			return true;
		} else {
			return false;
		}
	}
	
}
