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

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestListener implements ITestListener {

	private static ExtentTest test;
	private static String     testName;
	private static String     testCaseName;
	private static Logger     log;

	public static final void setTest(ExtentTest test) {
		TestListener.test = test;
	}
	
	@Override
	public void onStart(ITestContext arg0) {
		testName = arg0.getName();
		log = LogManager.getLogger(testName);
		log.info("------------------------------------------------------------------------");
		log.info(testName.toUpperCase() + " START");
		log.info("------------------------------------------------------------------------");
		listIncludedTestCases(arg0);
		log.info("------------------------------------------------------------------------");
		listExcludedTestCases(arg0);
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestStart(ITestResult arg0) {
		testCaseName = arg0.getMethod().getMethodName();
		log.info("------------------------------------------------------------------------");
		log.info(testCaseName + " TEST START");
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		test.log(LogStatus.PASS, testCaseName);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseName + " PASSED");
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		test.log(LogStatus.ERROR, arg0.getThrowable());
		test.log(LogStatus.FAIL, testCaseName);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseName + " FAILED");
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		test.log(LogStatus.ERROR, arg0.getThrowable());
		test.log(LogStatus.SKIP, testCaseName);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseName + " SKIPPED");
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {

	}

	@Override
	public void onFinish(ITestContext arg0) {
		log.info("------------------------------------------------------------------------");
		log.info(testName.toUpperCase() + " END");
		log.info("------------------------------------------------------------------------");
		log.info("SUMMARY");
		log.info("  Executed : " + arg0.getAllTestMethods().length);
		log.info("  Passed   : " + arg0.getPassedTests().size());
		log.info("  Failed   : " + arg0.getFailedTests().size());
		log.info("  Skipped  : " + arg0.getSkippedTests().size());
		log.info("------------------------------------------------------------------------\n");
	}
	
	/**
	 * List all Test Cases that will be executed.
	 * 
	 * @param arg0 ITestContext object.
	 */
	
	private static final void listIncludedTestCases(ITestContext arg0) {
		ITestNGMethod includedMethods[] = arg0.getAllTestMethods();
		if (includedMethods.length > 0) {
			log.info("The following Test Cases will be executed:");
			int count = 1;
			for (ITestNGMethod method : includedMethods) {
				if (method.getMethodName().toString().contains("setUp")) {
					continue;
				}
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.warn("No Test Cases will be executed! Please check TestNG Configuration file for this Test.");
		}
	}
	
	/**
	 * List all Test Cases that will not be executed.
	 * 
	 * @param arg0 ITestContext object.
	 */

	private static final void listExcludedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> excludedMethods = arg0.getExcludedMethods();
		if (excludedMethods.size() > 0) {
			log.info("The following Test Cases will not be executed:");
			int count = 1;
			for (ITestNGMethod method : excludedMethods) {
				if (method.getMethodName().toString().contains("setUp")) {
					continue;
				}
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.info("All Test Cases for this Test will be executed!");
		}
	}
}
