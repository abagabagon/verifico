package com.github.abagabagon.verifico.automation.mobile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppiumMobileDriver {
	
	private Logger log;
	private AppiumDriver driver;
	private URL appiumServerUrl;
	
	/**
	 * Mobile Automation Driver using Appium
	 * 
	 * @param appiumServerUrl	Appium Server URL
	 */
	
	public AppiumMobileDriver(URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumServerUrl = appiumServerUrl;
	}
	
	/**
	 * Initializes and returns IOSDriver Object.
	 * 
	 * @param platformVersion	Version of the Mobile Platform.
	 * @param deviceName		Name of the Device to which tests will be executed.
	 * @return					IOSDriver Object
	 */

	AppiumDriver getIOSDriver(String platformVersion, String deviceName) {
		this.log.trace("Initializing IOSDriver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.IOS);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		this.driver = new IOSDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized IOSDriver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AndroidDriver Object.
	 * 
	 * @param platformVersion	Version of the Mobile Platform.
	 * @param deviceName		Name of the Device to which tests will be executed.
	 * @param applicationFile	Application File of the Mobile Application to be tested.
	 * @return AndroidDriver Object
	 */
	
	AppiumDriver getAndroidDriver(String platformVersion, String deviceName, File applicationFile) {
		this.log.trace("Initializing AndroidDriver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.APP, applicationFile.getAbsolutePath());
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, "true");		
		capabilities.setCapability(MobileCapabilityType.NO_RESET, "false");
		capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
		this.driver = new AndroidDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized AndroidDriver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AndroidDriver Object.
	 * 
	 * @param platformVersion	Version of the Mobile Platform.
	 * @param deviceName		Name of the Device to which tests will be executed.
	 * @param applicationUrl	Application URL of the Mobile Application to be tested.
	 * @return AndroidDriver Object
	 */
	
	AppiumDriver getAndroidDriver(String platformVersion, String deviceName, URL applicationUrl, File applicationFile) {
		this.log.trace("Initializing AndroidDriver.");
		try {
			FileUtils.copyURLToFile(applicationUrl, applicationFile);
		} catch (IOException e) {
			this.log.fatal("Encountered IOException while trying to get Android Driver.");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get Android Driver.");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
		}
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.APP, applicationFile.getAbsolutePath());
		capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
		this.driver = new AndroidDriver(this.appiumServerUrl, capabilities);
		this.log.trace("Successfully initialized AndroidDriver.");
		return this.driver;
	}

}
