package com.github.abagabagon.verifico.testmanagement;

import java.io.IOException;
import java.util.HashMap;
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
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("status_id", new Integer(1));
		JSONObject response = this.addResultForCase(testCaseId, data);
		this.log.trace(response);
	}

	@Override
	public void setTestAsFailed(String testCaseId) {
		this.log.debug("Setting status as \"FAILED\" at TestRail Test Management Tool.");
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("status_id", new Integer(5));
		JSONObject response = this.addResultForCase(testCaseId, data);
		this.log.trace(response);
	}

	@Override
	public void setTestAsSkipped(String testCaseId) {
		this.log.debug("Setting status as \"SKIPPED\" at TestRail Test Management Tool.");
		Map<String, Integer> data = new HashMap<String, Integer>();
		data.put("status_id", new Integer(4));
		JSONObject response = this.addResultForCase(testCaseId, data);
		this.log.trace(response);
	}
	
	private String checkTestCaseId(String testCaseId) {
		String id = null;
		
		if(testCaseId.startsWith("C")) {
			id = testCaseId.substring(1);
		} else {
			id = testCaseId;
		}
		
		return id;
	}
	
	private JSONObject addResultForCase(String testCaseId, Map<String, Integer> data) {
		JSONObject response = null;
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

}
