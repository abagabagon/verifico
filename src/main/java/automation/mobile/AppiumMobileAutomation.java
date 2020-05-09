package automation.mobile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.Mobile;
import enums.TestStatus;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;

public class AppiumMobileAutomation implements MobileAutomation {
	
	AppiumDriver<MobileElement> driver;
	TouchAction<?> touchAction;
	Logger log;
	WebDriverWait wait;
	Select select;
	Alert alert;
	Actions action;
	JavascriptExecutor javascriptExecutor;
	ArrayList<String> tabs;
	File applicationFile;
	long implicitWaitDuration;
	long explicitWaitDuration;
	boolean testNgEnabled;
	
	private AppiumMobileDriver appiumMobileDriver;
	private Mobile mobile;
	private String platformVersion;
	private String deviceName;
	
	public AppiumMobileAutomation(Mobile mobile, URL appiumServerUrl, String platformVersion, String deviceName, File applicationFile) {
		this.log = LogManager.getLogger(this.getClass());
		this.log.debug("Initializing AppiumWebAutomation Class.");
		this.appiumMobileDriver = new AppiumMobileDriver(appiumServerUrl);
		this.implicitWaitDuration = 20;
		this.testNgEnabled = false;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.applicationFile = applicationFile;
		this.log.debug("Successfully initialized AppiumWebAutomation Class.");
	}
	
	public AppiumMobileAutomation(Mobile mobile, URL appiumServerUrl, String platformVersion, String deviceName, File applicationFile, boolean testNgEnabled) {
		this.log = LogManager.getLogger(this.getClass());
		this.log.debug("Initializing AppiumWebAutomation Class.");
		this.appiumMobileDriver = new AppiumMobileDriver(appiumServerUrl);
		this.implicitWaitDuration = 20;
		this.testNgEnabled = testNgEnabled;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.applicationFile = applicationFile;
		this.log.debug("Successfully initialized AppiumWebAutomation Class.");
	}
	
	@Override
	public void openApplication() {
		this.log.info("I open application.");
		switch(this.mobile) {
		case iOS:
			this.driver = this.appiumMobileDriver.getIOSDriver(this.platformVersion, this.deviceName, this.applicationFile);
			break;
		case Android:
			this.driver = this.appiumMobileDriver.getAndroidDriver(this.platformVersion, this.deviceName, this.applicationFile);
			break;
		default:
			this.log.fatal("Encountered unsupported Mobile Platform while initializing AppiumDriver. Check defined Mobile Platform.");
			System.exit(1);
		}
		this.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}
	
	private void setTouchAction() {
		if (this.driver == null) {
			this.log.fatal("AppiumDriver is null. Make sure AppiumDriver is initialized first.");
		} else {
			switch(this.mobile) {
			case iOS:
				this.touchAction = new TouchAction<IOSTouchAction>(this.driver);
				break;
			case Android:
				this.touchAction = new TouchAction<AndroidTouchAction>(this.driver);
				break;
			default:
				this.log.fatal("Encountered unsupported Mobile Platform while initializing TouchAction. Check defined Mobile Platform.");
			}
		}
		
	}

	@Override
	public void tap(Object locator) {
		this.log.info("I tap on Mobile Element: \"" + locator.toString() + "\".");
		this.setTouchAction();
		TapOptions tapOptions = new TapOptions();
		MobileElement element = this.driver.findElement((By)locator);
		this.touchAction.tap(tapOptions.withElement(ElementOption.element(element))).perform();
	}
	
	@Override
	public void tapFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToTap) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void longPress(Object locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void type(Object locator, String inputText) {
		this.log.info("I type \"" + inputText + "\" at Mobile Element: \"" + locator.toString() + "\".");
		MobileElement element = this.driver.findElement((By)locator);
		element.sendKeys(inputText);
	}
	
	@Override
	public void typeFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToFill, String inputText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear(Object locator) {
		this.log.info("I clear Mobile Element: \"" + locator.toString() + "\".");
		MobileElement element = this.driver.findElement((By)locator);
		element.clear();
	}

	@Override
	public String getText(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextFromTableBasedOnText(Object objectToCheckText, String textToCheck,
			Object objectToGetTextFrom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeValue(Object locator, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void wait(int duration) {
		// TODO Auto-generated method stub
		
	}
	
	/*#######################################################*/
	/*                     VERIFICATIONS                     */
	/*#######################################################*/

	@Override
	public TestStatus verifyTappable(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyValue(Object locator, String expectedValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyAttributeValue(Object locator, String attribute, String expectedValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyText(Object locator, String expectedValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyDisplayed(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyNotDisplayed(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyEnabled(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestStatus verifyDisabled(Object locator) {
		// TODO Auto-generated method stub
		return null;
	}

}
