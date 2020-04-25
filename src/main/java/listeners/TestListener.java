/*******************************************************************************
 * @name    TestListener
 * @author  Arnel L. Bagabagon Jr.
 * @version 0.1
 * @since   02/06/2019
 * 
 * This class implements the ITestListener Interface which monitors Test
 * Execution events (Test Start, Pass, Fail, etc.). This class provides
 * structured logs from start to end of Test Execution. ExtentReports is
 * also implemented in this class.
 * 
 ******************************************************************************/

package listeners;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import reporters.ExtentReport;

public class TestListener implements ITestListener {

	private String testName;
	private String testCaseName;
	private String testCaseDescription;
	private Logger log;
	
	public TestListener() {
		this.log = LogManager.getLogger(this.getClass());
		this.log.debug("Initializing TestListener Class.");
		this.log.debug("Successfully initialized TestListener Class.");
	}

	@Override
	public void onStart(ITestContext arg0) {
		this.testName = arg0.getName();
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testName.toUpperCase() + " START");
		this.log.info("------------------------------------------------------------------------");
		listIncludedTestCases(arg0);
		this.log.info("------------------------------------------------------------------------");
		listExcludedTestCases(arg0);
		this.log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		this.testCaseName = arg0.getMethod().getMethodName();
		this.testCaseDescription = arg0.getMethod().getDescription();
		ExtentReport.setTest(this.testCaseName, this.testCaseDescription);
		ExtentReport.setCategory(this.testName);
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testCaseName + " TEST START");
		this.log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
        String details = "Test Case: \"" + this.testCaseName + "\" has " + "<b>" + "PASSED" + "</b>";
        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.GREEN);
		ExtentReport.logPassed(markUp);
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testCaseName + " PASSED");
		this.log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
        String details = "Test Case: \"" + this.testCaseName + "\" has " + "<b>" + "FAILED" + "</b>";
        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.RED);
		ExtentReport.logFailed(markUp);
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testCaseName + " FAILED");
		this.log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
        String details = "Test Case: \"" + this.testCaseName + "\" has " + "<b>" + "SKIPPED" + "</b>";
        Markup markUp = MarkupHelper.createLabel(details, ExtentColor.YELLOW);
		ExtentReport.logSkipped(markUp);
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testCaseName + " SKIPPED");
		this.log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {

	}

	@Override
	public void onFinish(ITestContext arg0) {
		this.log.info("------------------------------------------------------------------------");
		this.log.info(this.testName.toUpperCase() + " END");
		this.log.info("------------------------------------------------------------------------");
		this.log.info("SUMMARY");
		this.log.info("  Executed : " + arg0.getAllTestMethods().length);
		this.log.info("  Passed   : " + arg0.getPassedTests().size());
		this.log.info("  Failed   : " + arg0.getFailedTests().size());
		this.log.info("  Skipped  : " + arg0.getSkippedTests().size());
		this.log.info("------------------------------------------------------------------------\n");
	}
	
	/**
	 * List all Test Cases that will be executed.
	 * 
	 * @param arg0 ITestContext object.
	 */
	
	private final void listIncludedTestCases(ITestContext arg0) {
		ITestNGMethod includedMethods[] = arg0.getAllTestMethods();
		if (includedMethods.length > 0) {
			this.log.info("The following Test Cases will be executed:");
			int count = 1;
			for (ITestNGMethod method : includedMethods) {
				if (method.getMethodName().toString().contains("setUp")) {
					continue;
				}
				this.log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			this.log.warn("No Test Cases will be executed! Please check TestNG Configuration file for this Test.");
		}
	}
	
	/**
	 * List all Test Cases that will not be executed.
	 * 
	 * @param arg0 ITestContext object.
	 */

	private final void listExcludedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> excludedMethods = arg0.getExcludedMethods();
		if (excludedMethods.size() > 0) {
			this.log.info("The following Test Cases will not be executed:");
			int count = 1;
			for (ITestNGMethod method : excludedMethods) {
				if (method.getMethodName().toString().contains("setUp")) {
					continue;
				}
				this.log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			this.log.info("All Test Cases for this Test will be executed!");
		}
	}
}
