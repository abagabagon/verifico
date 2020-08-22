package com.github.abagabagon.verifico;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.abagabagon.verifico.automation.mobile.AppiumMobileAutomation;
import com.github.abagabagon.verifico.automation.mobile.MobileAutomation;
import com.github.abagabagon.verifico.automation.web.AppiumWebAutomation;
import com.github.abagabagon.verifico.automation.web.SeleniumWebAutomation;
import com.github.abagabagon.verifico.automation.web.WebAutomation;
import com.github.abagabagon.verifico.data.excel.ExcelData;
import com.github.abagabagon.verifico.data.excel.XLSExcelData;
import com.github.abagabagon.verifico.data.excel.XLSXExcelData;
import com.github.abagabagon.verifico.data.sql.SQLData;
import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.enums.Excel;
import com.github.abagabagon.verifico.enums.Mobile;
import com.github.abagabagon.verifico.enums.SQL;
import com.github.abagabagon.verifico.report.ExtentReport;
import com.github.abagabagon.verifico.report.Reporter;
import com.github.abagabagon.verifico.testmanagement.TestManagement;
import com.github.abagabagon.verifico.testmanagement.TestRail;

public class Verifico {
	
	private Logger log;
	private WebAutomation webAutomation;
	private MobileAutomation mobileAutomation;
	private SQLData sqlData;
	private ExcelData excelData;
	private TestManagement testManagement;
	private Reporter report;
	
	public Verifico() {
		this.log = LogManager.getLogger(this.getClass());
	}
	
	/**
	 * Get WebAutomation instance for Web-based Application Test Automation
	 * 
	 * @param browser		Specific Web Browser to which tests will be executed.
	 * @param isHeadless	If test execution for the browser is Headless. Note that implemented browsers with headless support are <code>CHROME</code>, <code>FIREFOX</code> and <code>PHANTOMJS</code>. 
	 * 						<code>true</code> if browser is headless.
	 * 						<code>false</code> if browser is not headless.
	 * @return 				WebAutomation instance
	 */
	
	public final WebAutomation getWebAutomation(Browser browser, boolean isHeadless) {
		this.log.debug("Initializing WebAutomation instance.");
		this.webAutomation = new SeleniumWebAutomation(browser, isHeadless);
		return this.webAutomation;
	}
	
	/**
	 * Get WebAutomation instance for Web-based Mobile Application Test Automation
	 * 
	 * @param deviceName		Name of the Device to which tests will be executed.
	 * @param mobile			Specific Mobile Platform to which tests will be executed.
	 * @param platformVersion	Version of the Mobile Platform.
	 * @param browser			Specific Web Browser Platform to which tests will be executed.
	 * @param appiumServerUrl	URL of the running Appium Server.
	 * @return 					WebAutomation instance
	 */
	
	public final WebAutomation getWebAutomation(String deviceName, Mobile mobile, String platformVersion, Browser browser, URL appiumServerUrl) {
		this.log.debug("Initializing WebAutomation instance.");
		this.webAutomation = new AppiumWebAutomation(deviceName, mobile, platformVersion, browser, appiumServerUrl);
		return this.webAutomation;
	}
	
	/**
	 * Get MobileAutomation instance for Mobile Application Test Automation
	 * 
	 * @param deviceName		Name of the Device to which tests will be executed.
	 * @param mobile			Specific Mobile Platform to which tests will be executed.
	 * @param platformVersion	Version of the Mobile Platform.
	 * @param applicationFile	Application File of the Mobile Application to be tested.
	 * @param appiumServerUrl	URL of the running Appium Server.
	 * @return					MobileAutomation instance
	 */
	
	public final MobileAutomation getMobileAutomation(String deviceName, Mobile mobile, String platformVersion, File applicationFile, URL appiumServerUrl) {
		this.log.debug("Initializing MobileAutomation instance.");
		this.mobileAutomation = new AppiumMobileAutomation(deviceName, mobile, platformVersion, applicationFile, appiumServerUrl);
		return this.mobileAutomation;
	}
	
	/**
	 * Get SQLData instance for Test Automation
	 * 
	 * @param sqlType	SQL Database Type
	 * @param dbServer	SQL Database Server Host Name or IP Address
	 * @param dbName	SQL Database Name
	 * @param user		User Name for SQL Database Access
	 * @param password	Password of User Name for SQL Database Access
	 * @return			SQLData instance
	 */
	
	public final SQLData getSQLData(SQL sqlType, String dbServer, String dbName, String user, String password) {
		this.log.debug("Initializing SQLData instance.");
		this.sqlData = new SQLData(sqlType, dbServer, dbName, user, password);
		return this.sqlData;
	}
	
	/**
	 * Get ExcelData instance for Test Automation
	 * 
	 * @param excelType	Excel File Type
	 * @param filePath	File Path where the Excel File is located 
	 * @return			ExcelData instance
	 */
	
	public final ExcelData getExcelData(Excel excelType, String filePath) {
		this.log.debug("Initializing ExcelData instance.");
		switch(excelType) {
		case XLS:
			this.excelData = new XLSExcelData(filePath);
			break;
		case XLSX:
			this.excelData = new XLSXExcelData(filePath);
			break;
		}
		
		return this.excelData;
	}
	
	/**
	 * Get Test Management Tool instance for Test Automation
	 * 
	 * @param testRailServer	TestRail Server (e. g. "companyname.testrail.io")
	 * @param testRailUser		E-Mail of an Active TestRail User 
	 * @param testRailPassword	Password of the Active TestRail User
	 * @param testRunId			TestRail Test Run ID
	 * @return					TestManagement Instance
	 */
	
	public final TestManagement getTestManagement(String testRailServer, String testRailUser, String testRailPassword, int testRunId) {
		this.log.debug("Initializing Test Management Tool instance.");
		this.testManagement = new TestRail(testRailServer, testRailUser, testRailPassword, testRunId);
		return this.testManagement;
	}
	
	/**
	 * Get Reporter instance for Test Automation
	 * 
	 * @return Reporter Instance
	 */
	
	public final Reporter getReporter() {
		this.log.debug("Initializing Reporter instance.");
		this.report = new ExtentReport();
		return this.report;
	}

}
