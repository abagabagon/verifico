
package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.github.abagabagon.verifico.report.ExtentReport;
import com.github.abagabagon.verifico.report.Reporter;

public class Tests implements ITestListener {

	protected static Logger log;
	protected static Reporter report;
	protected static SoftAssert softAssert;
	protected static final String URL = "https://courses.letskodeit.com/practice";

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