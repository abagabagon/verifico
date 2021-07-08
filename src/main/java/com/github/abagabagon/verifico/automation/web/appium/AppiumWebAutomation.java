package com.github.abagabagon.verifico.automation.web.appium;

import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;

import com.github.abagabagon.verifico.automation.web.selenium.SeleniumWebAutomation;
import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.enums.Mobile;

/**
 * Appium implemented Web Automation Commands
 * 
 * @author albagabagon
 *
 */

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
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		deleteAllCookies();
	}

}