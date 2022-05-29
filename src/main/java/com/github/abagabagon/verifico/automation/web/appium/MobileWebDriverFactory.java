package com.github.abagabagon.verifico.automation.web.appium;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class MobileWebDriverFactory {
	
	private Logger log;
	public WebDriver driver;
	private URL appiumServerUrl;
	
	public MobileWebDriverFactory(URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumServerUrl = appiumServerUrl;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Google Chrome.
	 * 
	 * @return Google Chrome WebDriver Object
	 */
	
	public final WebDriver getChromeDriver(Platform platform, String platformVersion, String deviceName) {
		this.log.trace("Initializing Google Chrome Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, platform);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, Browser.CHROME);
		this.driver = new AppiumDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Google Chrome Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Mozilla Firefox.
	 * 
	 * @return Mozilla Firefox WebDriver Object
	 */
	
	public final WebDriver getFirefoxDriver(Platform platform, String platformVersion, String deviceName) {
		this.log.trace("Initializing Mozilla Firefox Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, platform);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, Browser.FIREFOX);
		this.driver = new AppiumDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Mozilla Firefox Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Safari.
	 * 
	 * @return Safari WebDriver Object
	 */

	public final WebDriver getSafariDriver(Platform platform, String platformVersion, String deviceName) {
		this.log.trace("Setting Property of Safari Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, platform);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, Browser.SAFARI);
		this.driver = new AppiumDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Safari Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AppiumDriver Object configured for Microsoft Edge.
	 * 
	 * @return Microsoft Edge WebDriver Object
	 */

	public final WebDriver getEdgeDriver(Platform platform, String platformVersion, String deviceName) {
		this.log.trace("Setting Property of Edge Driver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, platform);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, Browser.EDGE);
		this.driver = new AppiumDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized Microsoft Edge Driver.");
		return this.driver;
	}

}
