package com.github.abagabagon.verifico.testmanagement;

public interface TestManagement {
	
	/**
	 * Sets the result of a Test Case as PASSED.
	 * 
	 * @param testCaseId ID of the Test Case that PASSED
	 */
	
	public void setTestAsPassed(String testCaseId);
	
	/**
	 * Sets the result of a Test Case as FAILED.
	 * 
	 * @param testCaseId ID of the Test Case that FAILED
	 */
	
	public void setTestAsFailed(String testCaseId);
	
	/**
	 * Sets the result of a Test Case as SKIPPED.
	 * 
	 * @param testCaseId ID of the Test Case that was SKIPPED
	 */
	
	public void setTestAsSkipped(String testCaseId);

}
