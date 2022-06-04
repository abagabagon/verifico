package com.github.abagabagon.verifico.report;

/**
 * Interface for Test Automation Reporting
 *
 * @author albagabagon
 *
 */

public interface Reporter {

	/**
	 * Initializes Report instance.
	 *
	 * @param testSuiteName Test Suite Name
	 * @param organization  Organization Name
	 */

	public void setReport(String testSuiteName, String organization);

	/**
	 * Initializes Test instance.
	 *
	 * @param testName    Name of Test Case being executed.
	 * @param description Description of the current Test Case
	 */

	public void setTest(String testName, String description);

	/**
	 * Sets the Author of the current Test being executed.
	 *
	 * @param author Name of the Tester executing current Test Case.
	 */

	public void setAuthor(String author);

	/**
	 * Sets the Category of the current Test being executed.
	 *
	 * @param category Name of the Category of current Test Case.
	 */

	public void setCategory(String category);

	/**
	 * Sets the Device for the test.
	 *
	 */

	public void setDevice();

	/**
	 * Logs test event as INFO
	 *
	 * @param details Details of the Test Event.
	 */

	public void info(String details) ;

	/**
	 * Logs test event as PASSED
	 *
	 * @param testCaseName Name of the Test Case that PASSED
	 */

	public void pass(String testCaseName);

	/**
	 * Logs test event as FAILED
	 *
	 * @param testCaseName Name of the Test Case that FAILED
	 */

	public void fail(String testCaseName);

	/**
	 * Logs test event as SKIPPED
	 *
	 * @param testCaseName Name of the Test Case that was SKIPPED
	 */

	public void skip(String testCaseName);

	/**
	 * Generates the Report File.
	 *
	 */

	public void generateReport();

}
