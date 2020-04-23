package test;

import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import automation.Automation;
import automation.web.WebAutomation;
import enums.Browser;
import enums.Environment;
import pages.Pages;
import reporters.extentreport.ExtentReport;
import settings.Settings;

public class Tests {
	
	static WebAutomation I;
	Automation automation;
	Settings settings;	
	Pages pages;
	String testSuiteName;
	Logger log;
	
	@BeforeSuite(alwaysRun = true)
	@Parameters("environment")
	public void beforeSuite(Environment environment, ITestContext arg0) {
		this.settings = new Settings(environment);
		this.automation = new Automation();
		I = this.automation.getWebAutomation(Settings.getAutomationTool());
		this.testSuiteName = arg0.getSuite().getName();
		ExtentReport.setReport(this.testSuiteName);
	}

	@BeforeTest
	@Parameters("platform")
	public void beforeTest(Browser browser) {
		I.openBrowser(browser);
		this.pages = new Pages(I);
		this.pages.initializePages();
	}

	@AfterTest
	public static void afterTest() {
		I.closeBrowser();
	}
	
	@AfterSuite
	public static void afterSuite() {
		ExtentReport.generateExtentReport();
	}
}