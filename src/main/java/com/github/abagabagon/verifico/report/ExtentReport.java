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

/**
 * Implemented Extent Report Reporter
 *
 * @author albagabagon
 *
 */

public class ExtentReport implements Reporter {

	private ExtentReports report;
	private ExtentSparkReporter reporter;
	private ExtentTest test;
	private Logger log;

	public ExtentReport() {
		this.log = LogManager.getLogger(this.getClass());
	}

	@Override
	public void setReport(String testSuiteName, String organization) {
		this.log.debug("Initializing ExtentReport Instance.");
		this.setExtentSparkReporter(testSuiteName);
		this.report = new ExtentReports();
		this.report.setSystemInfo("Organization", organization);
		this.report.attachReporter(this.reporter);
		this.log.debug("Successfully initialized ExtentReport Instance.");
	}

	/**
	 * Initializes ExtentSparkReporter instance.
	 *
	 * @param fileName Output File Name
	 */

	private void setExtentSparkReporter(String fileName) {
		this.log.debug("Initializing ExtentSparkReporter Instance.");
		String filePath = "./reports/";
		this.reporter = new ExtentSparkReporter(filePath + fileName);
		this.reporter.config().setTheme(Theme.DARK);
		this.reporter.config().setDocumentTitle(fileName);
		this.reporter.config().setEncoding("utf-8");
		this.reporter.config().setReportName(fileName.toString());
		this.log.debug("Successfully initialized ExtentSparkReporter Instance.");
	}

	@Override
	public void setTest(String testName, String description) {
		this.log.debug("Initializing ExtentTest Instance.");
		if(this.isExtentReportsNull()) {
			this.log.warn("Unable to set test. Make sure an instance of ExtentReports has been created.");
		} else {
			this.test = this.report.createTest(testName, description);
			this.log.debug("Successfully initialized ExtentTest Instance.");
		}
	}

	@Override
	public void setAuthor(String author) {
		this.log.debug("Setting Author for ExtentTest.");
		if (this.isExtentTestNull()) {
			this.log.warn("Unable to assign the Author for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			this.test.assignAuthor(author);
			this.log.debug("Successfully set Author for ExtentTest.");
		}
	}

	@Override
	public void setCategory(String category) {
		this.log.debug("Setting Category for ExtentTest.");
		if (this.isExtentTestNull()) {
			this.log.warn("Unable to assign the Category for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			this.test.assignCategory(category);
			this.log.debug("Successfully set Category for ExtentTest.");
		}
	}

	@Override
	public void setDevice() {
		this.log.debug("Setting Device for ExtentTest.");
		if (this.isExtentTestNull()) {
			this.log.warn("Unable to assign the Device for the Test. Make sure an instance of ExtentTest has been created.");
		} else {
			String device = System.getProperty("os.version");
			this.test.assignDevice(device);
			this.log.debug("Successfully set Device for ExtentTest.");
		}
	}

	@Override
	public void info(String details) {
		if(this.isExtentTestNull()) {
			this.log.warn("\"INFO\" Status could not be logged at ExtentReports.");
		} else {
			this.log.info(details);
			this.test.log(Status.INFO, details);
		}
	}

	@Override
	public void pass(String testCaseName) {
		if(this.isExtentTestNull()) {
			this.log.warn("\"PASS\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "PASSED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.GREEN);
			this.test.log(Status.PASS, markUp);
		}
	}

	@Override
	public void fail(String testCaseName) {
		if(this.isExtentTestNull()) {
			this.log.warn("\"FAIL\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "FAILED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.RED);
			this.test.log(Status.FAIL, markUp);
		}
	}

	@Override
	public void skip(String testCaseName) {
		if(this.isExtentTestNull()) {
			this.log.warn("\"SKIP\" Status could not be logged at ExtentReports.");
		} else {
	        String details = "Test Case: \"" + testCaseName + "\" has " + "<b>" + "SKIPPED" + "</b>";
	        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.YELLOW);
			this.test.log(Status.SKIP, markUp);
		}
	}

	@Override
	public void generateReport() {
		this.log.debug("Generating ExtentReports.");
		if(this.isExtentReportsNull()) {
			this.log.warn("Unable to generate ExtentReports. Make sure an instance of ExtentReports has been created.");
		} else {
			try {
				this.report.flush();
				this.report = null;
			} catch(Exception e) {
				this.log.error("An Exception occurred while closing ExtentReports.");
			}
			this.log.debug("Successfully generated ExtentReports.");
		}
	}

	/**
	 * Checks if ExtentReports is NULL
	 *
	 * @return <code>true</code> if ExtentReports is NULL. <code>false</code> if
	 *         ExtentReports is not NULL.
	 */

	private boolean isExtentReportsNull() {
		this.log.trace("Checking if ExtentReport is NULL");
		if(this.report == null) {
			this.log.debug("ExtentReport Instance is NULL.");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if ExtentTest is NULL
	 *
	 * @return <code>true</code> if ExtentTest is NULL. <code>false</code> if
	 *         ExtentTest is not NULL.
	 */

	private boolean isExtentTestNull() {
		this.log.trace("Checking if ExtentTest is NULL");
		if(this.test == null) {
			this.log.debug("ExtentTest Instance is NULL.");
			return true;
		} else {
			return false;
		}
	}

}
