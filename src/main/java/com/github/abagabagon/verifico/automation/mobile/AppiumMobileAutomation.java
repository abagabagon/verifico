package com.github.abagabagon.verifico.automation.mobile;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.touch.TouchActions;

import com.github.abagabagon.verifico.enums.Mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.ElementOption;

/**
 * Appium implemented Mobile Automation Commands
 * 
 * @author albagabagon
 *
 */

public class AppiumMobileAutomation implements MobileAutomation {
	
	AppiumDriver<MobileElement> driver;
	TouchAction<?> action;
	TouchActions actions;
	Logger log;
	File applicationFile;
	URL applicationUrl;
	long implicitWaitDuration;
	long explicitWaitDuration;
	
	private AppiumMobileDriver appiumMobileDriver;
	private Mobile mobile;
	private String platformVersion;
	private String deviceName;
	private ApplicationSource applicationSource;
	
	public enum ApplicationSource {
		File, URL
	}
	
	enum UserAction {
		CLEAR, TAP, LONG_PRESS, COUNT, GET_ATTRIBUTE, GET_TEXT, SEND_KEYS
	}
	
	/**
	 * Mobile Automation using Appium
	 * 
	 * @param deviceName		Name of the Device (e. g. Serial Number)
	 * @param mobile			Mobile Platform
	 * @param platformVersion	Mobile Platform Version
	 * @param applicationFile	Application File for testing
	 * @param appiumServerUrl	Appium Server URL
	 */
	
	public AppiumMobileAutomation(String deviceName, Mobile mobile, String platformVersion, File applicationFile, URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumMobileDriver = new AppiumMobileDriver(appiumServerUrl);
		this.implicitWaitDuration = 20;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.applicationSource = ApplicationSource.File;
		this.applicationFile = applicationFile;
	}
	
	public AppiumMobileAutomation(String deviceName, Mobile mobile, String platformVersion, URL applicationUrl, File applicationFile, URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumMobileDriver = new AppiumMobileDriver(appiumServerUrl);
		this.implicitWaitDuration = 20;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.applicationSource = ApplicationSource.URL;
		this.applicationUrl = applicationUrl;
		this.applicationFile = applicationFile;
	}
	
	/*#######################################################*/
	/*                   DEVICE ACTIONS                      */
	/*#######################################################*/
	
	@Override
	public void openApplication() {
		this.log.debug("I open Application.");
		switch(this.mobile) {
		case iOS:
			this.driver = this.appiumMobileDriver.getIOSDriver(this.platformVersion, this.deviceName);
			this.action = new TouchAction<IOSTouchAction>(this.driver);
			break;
		case Android:
			switch(this.applicationSource) {
			case File:
				this.driver = this.appiumMobileDriver.getAndroidDriver(this.platformVersion, this.deviceName, this.applicationFile);
				break;
			case URL:
				this.driver = this.appiumMobileDriver.getAndroidDriver(this.platformVersion, this.deviceName, this.applicationUrl, this.applicationFile);
				break;
			default:
				this.log.fatal("Unsupported Application Source Value.");
				System.exit(1);
			}
			this.action = new TouchAction<AndroidTouchAction>(this.driver);
			break;
		default:
			this.log.fatal("Encountered unsupported Mobile Platform while initializing AppiumDriver. Check defined Mobile Platform.");
			System.exit(1);
		}
		this.initializeImplicitWait(20);
	}
	
	private void initializeImplicitWait(long duration) {
		this.log.trace("I initialize Implicit Wait.");
		this.driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
	}
	
	@Override
	public void closeApplication() {
		this.log.debug("I close Application.");
		this.driver.quit();
	}
	
	@Override
	public Object getOrientation() {
		this.log.debug("I get Screen Orientation.");
		ScreenOrientation screenOrientation = this.driver.getOrientation();
		Object orientation = screenOrientation;
		return orientation;
	}
	
	@Override
	public void setOrientation(Object orientation) {
		this.log.debug("I set Screen Orientation.");
		this.driver.rotate((ScreenOrientation)orientation);
	}
	
	@Override
	public Object getGeolocation() {
		this.log.debug("I get Geo Location.");
		Location geoLocation = this.driver.location();
		Object location = geoLocation;
		return location;
	}
	
	@Override
	public void setGeolocation(Object location) {
		this.log.debug("I set Geo Location.");
		Location geoLocation = (Location)location;
		this.driver.setLocation(geoLocation);
	}
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	MobileElement getElement(By locator) {
		MobileElement element = null;
		element = this.driver.findElement(locator);
		return element;
	}
	
	List<MobileElement> getElements(By locator) {
		List<MobileElement> elements;
		elements = this.driver.findElements(locator);
		return elements;
	}

	@Override
	public void tap(By locator) {
		this.log.debug("I tap on Mobile Element: \"" + locator.toString() + "\".");
		TapOptions tapOptions = new TapOptions();
		MobileElement element = this.driver.findElement(locator);
		this.action.tap(tapOptions.withElement(ElementOption.element(element))).perform();
	}
	
	@Override
	public void tapFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToTap) {
		this.log.debug("I tap a Mobile Element based on Text: \"" + textToCheck + "\".");
		List<MobileElement> elementToCheckText = this.getElements(objectToCheckText);
		List<MobileElement> elementToTap = this.getElements(objectToTap);
		int size = elementToTap.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				TapOptions tapOptions = new TapOptions();
				this.action.tap(tapOptions.withElement(ElementOption.element(elementToTap.get(i)))).perform();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}
	
	@Override
	public void longPress(By locator, long duration) {
		this.log.debug("I long press on Mobile Element: \"" + locator.toString() + "\".");
		LongPressOptions longPressOptions = new LongPressOptions();
		MobileElement element = this.driver.findElement(locator);
		this.action.longPress(longPressOptions.withElement(ElementOption.element(element)).withDuration(Duration.ofSeconds(duration))).release().perform();
	}
	
	@Override
	public void longPressFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToLongPress, long duration) {
		this.log.debug("I long press a Mobile Element based on Text: \"" + textToCheck + "\".");
		List<MobileElement> elementToCheckText = this.getElements(objectToCheckText);
		List<MobileElement> elementToLongPress = this.getElements(objectToLongPress);
		int size = elementToLongPress.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				LongPressOptions longPressOptions = new LongPressOptions();
				this.action.longPress(longPressOptions.withElement(ElementOption.element(elementToLongPress.get(i))).withDuration(Duration.ofSeconds(duration))).release().perform();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}

	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Mobile Element: \"" + locator.toString() + "\".");
		MobileElement element = this.driver.findElement(locator);
		element.sendKeys(inputText);
	}
	
	@Override
	public void typeFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, String inputText) {
		this.log.debug("I type at Web Element based on Text: \"" + textToCheck + "\".");
		List<MobileElement> elementToCheckText = this.getElements(objectToCheckText);
		List<MobileElement> elementToFill = this.getElements(objectToFill);
		int size = elementToFill.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elementToFill.get(i).sendKeys(inputText);
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}

	@Override
	public void clear(By locator) {
		this.log.debug("I clear Mobile Element: \"" + locator.toString() + "\".");
		MobileElement element = this.driver.findElement(locator);
		element.clear();
	}

	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Mobile Element: \"" + locator.toString() + "\".");
		MobileElement element = this.getElement(locator);
		String text = null;
		text = element.getText().trim();
		if (text.length() == 0) {
			this.log.trace("Mobile Element: \"" + locator.toString() + "\" has no text.");
		}
		return text;
	}

	@Override
	public String getTextFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetTextFrom) {
		this.log.debug("I get text from a Mobile Element based on Text: \"" + textToCheck + "\".");
		List<MobileElement> elementToCheckText = this.getElements(objectToCheckText);
		List<MobileElement> elementToClick = this.getElements(objectToGetTextFrom);
		int size = elementToClick.size();
		boolean flgTextFound = false;
		String retrievedText = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				retrievedText = elementToClick.get(i).getText().trim();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
		return retrievedText;
	}

	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = null;
		MobileElement element = this.getElement(locator);
		text = element.getAttribute(attribute);
		if (text.length() == 0) {
			this.log.trace("The Attribute: " + attribute + " of Web Element: \"" + locator.toString() + "\".");
		}
		return text;
	}

	@Override
	public String getValue(By locator) {
		this.log.debug("I get value from Mobile Element: \"" + locator.toString() + "\".");
		String text = null;
		MobileElement element = this.getElement(locator);
		text = element.getAttribute("value");
		if (text.length() == 0) {
			this.log.trace("The Text Box/Area Web Element: \"" + locator.toString() + "\" has no value.");
		}
		return text;
	}

	@Override
	public void wait(int duration) {
		this.log.debug("I wait for " + duration + ".");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		}
	}
	
	/*#######################################################*/
	/*                     VERIFICATIONS                     */
	/*#######################################################*/

	@Override
	public boolean typed(By locator, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" is displayed at Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see value: \"" + expectedValue + "\" from Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see value: \"" + expectedValue + "\" from Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean didntType(By locator, String value) {
		this.log.debug("I verify \"" + value + "\" is not displayed at Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(!isValueEqual) {
			status = true;
			this.log.debug("I don't see value: \"" + value + "\" from Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I see value: \"" + value + "\" from Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" is displayed for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I verify \"" + value + "\" is not displayed for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(!isValueEqual) {
			status = true;
			this.log.debug("I don't see value: \"" + value + "\" for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I see value: \"" + value + "\" for attribute: \"" + attribute + "\" at Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" Text Value is displayed.");
		String actualText = this.getText(locator);
		boolean isValueEqual = actualText.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see text: \"" + expectedValue + "\" at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see text: \"" + expectedValue + "\" at Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("I verify \"" + value + "\" Text Value is displayed.");
		String actualText = this.getText(locator);
		boolean isValueEqual = actualText.equals(value);
		boolean status = false;
		if(!isValueEqual) {
			status = true;
			this.log.debug("I don't see text: \"" + value + "\" at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I see text: \"" + value + "\" at Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}

	@Override
	public boolean see(By locator) {
		this.log.debug("I verify Mobile Element: \"" + locator.toString() + "\" is displayed.");
		List<MobileElement> elements = this.getElements(locator);
		boolean status = false;
		if (elements.size() > 0) {
			status = true;
			this.log.debug("I verified Mobile Element: \"" + locator.toString() + "\" is displayed.");
		} else {
			status = false;
			this.log.error("I verified Mobile Element: \"" + locator.toString() + "\" is not displayed.");
		}
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I verify Mobile Element: \"" + locator.toString() + "\" is not displayed.");
		this.initializeImplicitWait(2);
		List<MobileElement> elements = this.getElements(locator);
		boolean status = false;
		if (elements.size() == 0) {
			status = true;
			this.log.debug("I verified Mobile Element: \"" + locator.toString() + "\" is not displayed.");
		} else {
			status = false;
			this.log.error("I verified Mobile Element: \"" + locator.toString() + "\" is displayed.");
		}
		this.initializeImplicitWait(20);
		return status;
	}

	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("I verify Mobile Element \"" + locator.toString() + "\" is enabled.");
		MobileElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			status = true;
			this.log.debug("I verified Mobile Element: \"" + locator.toString() + "\" is enabled.");
		} else {
			status = false;
			this.log.error("I verified Mobile Element: \"" + locator.toString() + "\" is not enabled.");
		}
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("I verify Mobile Element \"" + locator.toString() + "\" is disabled.");
		MobileElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (!isEnabled) {
			status = true;
			this.log.debug("I verified Mobile Element: \"" + locator.toString() + "\" is disabled.");
		} else {
			status = false;
			this.log.error("I verified Mobile Element: \"" + locator.toString() + "\" is not disabled.");
		}
		return status;
	}

}
