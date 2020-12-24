package com.github.abagabagon.verifico.automation.web;

import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.testng.Assert;

import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.enums.Mobile;
import com.github.abagabagon.verifico.enums.TestStatus;

public class AppiumWebAutomation extends SeleniumWebAutomation {

	private AppiumWebDriver appiumWebDriver;
	private Mobile mobile;
	private Browser browser;
	private String platformVersion;
	private String deviceName;
	
	public AppiumWebAutomation(String deviceName, Mobile mobile, String platformVersion, Browser browser, URL appiumServerUrl) {
		super(browser);
		this.log = LogManager.getLogger(this.getClass());
		this.appiumWebDriver = new AppiumWebDriver(appiumServerUrl);
		this.seleniumWait = new SeleniumWait(this.wait);
		this.browser = browser;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
	}

	/* ####################################################### */
	/*                     BROWSER ACTIONS                     */
	/* ####################################################### */

	@Override
	public void openBrowser() {
		this.log.trace("Initializing Appium Web Driver.");
		this.log.debug("I open Web Browser.");
		this.driver = this.appiumWebDriver.getWebDriver(this.mobile, this.browser, this.platformVersion, this.deviceName);
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		this.driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30));
		this.initializeImplicitWait(20);
		this.initializeExplicitWait(20);
		deleteAllCookies();
	}
	
	@Override
	public TestStatus verifyUrl(String expectedUrl) {
		this.log.debug("I verify Page URL: \"" + expectedUrl + "\".");
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.contains(expectedUrl);
		TestStatus status = TestStatus.FAILED;
		if(isUrlEqual) {
			status = TestStatus.PASSED;
			this.log.debug("I see Page URL: \"" + expectedUrl + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see Page URL: \"" + expectedUrl + "\". Actual URL is \"" + actualUrl + "\".");
			Assert.fail();
		}
		return status;
	}
	
	@Override
	public TestStatus verifyTitle(String expectedTitle) {
		this.log.debug("I verify Page Title: \"" + expectedTitle + "\".");
		String actualTitle = this.driver.getTitle().trim();
		boolean isUrlEqual = actualTitle.contains(expectedTitle);
		TestStatus status = TestStatus.FAILED;
		if(isUrlEqual) {
			status = TestStatus.PASSED;
			this.log.debug("I saw Page Title: \"" + expectedTitle + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see Page Title: \"" + expectedTitle + "\". Actual Title is \"" + actualTitle + "\".");
			Assert.fail();
		}
		return status;
	}

}