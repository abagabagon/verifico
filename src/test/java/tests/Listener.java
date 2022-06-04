
package tests;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class Listener extends Tests implements ITestListener{

	private static ArrayList<String> passedTests;
	private static ArrayList<String> failedTests;
	private static ArrayList<String> skippedTests;

	@Override
	public void onStart(ITestContext arg0) {
		String testName = arg0.getName();
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
		String testCaseId = arg0.getMethod().getMethodName();
		String testCaseDescription = arg0.getMethod().getDescription();
		report.setTest(testCaseId, testCaseDescription);
		report.setCategory(arg0.getTestContext().getName());
		log.info("------------------------------------------------------------------------");
		log.info(testCaseId + " TEST START");
		log.info(testCaseDescription);
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
		String testCaseId = arg0.getMethod().getMethodName();
		report.pass(testCaseId);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseId + " PASSED");
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		String testCaseId = arg0.getMethod().getMethodName();
		report.fail(testCaseId);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseId + " FAILED");
		log.debug(ExceptionUtils.getStackTrace(arg0.getThrowable()));
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		String testCaseId = arg0.getMethod().getMethodName();
		report.skip(testCaseId);
		log.info("------------------------------------------------------------------------");
		log.info(testCaseId + " SKIPPED");
		log.debug(ExceptionUtils.getStackTrace(arg0.getThrowable()));
		log.info("------------------------------------------------------------------------");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {

	}

	@Override
	public void onFinish(ITestContext arg0) {
		String testName = arg0.getName();
		log.info("------------------------------------------------------------------------");
		log.info(testName.toUpperCase() + " END");
		log.info("------------------------------------------------------------------------");
		reportPassedTestCases(arg0);
		log.info("------------------------------------------------------------------------");
		reportFailedTestCases(arg0);
		log.info("------------------------------------------------------------------------");
		reportSkippedTestCases(arg0);
		log.info("------------------------------------------------------------------------");
		log.info("SUMMARY");
		log.info("  Executed : " + arg0.getAllTestMethods().length);
		log.info("  Passed   : " + arg0.getPassedTests().size());
		log.info("  Failed   : " + arg0.getFailedTests().size());
		log.info("  Skipped  : " + arg0.getSkippedTests().size());
		log.info("------------------------------------------------------------------------\n");
	}

	private static final void listIncludedTestCases(ITestContext arg0) {
		ITestNGMethod includedMethods[] = arg0.getAllTestMethods();
		if (includedMethods.length > 0) {
			log.info("The following Test Cases will be executed:");
			int count = 1;
			for (ITestNGMethod method : includedMethods) {
				if (method.getMethodName().toString().contains("before") || method.getMethodName().toString().contains("after")) {
					continue;
				}
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.warn("No Test Cases will be executed! Please check TestNG Configuration file for this Test.");
		}
	}

	private static final void listExcludedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> excludedMethods = arg0.getExcludedMethods();
		if (excludedMethods.size() > 0) {
			log.info("The following Test Cases will not be executed:");
			int count = 1;
			for (ITestNGMethod method : excludedMethods) {
				if (method.getMethodName().toString().contains("before") || method.getMethodName().toString().contains("after")) {
					continue;
				}
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.info("All Test Cases for this Test will be executed!");
		}
	}

	private static final void reportPassedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> passedMethods = arg0.getPassedTests().getAllMethods();
		passedTests = new ArrayList<String>();
		if (passedMethods.size() > 0) {
			log.info("The following Test Cases have PASSED:");
			int count = 1;
			for (ITestNGMethod method : passedMethods) {
				if (method.getMethodName().toString().contains("before") || method.getMethodName().toString().contains("after")) {
					continue;
				}
				passedTests.add(method.getMethodName().toString());
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.info("No Test Cases PASSED!");
		}
	}

	private static final void reportFailedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> failedMethods = arg0.getFailedTests().getAllMethods();
		failedTests = new ArrayList<String>();
		if (failedMethods.size() > 0) {
			log.info("The following Test Cases have FAILED:");
			int count = 1;
			for (ITestNGMethod method : failedMethods) {
				if (method.getMethodName().toString().contains("before") || method.getMethodName().toString().contains("after")) {
					continue;
				}
				failedTests.add(method.getMethodName().toString());
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.info("No Test Cases FAILED!");
		}
	}

	private static final void reportSkippedTestCases(ITestContext arg0) {
		Collection<ITestNGMethod> skippedMethods = arg0.getSkippedTests().getAllMethods();
		skippedTests = new ArrayList<String>();
		if (skippedMethods.size() > 0) {
			log.info("The following Test Cases are SKIPPED:");
			int count = 1;
			for (ITestNGMethod method : skippedMethods) {
				if (method.getMethodName().toString().contains("before") || method.getMethodName().toString().contains("after")) {
					continue;
				}
				skippedTests.add(method.getMethodName().toString());
				log.info(count + ". " + method.getMethodName());
				count++;
			}
		} else {
			log.info("No Test Cases SKIPPED!");
		}
	}

}