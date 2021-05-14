package com.github.abagabagon.verifico.automation.mobile;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import com.github.abagabagon.verifico.enums.Mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

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
		this.implicitWaitDuration = 5;
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.applicationSource = ApplicationSource.File;
		this.applicationFile = applicationFile;
	}
	
	public AppiumMobileAutomation(String deviceName, Mobile mobile, String platformVersion, URL applicationUrl, File applicationFile, URL appiumServerUrl) {
		this.log = LogManager.getLogger(this.getClass());
		this.appiumMobileDriver = new AppiumMobileDriver(appiumServerUrl);
		this.implicitWaitDuration = 5;
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
		this.initializeImplicitWait(this.implicitWaitDuration);
	}
	
	private void initializeImplicitWait(long duration) {
		this.log.trace("I initialize Implicit Wait.");
		try {
			this.driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Implicit Wait. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void closeApplication() {
		try {
			this.log.debug("I close Application.");
			this.driver.quit();
		} catch (NullPointerException e) {
			this.log.error("Unable to close Application. Application might have already been closed or was never opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to close Application.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
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
	
	@Override
	public void resetApp() {
		this.log.debug("I reset application.");
		this.driver.resetApp();
	}
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	@Override
	public void swipe(int startX, int startY, int endX, int endY) {
		this.log.debug("I swipe from (" + startX + ", " + startY + ") Coordinate to (" + endX + ", " + endY + ") Coordinate.");
		this.action.press(PointOption.point(startX, startY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(endX, endY)).release().perform();
	}
	
	@Override
	public void swipeDown() {
		this.log.debug("I swipe down.");
		double anchorPercentage = 0.5;
		double startPercentage = 0.2;
		double finalPercentage = 0.8;
		Dimension size = this.driver.manage().window().getSize();
		int anchor = (int) (size.width * anchorPercentage);
		int startPoint = (int) (size.height * startPercentage);
		int endPoint = (int) (size.height * finalPercentage);
		this.action.press(PointOption.point(anchor, startPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(anchor, endPoint)).release().perform();
	}
	
	@Override
	public void swipeUp() {
		this.log.debug("I swipe up.");
		double anchorPercentage = 0.5;
		double startPercentage = 0.8;
		double finalPercentage = 0.2;
		Dimension size = this.driver.manage().window().getSize();
		int anchor = (int) (size.width * anchorPercentage);
		int startPoint = (int) (size.height * startPercentage);
		int endPoint = (int) (size.height * finalPercentage);
		this.action.press(PointOption.point(anchor, startPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(anchor, endPoint)).release().perform();
	}
	
	@Override
	public void swipe(By locator) {
		this.log.debug("I swipe to Mobile Element: \"" + locator.toString() + "\".");
		double anchorPercentage = 0.5;
		double startPercentage = 0.8;
		double finalPercentage = 0.2;
		boolean isElementFound = this.driver.findElements(locator).size() > 0;
		
		while(!isElementFound) {
			Dimension size = this.driver.manage().window().getSize();
			int anchor = (int) (size.width * anchorPercentage);
			int startPoint = (int) (size.height * startPercentage);
			int endPoint = (int) (size.height * finalPercentage);
			this.action.press(PointOption.point(anchor, startPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(2000))).moveTo(PointOption.point(anchor, endPoint)).release().perform();
			isElementFound = this.driver.findElements(locator).size() > 0;
		}

	}
	
	private void executeTapCommands(UserAction userAction, By locator, long duration) {
		boolean actionPerformed = false;
		MobileElement element = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case TAP:
					TapOptions tapOptions = new TapOptions();
					element = this.driver.findElement(locator);
					this.action.tap(tapOptions.withElement(ElementOption.element(element))).perform();
					break;
				case LONG_PRESS:
					LongPressOptions longPressOptions = new LongPressOptions();
					element = this.driver.findElement(locator);
					this.action.longPress(longPressOptions.withElement(ElementOption.element(element)).withDuration(Duration.ofSeconds(duration))).release().perform();
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void tap(By locator) {
		this.log.debug("I tap on Mobile Element: \"" + locator.toString() + "\".");
		this.executeTapCommands(UserAction.TAP, locator, 0);
	}
	
	@Override
	public void longPress(By locator, long duration) {
		this.log.debug("I long press on Mobile Element: \"" + locator.toString() + "\".");
		this.executeTapCommands(UserAction.LONG_PRESS, locator, duration);
	}
	
	private void executeKeyboardCommands(UserAction userAction, By locator, String inputText) {
		boolean actionPerformed = false;
		MobileElement element = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case CLEAR:
					element = this.driver.findElement(locator);
					element.clear();
					break;
				case SEND_KEYS:
					element = this.driver.findElement(locator);
					element.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Mobile Element: \"" + locator.toString() + "\".");
		this.executeKeyboardCommands(UserAction.SEND_KEYS, locator, inputText);
	}
	
	@Override
	public void clear(By locator) {
		this.log.debug("I clear Mobile Element: \"" + locator.toString() + "\".");
		this.executeKeyboardCommands(UserAction.CLEAR, locator, null);
	}
	
	private String executeGetCommands(UserAction userAction, By locator, String attribute) {
		boolean actionPerformed = false;
		MobileElement element = null;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case GET_ATTRIBUTE:
					element = this.driver.findElement(locator);
					value = element.getText().trim();
					if (value.length() == 0) {
						this.log.trace("Mobile Element: \"" + locator.toString() + "\" has no text.");
					}
					break;
				case GET_TEXT:
					element = this.driver.findElement(locator);
					value = element.getAttribute(attribute).trim();
					if (value.length() == 0) {
						this.log.trace("The Attribute: " + attribute + " of Mobile Element: \"" + locator.toString() + "\" has no text.");
					}
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Mobile Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_TEXT, locator, "text");
		return text;
	}
	
	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.debug("I get attribute value from Mobile Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_ATTRIBUTE, locator, attribute);
		return text;
	}
	
	private void executeTapCommands(UserAction userAction, By parent, By child, int index, long duration) {
		boolean actionPerformed = false;
		MobileElement parentElement = null;
		MobileElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.driver.findElements(parent).get(index);
				childElement = parentElement.findElement(child);
				switch(userAction) {
				case TAP:
					TapOptions tapOptions = new TapOptions();
					this.action.tap(tapOptions.withElement(ElementOption.element(childElement))).perform();
					break;
				case LONG_PRESS:
					LongPressOptions longPressOptions = new LongPressOptions();
					this.action.longPress(longPressOptions.withElement(ElementOption.element(childElement)).withDuration(Duration.ofSeconds(duration))).release().perform();
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeKeyboardCommands(UserAction userAction, By parent, By child, int index, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		MobileElement parentElement = null;
		MobileElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.driver.findElements(parent).get(index);
				childElement = parentElement.findElement(child);
				switch(userAction) {
				case CLEAR:
					childElement.clear();
					break;
				case SEND_KEYS:
					childElement.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private String executeGetCommands(UserAction userAction, By parent, By child, int index, String attribute) {
		boolean actionPerformed = false;
		MobileElement parentElement = null;
		MobileElement childElement = null;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.driver.findElements(parent).get(index);
				childElement = parentElement.findElement(child);
				switch(userAction) {
				case GET_ATTRIBUTE:
					value = childElement.getText().trim();
					if (value.length() == 0) {
						this.log.trace("Mobile Element: \"" + child.toString() + "\" has no text.");
					}
					break;
				case GET_TEXT:
					value = childElement.getAttribute(attribute).trim();
					if (value.length() == 0) {
						this.log.trace("The Attribute: " + attribute + " of Mobile Element: \"" + child.toString() + "\" has no text.");
					}
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Mobile Element \"" + child.toString() + "\" under Parent Mobile Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	private void executeListTapCommands(UserAction userAction, By objectList, String textToCheck, long duration) {
		List<MobileElement> elements = this.driver.findElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(userAction) {
					case TAP:
						this.executeTapCommands(UserAction.TAP, objectList, j, 0);
						break;
					case LONG_PRESS:
						this.executeTapCommands(UserAction.LONG_PRESS, objectList, j, duration);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element List: \"" +  objectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element List: \"" +  objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeListTapCommands(UserAction userAction, By objectList, String attribute, String valueToCheck, long duration) {
		List<MobileElement> elements = this.driver.findElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(userAction) {
					case TAP:
						this.executeTapCommands(UserAction.TAP, objectList, j, 0);
						break;
					case LONG_PRESS:
						this.executeTapCommands(UserAction.LONG_PRESS, objectList, j, duration);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Mobile Element List: \"" +  objectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Mobile Element List: \"" +  objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeTapCommands(UserAction userAction, By locator, int index, long duration) {
		boolean actionPerformed = false;
		List<MobileElement> elements = null;
		for(int i = 1; i <= 4; i++) {
			try {
				elements = this.driver.findElements(locator);
				switch(userAction) {
				case TAP:
					TapOptions tapOptions = new TapOptions();
					this.action.tap(tapOptions.withElement(ElementOption.element(elements.get(index)))).perform();
					break;
				case LONG_PRESS:
					LongPressOptions longPressOptions = new LongPressOptions();
					this.action.longPress(longPressOptions.withElement(ElementOption.element(elements.get(index))).withDuration(Duration.ofSeconds(duration))).release().perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\". The Mobile Element is no longer present in the Screen.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Mobile Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void tapOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I tap a Mobile Element from the Mobile Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.executeListTapCommands(UserAction.TAP, objectList, textToCheck, 0);
	}
	
	@Override
	public void tapOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck) {
		this.log.debug("I tap a Mobile Element from the Mobile Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.executeListTapCommands(UserAction.TAP, objectList, attribute, valueToCheck, 0);
	}
	
	@Override
	public void longPressOnListElementBasedOnText(By objectList, String textToCheck, long duration) {
		this.log.debug("I tap a Mobile Element from the Mobile Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.executeListTapCommands(UserAction.LONG_PRESS, objectList, textToCheck, duration);
	}
	
	@Override
	public void longPressOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck, long duration) {
		this.log.debug("I tap a Mobile Element from the Mobile Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.executeListTapCommands(UserAction.LONG_PRESS, objectList, attribute, valueToCheck, duration);
	}
	
	private void executeTableTapCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, long duration) {
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					MobileElement elementToDoActionTo = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToDoActionTo);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case TAP:
							this.executeTapCommands(UserAction.TAP, rowObjectList, rowObjectToDoActionTo, j, 0);
							break;
						case LONG_PRESS:
							this.executeTapCommands(UserAction.LONG_PRESS, rowObjectList, rowObjectToDoActionTo, j, duration);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeTableTapCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToDoActionTo, long duration) {
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(valueToCheck)) {
					MobileElement elementToDoActionTo = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToDoActionTo);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case TAP:
							this.executeTapCommands(UserAction.TAP, rowObjectList, rowObjectToDoActionTo, j, 0);
							break;
						case LONG_PRESS:
							this.executeTapCommands(UserAction.LONG_PRESS, rowObjectList, rowObjectToDoActionTo, j, duration);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void tapOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I tap the Mobile Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableTapCommands(UserAction.TAP, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick, 0);
	}
	
	@Override
	public void tapOnTableRowElementBasedOnTableRowAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I tap the Mobile Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableTapCommands(UserAction.TAP, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClick, 0);
	}
	
	@Override
	public void longPressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick, long duration) {
		this.log.debug("I tap the Mobile Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableTapCommands(UserAction.LONG_PRESS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick, duration);
	}
	
	@Override
	public void longPressOnTableRowElementBasedOnTableRowAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClick, long duration) {
		this.log.debug("I tap the Mobile Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableTapCommands(UserAction.LONG_PRESS, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClick, duration);
	}
	
	private void executeTableKeyboardCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					MobileElement elementToDoActionTo = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToDoActionTo);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case CLEAR:
							this.executeKeyboardCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Mobile Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableKeyboardCommands(UserAction.CLEAR, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, textToCheck, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Mobile Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableKeyboardCommands(UserAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	private String executeTableGetCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					MobileElement elementToDoActionTo = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToDoActionTo);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Mobile Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.executeTableGetCommands(UserAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Mobile Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.executeTableGetCommands(UserAction.GET_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public int count(By locator) {
		this.log.debug("I count Mobile Element: \"" + locator.toString() + "\".");
		List<MobileElement> element = this.driver.findElements(locator);
		int size = element.size();
		this.log.debug("I counted " + size + " instances of Mobile Element: \"" + locator.toString() + "\".");
		return size;
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
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seePartialAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeePartialAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of Mobile Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as text value of at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not text value of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of Mobile Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as text value of at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not text value of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfListElement(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
		List<MobileElement> elements = this.driver.findElements(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement element = this.driver.findElements(locator).get(j);
				String text = element.getText().trim();
				if (text.equals(expectedValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfListElement(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
		List<MobileElement> elements = this.driver.findElements(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement element = this.driver.findElements(locator).get(j);
				String text = element.getText().trim();
				if (text.equals(value)) {
					flgTextFound = true;
					this.log.error("I saw \"" + value + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			List<MobileElement> rows = this.driver.findElements(rowObjectList);
			int size = rows.size();
			for(int j = 0; j < size; j++) {
				MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(expectedValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);;
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);;
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElements(rowObjectListToSeeTextFrom);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if(!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the text value of the Mobile Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElements(rowObjectListToSeeTextFrom);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
								status = false;
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
								status = true;
							}
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialText(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of Mobile Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.contains(expectedPartialValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedPartialValue + "\" as not the partial text value of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialText(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as not the partial text value of Mobile Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.contains(partialValue);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + partialValue + "\" as the partial text value of at Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + partialValue + "\" as not the partial the text value of Mobile Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfListElement(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
		List<MobileElement> elements = this.driver.findElements(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement element = this.driver.findElements(locator).get(j);
				String text = element.getText().trim();
				if (text.contains(expectedPartialValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfListElement(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
		List<MobileElement> elements = this.driver.findElements(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement element = this.driver.findElements(locator).get(j);
				String text = element.getText().trim();
				if (text.contains(partialValue)) {
					flgTextFound = true;
					this.log.error("I saw \"" + partialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Mobile Elements from the Mobile Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeePartialTextFrom);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.contains(expectedPartialValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeePartialTextFrom);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.contains(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);;
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.contains(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					MobileElement elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToSeeTextFrom);;
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.contains(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Mobile Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Mobile Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElements(rowObjectListToSeeTextFrom);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if (!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial text value of the Mobile Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSeeTextFrom = this.driver.findElements(rowObjectList).get(j).findElements(rowObjectListToSeeTextFrom);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Mobile Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}

	@Override
	public boolean see(By locator) {
		this.log.debug("I see Mobile Element: \"" + locator.toString() + "\" displayed.");
		MobileElement element = this.driver.findElement(locator);
		boolean status = false;
		if (element != null) {
			status = true;
			this.log.debug("I saw Mobile Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see Mobile Element: \"" + locator.toString() + "\".");
		}
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I see Mobile Element: \"" + locator.toString() + "\" is not displayed.");
		this.initializeImplicitWait(2);
		List<MobileElement> elements = this.driver.findElements(locator);
		boolean status = false;
		if (elements.size() > 0) {
			this.log.error("I saw Mobile Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see Mobile Element: \"" + locator.toString() + "\".");
		}
		this.initializeImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Mobile Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSee = this.driver.findElements(rowObjectList).get(j).findElements(rowObjectToSee);
					if (elementToSee.size() > 0) {
						status = true;
						this.log.debug("I saw the Mobile Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
					} else {
						this.log.debug("I didn't see the Mobile Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Mobile Element: \"" + rowObjectToSee.toString() + "\" to not be displayed within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Mobile Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.initializeImplicitWait(2);
		List<MobileElement> rows = this.driver.findElements(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				MobileElement elementToCheckText = this.driver.findElements(rowObjectList).get(j).findElement(rowObjectToCheckText);;
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Mobile Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<MobileElement> elementToSee = rows.get(j).findElements(rowObjectToSee);
					if (elementToSee.size() > 0) {
						this.log.error("I saw the Mobile Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
					} else {
						status = true;
						this.log.debug("I didn't see the Mobile Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Mobile Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Mobile Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Mobile Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		this.initializeImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("I see Mobile Element \"" + locator.toString() + "\" enabled.");
		MobileElement element = this.driver.findElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			status = true;
			this.log.debug("I saw Mobile Element: \"" + locator.toString() + "\" enabled.");
		} else {
			this.log.error("I didn't see Mobile Element: \"" + locator.toString() + "\" enabled.");
		}
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("I see Mobile Element \"" + locator.toString() + "\" disabled.");
		MobileElement element = this.driver.findElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			this.log.error("I saw Mobile Element: \"" + locator.toString() + "\" enabled.");
		} else {
			status = true;
			this.log.debug("I didn't see Mobile Element: \"" + locator.toString() + "\" enabled.");
		}
		this.log.info("Status Enabled? " + isEnabled);
		return status;
	}

}
