package base;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import listeners.TestListener;
import reporters.ExtentReport;
import selenium.Driver;
import selenium.Selenium;

public class Test {
	
	protected static Logger log;
	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static ExtentReports report;
	protected static ExtentTest test;
	protected Selenium I;
	
	private static String testName;
		
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext arg0) {
		log = LogManager.getLogger(Test.class.getName());
		Configuration.initialize();
		ExtentReport.setReport(arg0.getSuite().getName());
		report = ExtentReport.getReport();
	}
	
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext arg0) {
		testName = arg0.getName();
	}

	@BeforeMethod(alwaysRun = true)
	@Parameters("platform")
	public void beforeMethod(ITestResult arg0, String platform) {
		driver = Driver.getWebDriver(platform);
		initializeExtentTest(arg0);
		initializeWaits();
		I = new Selenium(log, driver, wait, test);
		initializePages(I, log, test);
		TestListener.setTest(test);
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		I.closeBrowser();
		report.endTest(test);
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		generateExtentReport();
	}
	
	private static void initializePages(Selenium I, Logger log, ExtentTest test) {
		log.debug("Initializing PouchCue Pages.");
		log.debug("Successfully initialized PouchCue Pages.");
	}
	
	private static void initializeWaits() {
		driver.manage().timeouts().implicitlyWait(Configuration.getImplicitWaitDuration(), TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, Configuration.getExplicitWaitDuration());
	}
	
	private static void initializeExtentTest(ITestResult arg0) {
		test = report.startTest(arg0.getMethod().getMethodName());
		test.assignCategory(testName);
		test.assignAuthor(Configuration.getTester());
	}
	
	private static void generateExtentReport() {
		try {
			report.flush();
			report.close();		
		} catch (NullPointerException e) {
			log.info("Encountered NullPointerException while closing ExtentReport Instance.");
		} catch (Exception e) {
			log.info("Encountered Exception while closing ExtentReport Instance.");
		}
	}
}
