
package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.github.abagabagon.verifico.automation.web.AlertCommands;
import com.github.abagabagon.verifico.automation.web.BrowserCommands;
import com.github.abagabagon.verifico.automation.web.KeyboardCommands;
import com.github.abagabagon.verifico.automation.web.MouseCommands;
import com.github.abagabagon.verifico.automation.web.StateAssertions;
import com.github.abagabagon.verifico.automation.web.ValueAssertions;
import com.github.abagabagon.verifico.automation.web.WaitCommands;
import com.github.abagabagon.verifico.report.ExtentReport;
import com.github.abagabagon.verifico.report.Reporter;

public class Tests implements ITestListener {

	protected static Logger log;
	protected static Reporter report;
	protected static SoftAssert softAssert;

	protected WebDriver driver;
	protected WaitCommands wait;
	protected BrowserFactory browserFactory;
	protected BrowserCommands browser;
	protected AlertCommands alert;
	protected MouseCommands mouse;
	protected KeyboardCommands keyboard;
	protected ValueAssertions value;
	protected StateAssertions state;

	static {
		log = LogManager.getLogger(Tests.class);
	}

	@BeforeSuite(alwaysRun = true)
	public void setupReporter(ITestContext arg0) {
		String organization = "Verifico";
		String testName = arg0.getSuite().getName();
		report = new ExtentReport();
		report.setReport(testName, organization);
	}

	@AfterSuite(alwaysRun = true)
	public void generateReport(ITestContext arg0) {
		report.generateReport();
	}

	@BeforeMethod
	protected void setupSoftAssert() {
		softAssert = new SoftAssert();
	}

	@AfterMethod
	protected void teardownSoftAssert() {
		softAssert = null;
	}

}