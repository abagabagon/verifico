package automation.mobile;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import enums.Mobile;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppiumMobileDriver {
	
	private Logger log;
	private AppiumDriver<MobileElement> driver;
	private URL appiumServerUrl;
	
	public AppiumMobileDriver(URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumServerUrl = appiumServerUrl;
	}
	
	/**
	 * Initializes and returns IOSDriver Object.
	 * 
	 * @param platformVersion
	 * @param deviceName
	 * @param applicationFile
	 * @return
	 */

	AppiumDriver<MobileElement> getIOSDriver(String platformVersion, String deviceName) {
		this.log.debug("Initializing IOSDriver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, Mobile.iOS);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		this.driver = new IOSDriver<MobileElement>(this.appiumServerUrl, capabilities);
		this.log.debug("Successfully initialized IOSDriver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns AndroidDriver Object.
	 * 
	 * @param platformVersion
	 * @param deviceName
	 * @param applicationFile
	 * @return
	 */
	
	AppiumDriver<MobileElement> getAndroidDriver(String platformVersion, String deviceName, File applicationFile) {
		this.log.debug("Initializing AndroidDriver.");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.PLATFORM_NAME, Mobile.Android);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.APP, applicationFile.getAbsolutePath());
		this.driver = new AndroidDriver<MobileElement>(this.appiumServerUrl, capabilities);
		this.log.debug("Successfully initialized AndroidDriver.");
		return this.driver;
	}
	


}
