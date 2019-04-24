package reporters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import selenium.Utilities;

public class ExtentReport {
	
	private static ExtentReports report;
	private static ExtentTest test;
	
	/**
	 * Initializes ExtentReports instance.
	 * 
	 * @param  testSuiteName Name of the Test Suite
	 */
	
	public static final void setReport(String testSuiteName) {
		String filePath = "./reports/" + testSuiteName + " " + Utilities.getCurrentDate("yyyyMMddHHmmss") + ".html";
		report = new ExtentReports(filePath, true);
	}

	/**
	 * Initializes ExtentTest instance.
	 * 
	 * @param testCaseName Name of Test Case being executed.
	 * @param author       Name of the Tester executing current Test Case.
	 * @param category     Name of the Category current Test Case is under.
	 * @param description  Description of the current Test Case 
	 */
	
	public static final void setTest(String testCaseName, String author, String category, String description) {
		test = report.startTest(testCaseName);
		test.assignAuthor(author);
		test.assignCategory(category);
		test.setDescription(description);
	}
	
	/**
	 * Gets an instance of ExtentReports
	 * 
	 * @return Instance of ExtentReports
	 */
	
	public static final ExtentReports getReport() {
		return report;
	}
	
	/**
	 * Gets an instance of ExtentTest
	 * 
	 * @return Instance of ExtentTest
	 */
	
	public static final ExtentTest getTest() {
		return test;
	}
}
