package com.github.abagabagon.verifico.automation.web.appium;

import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.enums.Mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * Appium implemented Web Driver Commands
 * 
 * @author albagabagon
 *
 */

public class AppiumWebDriver {
	
	private Logger log;
	private WebDriver driver;
	private URL appiumServerUrl;
	
	public AppiumWebDriver(URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumServerUrl = appiumServerUrl;
	}
	
	WebDriver getWebDriver(Mobile mobile, Browser browser, String platformVersion, String deviceName) {
		this.log.trace("Initializing Appium Web Driver.");
		try {
			switch (browser) {
			case CHROME:
				this.driver = this.getChromeDriver(mobile, platformVersion, deviceName);
				break;
			case SAFARI:
				this.driver = this.getSafariDriver(mobile, platformVersion, deviceName);
				break;
			default:
				this.log.fatal("Unsupported Web Browser.");
				System.exit(1);
			}
		} catch (WebDriverException e) {
			this.log.fatal("Encountered WebDriverException while initializing Appium Web Driver.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			System.exit(1);
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing Appium Web Driver.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			System.exit(1);
		}
		return this.driver;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Chrome.
	 * 
	 * @return WebDriver Object
	 */
	
	private WebDriver getChromeDriver(Mobile mobile, String platformVersion, String deviceName) {
		this.log.trace("Initializing Google Chrome Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, mobile.toString());
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
		this.driver = new AppiumDriver<WebElement>(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Google Chrome Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Safari.
	 * 
	 * @return WebDriver Object
	 */

	private WebDriver getSafariDriver(Mobile mobile, String platformVersion, String deviceName) {
		this.log.trace("Setting Property of Safari Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, mobile.toString());
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Safari");
		this.driver = new AppiumDriver<WebElement>(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Safari Driver.");
		return this.driver;
	}

}
