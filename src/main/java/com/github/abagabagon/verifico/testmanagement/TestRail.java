package com.github.abagabagon.verifico.testmanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class TestRail implements TestManagement {
	
	private Logger log;
	private APIClient testRail;
	private int runId;
	
	public TestRail(String testRailServer, String user, String password, int runId) {
		this.log = LogManager.getLogger(this.getClass());
		this.testRail = new APIClient("https://" + testRailServer);
		this.testRail.setUser(user);
		this.testRail.setPassword(password);
		this.runId = runId;
	}
	
	@Override
	public void setTestAsPassed(String testCaseId) {
		this.log.debug("Setting status as \"PASSED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultForCase(testCaseId, 1);
		this.log.trace(response);
	}

	@Override
	public void setTestAsFailed(String testCaseId) {
		this.log.debug("Setting status as \"FAILED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultForCase(testCaseId, 2);
		this.log.trace(response);
	}

	@Override
	public void setTestAsSkipped(String testCaseId) {
		this.log.debug("Setting status as \"SKIPPED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultForCase(testCaseId, 4);
		this.log.trace(response);
	}
	
	@Override
	public void setTestsAsPassed(String[] testCaseIds) {
		this.log.debug("Setting status as \"PASSED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultsForCases(testCaseIds, 1);
		this.log.trace(response);
	}

	@Override
	public void setTestsAsFailed(String[] testCaseIds) {
		this.log.debug("Setting status as \"FAILED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultsForCases(testCaseIds, 2);
		this.log.trace(response);
	}

	@Override
	public void setTestsAsSkipped(String[] testCaseIds) {
		this.log.debug("Setting status as \"SKIPPED\" at TestRail Test Management Tool.");
		JSONObject response = this.addResultsForCases(testCaseIds, 4);
		this.log.trace(response);
	}
	
	/**
	 * Adds result of a Test Case at Test Rail
	 * 
	 * @param testCaseId	Test Case ID to add result to 
	 * @param statusId		Status ID of result
	 * @return				JSONObject response
	 */
	
	private JSONObject addResultForCase(String testCaseId, int statusId) {
		JSONObject response = null;
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("status_id", new Integer(statusId));
		String id = this.checkTestCaseId(testCaseId);

		try {
			response = (JSONObject) this.testRail.sendPost("add_result_for_case/" + this.runId + "/" + id, data);
		} catch (IOException e) {
			this.log.error("Encountered IOException while adding TestRail Test Result.");
		} catch (APIException e) {
			this.log.error("Encountered APIException while adding TestRail Test Result.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while adding TestRail Test Result.");
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Adds result of multiple Test Cases at Test Rail
	 * 
	 * @param testCaseIds	Test Case IDs to add result to
	 * @param statusId		Status ID of result
	 * @return				JSONObject response
	 */
	
	private JSONObject addResultsForCases(String[] testCaseIds, int statusId) {
		JSONObject response = null;
		Map<String, List<Map<String, Integer>>> data = new HashMap<String, List<Map<String, Integer>>>();
		List<Map<String, Integer>> testCases = new ArrayList<Map<String, Integer>>();
		int size = testCaseIds.length;
		data.put("results", testCases);
		
		for (int testCount = 0; testCount < size; testCount++) {
			Map<String, Integer> testCase = new HashMap<String, Integer>();
			String id = this.checkTestCaseId(testCaseIds[testCount]);
			testCase.put("case_id", Integer.parseInt(id));
			testCase.put("status_id", new Integer(statusId));
			testCases.add(testCase);
		}
		
		try {
			response = (JSONObject) this.testRail.sendPost("add_results_for_cases/" + this.runId, data);
		} catch (IOException e) {
			this.log.error("Encountered IOException while adding TestRail Test Result.");
		} catch (APIException e) {
			this.log.error("Encountered APIException while adding TestRail Test Result.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while adding TestRail Test Result.");
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Checks value of Test Case ID (Should only contain numeric values)
	 * 
	 * @param testCaseId	Test Case ID Value to check
	 * @return				Test Case ID Value that only contains numeric value
	 */
	
	private String checkTestCaseId(String testCaseId) {
		String id = null;
		
		if(testCaseId.startsWith("C")) {
			id = testCaseId.substring(1);
		} else {
			id = testCaseId;
		}
		
		return id;
	}

}
