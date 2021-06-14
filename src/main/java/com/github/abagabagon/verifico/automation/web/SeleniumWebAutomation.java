package com.github.abagabagon.verifico.automation.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.abagabagon.verifico.automation.web.SeleniumBrowserCommands.BrowserAction;
import com.github.abagabagon.verifico.automation.web.SeleniumBrowserCommands.SwitchAction;
import com.github.abagabagon.verifico.automation.web.SeleniumGetCommands.GetAction;
import com.github.abagabagon.verifico.automation.web.SeleniumKeyboardCommands.KeyboardAction;
import com.github.abagabagon.verifico.automation.web.SeleniumMouseCommands.MouseAction;
import com.github.abagabagon.verifico.automation.web.SeleniumSelectCommands.SelectAction;
import com.github.abagabagon.verifico.enums.Browser;

/**
 * Selenium implemented Web Automation Commands
 * 
 * @author albagabagon
 *
 */

public class SeleniumWebAutomation implements WebAutomation {

	protected WebDriver driver;
	protected Logger log;
	private JavascriptExecutor javascriptExecutor;
	private Actions action;
	private ArrayList<String> tabs;
	private Browser browser;
	private boolean isHeadless;
	private SeleniumWebDriver seleniumWebDriver;
	private SeleniumWait seleniumWait;
	
	
	private SeleniumBrowserCommands browserCommand;
	private SeleniumGetCommands getCommand;
	private SeleniumKeyboardCommands keyboardCommand;
	private SeleniumMouseCommands mouseCommand;
	private SeleniumSelectCommands selectCommand;

	
	public SeleniumWebAutomation(Browser browser) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.browser = browser;
		this.isHeadless = false;
	}

	public SeleniumWebAutomation(Browser browser, boolean isHeadless) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.browser = browser;
		this.isHeadless = isHeadless;
	}
	
	/* ####################################################### */
	/*                     BROWSER ACTIONS                     */
	/* ####################################################### */
	
	@Override
	public void openBrowser() {
		this.log.debug("I open Web Browser.");
		this.driver = this.seleniumWebDriver.getWebDriver(this.browser, this.isHeadless);
		this.seleniumWait = new SeleniumWait(this.driver, this.setExplicitWait(15));
		this.action = new Actions(this.driver);
		this.javascriptExecutor = (JavascriptExecutor)this.driver;
		this.browserCommand = new SeleniumBrowserCommands(this.driver, this.javascriptExecutor, this.seleniumWait);
		this.getCommand = new SeleniumGetCommands(this.driver, this.seleniumWait);
		this.keyboardCommand = new SeleniumKeyboardCommands(this.driver, this.action, this.seleniumWait);
		this.mouseCommand = new SeleniumMouseCommands(this.driver, this.javascriptExecutor, this.action, this.seleniumWait);
		this.selectCommand = new SeleniumSelectCommands(this.driver, this.seleniumWait);
		this.maximize();
		this.deleteAllCookies();
		this.setImplicitWait(10);
	}
	
	@Override
	public void closeBrowser() {
		this.log.debug("I close Browser.");
		this.browserCommand.execute(BrowserAction.CLOSE_BROWSER, null);
	}
	
	@Override
	public void openTab(String url) {
		this.log.debug("I open New Tab.");
		this.browserCommand.execute(BrowserAction.OPEN_TAB, url);
	}
	
	@Override
	public void closeTab() {
		this.log.debug("I close Tab.");
		this.browserCommand.execute(BrowserAction.CLOSE_TAB, null);
	}

	@Override
	public void goTo(String url) {
		this.log.debug("I navigate to URL: \"" + url + "\".");
		this.browserCommand.execute(BrowserAction.GO_TO, url);
	}
	
	@Override
	public void back() {
		this.log.debug("I click back.");
		this.browserCommand.execute(BrowserAction.BACK, null);
	}

	@Override
	public void forward() {
		this.log.debug("I click forward.");
		this.browserCommand.execute(BrowserAction.FORWARD, null);
	}

	@Override
	public void refresh() {
		this.log.debug("I click refresh.");
		this.browserCommand.execute(BrowserAction.REFRESH, null);
	}

	@Override
	public void maximize() {
		this.log.trace("I maximize Web Browser Window.");
		this.browserCommand.execute(BrowserAction.MAXIMIZE, null);
	}

	@Override
	public void deleteAllCookies() {
		this.log.trace("I delete all cookies.");
		this.browserCommand.execute(BrowserAction.DELETE_ALL_COOKIES, null);
	}
	
	@Override
	public boolean switchTabByTitle(String title) {
		this.log.debug("I switch to Tab with Page Title: \"" + title + "\".");
		boolean isExisting = this.browserCommand.executeSwitchTab(SwitchAction.BY_TITLE, title);
		return isExisting;
	}
	
	@Override
	public boolean switchTabByURL(String url) {
		this.log.debug("I switch to Tab with Page URL: \"" + url + "\".");
		boolean isExisting = this.browserCommand.executeSwitchTab(SwitchAction.BY_URL, url);
		return isExisting;
	}
	
	@Override
	public void switchTabToOriginal() {
		this.log.debug("I switch to Original Tab.");
		try {
			this.tabs = new ArrayList<String>(this.driver.getWindowHandles());
		} catch (NullPointerException e) {
			this.log.fatal("Unable to get current browser tabs. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get current browser tabs.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			this.driver.switchTo().window(this.tabs.get(0));
		} catch (NoSuchWindowException e) {
			this.log.error("Encountered an error while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void scroll(String pixelHorizontal, String pixelVertical) {
		this.log.trace("I scroll page.");
		String script = "window.scrollBy(" + pixelHorizontal + ", " + pixelVertical + ")";
		try {
			this.javascriptExecutor.executeScript(script);
		} catch (NullPointerException e) {
			this.log.error("Unable to scroll page. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	void setImplicitWait(long duration) {
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
	
	WebDriverWait setExplicitWait(long duration) {
		this.log.trace("I initialize Explicit Wait.");
		WebDriverWait wait = null;
		try {
			wait = new WebDriverWait(this.driver, duration);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Explicit Wait. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return wait;
	}
	
	/* ####################################################### */
	/*                       USER ACTIONS                      */
	/* ####################################################### */
	
	/********************** MOUSE COMMANDS *********************/
	
	@Override
	public void point(By locator) {
		this.log.debug("I point at Web Element: \"" + locator.toString() + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.POINT, locator);
	}
	
	@Override
	public void click(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICK, locator);
	}
	
	@Override
	public void clickJS(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICKJS, locator);
	}
	
	@Override
	public void clickAndHold(By locator) {
		this.log.debug("I click and hold Web Element: \"" + locator.toString() + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICK_AND_HOLD, locator);
	}
	
	@Override
	public void doubleClick(By locator) {
		this.log.debug("I double click Web Element: \"" + locator.toString() + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.DOUBLE_CLICK, locator);
	}
	
	@Override
	public void point(By parent, By child) {
		this.log.debug("I point at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.POINT, parent, child);
	}
	
	@Override
	public void click(By parent, By child) {
		this.log.debug("I click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICK, parent, child);
	}
	
	@Override
	public void clickJS(By parent, By child) {
		this.log.debug("I click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICKJS, parent, child);
	}
	
	@Override
	public void clickAndHold(By parent, By child) {
		this.log.debug("I click and hold Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICK_AND_HOLD, parent, child);
	}
	
	@Override
	public void doubleClick(By parent, By child) {
		this.log.debug("I double-click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.DOUBLE_CLICK, parent, child);
	}
	
	@Override
	public void point(By parent, int index, By child) {
		this.log.debug("I point at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.POINT, parent, index, child);
	}
	
	@Override
	public void click(By parent, int index, By child) {
		this.log.debug("I click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICK, parent, index, child);
	}
	
	@Override
	public void clickJS(By parent, int index, By child) {
		this.log.debug("I click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.CLICKJS, parent, index, child);
	}
	
	@Override
	public void doubleClick(By parent, int index, By child) {
		this.log.debug("I double-click Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.mouseCommand.doBasicCommand(MouseAction.DOUBLE_CLICK, parent, index, child);
	}
	
	@Override
	public void pointOnListElement(By objectList, int index) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.POINT, objectList, index);
	}
	
	@Override
	public void clickOnListElement(By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICK, objectList, index);
	}
	
	@Override
	public void clickJSOnListElement(By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICKJS, objectList, index);
	}
	
	@Override
	public void doubleClickOnListElement(By objectList, int index) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.DOUBLE_CLICK, objectList, index);
	}
	
	@Override
	public void pointOnListElement(By parent, By objectList, int index) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.POINT, parent, objectList, index);
	}
	
	@Override
	public void clickOnListElement(By parent, By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICK, parent, objectList, index);
	}
	
	@Override
	public void clickJSOnListElement(By parent, By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICKJS, parent, objectList, index);
	}
	
	@Override
	public void doubleClickOnListElement(By parent, By objectList, int index) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.DOUBLE_CLICK, parent, objectList, index);
	}
	
	@Override
	public void pointOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.POINT, objectList, textToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICK, objectList, textToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICKJS, objectList, textToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.DOUBLE_CLICK, objectList, textToCheck);
	}
	
	@Override
	public void pointOnListElementBasedOnText(By parent, By objectList, String textToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.POINT, parent, objectList, textToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnText(By parent, By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICK, parent, objectList, textToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnText(By parent, By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICKJS, parent, objectList, textToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnText(By parent, By objectList, String textToCheck) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.DOUBLE_CLICK, parent, objectList, textToCheck);
	}
	
	@Override
	public void pointOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.POINT, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICK, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICKJS, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck) {
		this.log.debug("I double-click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void pointOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.POINT, parent, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICK, parent, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICKJS, parent, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I double-click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, parent, objectList, attribute, valueToCheck);
	}
	
	
	@Override
	public void pointOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.POINT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICK, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICKJS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.DOUBLE_CLICK, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToDoubleClick);
	}
	
	@Override
	public void pointOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.POINT, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICK, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICKJS, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.DOUBLE_CLICK, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToDoubleClick);
	}
	
	@Override
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICKJS, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToDoubleClick);
	}
	
	@Override
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, parent, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, parent, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICKJS, parent, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, parent, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToDoubleClick);
	}
	
	/********************** KEYBOARD COMMANDS *********************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void clear(By locator) {
		this.log.debug("I clear Web Element: \"" + locator.toString() + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.CLEAR, locator, null, null);
	}
	
	@Override
	public void press(By locator, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\"."); 
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.SEND_KEYS, locator, null, keyButton);
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.SEND_KEYS, locator, inputText, null);
	}
	
	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_TEXT, locator, null);
		return text;
	}
	
	@Override
	public String getValue(By locator) {
		this.log.debug("I get value from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, locator, "value");
		return text;
	}
	
	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, locator, attribute);
		return text;
	}
	
	@Override
	public String getDropDownListValue(By locator) {
		this.log.debug("I get value from Drop-down List Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_DROPDOWN, locator, null);
		return text;
	}
	
	@Override
	public void select(By locator, String option) {
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.selectCommand.executeSelectCommands(SelectAction.SELECT, locator, option);
	}
	
	@Override
	public void select(By locator, By optionList, String option) {
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.click(locator);
		this.clickOnListElementBasedOnText(optionList, option);
	}
	
	@Override
	public void deselect(By locator, String option) {
		this.log.debug("I deselect option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.selectCommand.executeSelectCommands(SelectAction.DESELECT, locator, option);
	}
	
	@Override
	public void clear(By parent, By child) {
		this.log.debug("I clear Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.CLEAR, parent, child, null, null);
	}
	
	@Override
	public void press(By parent, By child, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\"."); 
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.SEND_KEYS, parent, child, null, keyButton);
	}
	
	@Override
	public void type(By parent, By child, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.SEND_KEYS, parent, child, inputText, null);
	}
	
	@Override
	public void clear(By parent, int index, By child) {
		this.log.debug("I clear Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.CLEAR, parent, child, index, null, null);
	}
	
	@Override
	public void press(By parent, int index, By child, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.PRESS, parent, child, index, null, keyButton);
	}
	
	@Override
	public void type(By parent, int index, By child, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.executeKeyboardCommands(KeyboardAction.SEND_KEYS, parent, child, index, inputText, null);
	}

	@Override
	public String getText(By parent, By child) {
		this.log.debug("I get text from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_TEXT, parent, child, null);
		return text;
	}
	
	@Override
	public String getValue(By parent, By child) {
		this.log.debug("I get value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, parent, child, "value");
		return text;
	}
	
	@Override
	public String getAttributeValue(By parent, By child, String attribute) {
		this.log.debug("I get attribute value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, parent, child, attribute);
		return text;
	}
	
	@Override
	public String getDropDownListValue(By parent, By child) {
		this.log.debug("I get value from Drop-down List Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.executeGetCommands(GetAction.GET_DROPDOWN, parent, child, null);
		return text;
	}
	
	@Override
	public String getText(By parent, int index, By child) {
		this.log.debug("I get text from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedText = this.getCommand.executeGetCommands(GetAction.GET_TEXT, parent, child, index, null);
		return retrievedText;
	}
	
	@Override
	public String getValue(By parent, int index, By child) {
		this.log.debug("I get value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedValue = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, parent, child, index, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValue(By parent, int index, By child, String attribute) {
		this.log.debug("I get attribute value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedValue = this.getCommand.executeGetCommands(GetAction.GET_ATTRIBUTE, parent, child, index, attribute);
		return retrievedValue;
	}
	

	

	

	
	
	

	

	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.CLEAR, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, null, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.PRESS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\"attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.CLEAR, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClear, valueToCheck, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.PRESS, rowObjectList, rowObjectToCheckText, valueToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, valueToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.SEND_KEYS, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.PRESS, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.CLEAR, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, null, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.SEND_KEYS, parent, rowObjectList, rowObjectToCheckText, valueToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.PRESS, parent, rowObjectList, rowObjectToCheckText, valueToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\"attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.executeTableKeyboardCommands(KeyboardAction.CLEAR, parent, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClear, valueToCheck, null);
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.executeTableGetCommands(GetAction.GET_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.executeTableGetCommands(GetAction.GET_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.executeTableGetCommands(GetAction.GET_TEXT, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.executeTableGetCommands(GetAction.GET_TEXT, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.executeTableGetCommands(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public void dragAndDrop(By sourceObject, By targetObject) {
		this.log.debug("I drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
		boolean actionPerformed = false;
		WebElement sourceElement = null;
		WebElement targetElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceObject);
				targetElement = this.seleniumWait.waitForObjectToBeClickable(targetObject);
				this.action.dragAndDrop(sourceElement, targetElement).perform();
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (TimeoutException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". Wait time has expired.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}

	@Override
	public void acceptAlert() {
		this.log.debug("I accept Javascript Alert.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.accept();
	}
	
	@Override
	public void cancelAlert() {
		this.log.debug("I cancel Javascript Alert.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.dismiss();
	}
	
	@Override
	public void typeAlert(String inputText) {
		this.log.debug("I type: \"" + inputText + "\" at Javascript Alert Text Box.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.sendKeys(inputText);
	}
	
	@Override
	public int count(By locator) {
		this.log.debug("I count Web Element: \"" + locator.toString() + "\".");
		this.seleniumWait.waitForPage();
		this.setImplicitWait(2);
		List<WebElement> element = this.driver.findElements(locator);
		int size = element.size();
		this.setImplicitWait(10);
		this.log.debug("I counted " + size + " instances of Web Element: \"" + locator.toString() + "\".");
		return size;
	}
	
	@Override
	public void wait(int duration) {
		this.log.debug("I wait for " + duration + " Second(s).");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for " + duration + ".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/* ####################################################### */
	/*                      VERIFICATIONS                      */
	/* ####################################################### */
	
	@Override
	public boolean seeUrl(String expectedUrl) {
		this.log.debug("I see Page URL: \"" + expectedUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToBe(expectedUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.debug("I saw Page URL: \"" + expectedUrl + "\".");
		} else {
			this.log.error("I didn't see Page URL: \"" + expectedUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeUrl(String url) {
		this.log.debug("I see Page URL is not \"" + url + "\".");
		this.seleniumWait.waitForPage();
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.equals(url);
		boolean status = false;
		if(isUrlEqual) {
			this.log.error("I saw Page URL: \"" + url + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see Page URL: \"" + url + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialUrl(String partialUrl) {
		this.log.debug("I see partial Page URL: \"" + partialUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToContain(partialUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.debug("I saw partial Page URL: \"" + partialUrl + "\".");
		} else {
			this.log.error("I didn't see partial Page URL: \"" + partialUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeePartialUrl(String partialUrl) {
		this.log.debug("I see partial Page URL is not \"" + partialUrl + "\".");
		this.seleniumWait.waitForPage();
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.contains(partialUrl);
		boolean status = false;
		if(isUrlEqual) {
			this.log.error("I saw partial Page URL: \"" + partialUrl + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see partial Page URL: \"" + partialUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seeTitle(String expectedTitle) {
		this.log.debug("I see Page Title: \"" + expectedTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToBe(expectedTitle);
		String actualTitle = this.driver.getTitle().trim();
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.debug("I saw Page Title: \"" + expectedTitle + "\".");
		} else {
			this.log.error("I didn't see Page Title: \"" + expectedTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTitle(String title) {
		this.log.debug("I see Page Title is not \"" + title + "\".");
		this.seleniumWait.waitForPage();
		String actualTitle = this.driver.getTitle().trim();
		boolean isTitleEqual = actualTitle.equals(title);
		boolean status = false;
		if(isTitleEqual) {
			this.log.error("I saw Page Title: \"" + title + "\".");
		} else {
			status = true;
			this.log.debug("I don't see Page Title: \"" + title + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialTitle(String expectedPartialTitle) {
		this.log.debug("I see partial Page Title: \"" + expectedPartialTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToContain(expectedPartialTitle);
		String actualTitle = this.driver.getTitle().trim();
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.debug("I saw Page Title: \"" + expectedPartialTitle + "\".");
		} else {
			this.log.error("I didn't see Page Title: \"" + expectedPartialTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTitle(String partialTitle) {
		this.log.debug("I see partial Page Title is not \"" + partialTitle + "\".");
		this.seleniumWait.waitForPage();
		String actualTitle = this.driver.getTitle().trim();
		boolean isTitleEqual = actualTitle.contains(partialTitle);
		boolean status = false;
		if(isTitleEqual) {
			this.log.error("I saw Page Title: \"" + partialTitle + "\".");
		} else {
			status = true;
			this.log.debug("I don't see Page Title: \"" + partialTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean typed(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is typed on Web Element: \"" + locator.toString() + "\".");
		boolean isValueEqual = this.seleniumWait.waitForValueToBe(locator, expectedValue);
		String actualValue = this.getValue(locator);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I typed \"" + expectedValue + "\" on Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't type \"" + expectedValue + "\" on Web Element: \"" + locator.toString() + "\". Actual value typed is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean didntType(By locator, String value) {
		this.log.debug("I see \"" + value + "\" is not typed on Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I typed \"" + value + "\" on Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't type \"" + value + "\" on Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seePartialAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeePartialAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean selectedDropDown(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is selected at Drop-down List Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		Select select = new Select(element);
		String actualValue = select.getFirstSelectedOption().getText().toLowerCase();
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I selected: \"" + expectedValue + "\" at Drop-down List Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't select: \"" + expectedValue + "\" at Drop-down List Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;
	}

	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfListElement(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.equals(expectedValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfListElement(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.equals(value)) {
					flgTextFound = true;
					this.log.error("I saw \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
			int size = rows.size();
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(expectedValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if(!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								status = false;
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
								status = true;
							}
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialText(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of Web Element: \"" + locator.toString() + "\".");
		boolean isValueEqual = this.seleniumWait.waitForTextToContain(locator, expectedPartialValue);
		String actualText = this.getText(locator).trim();
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialText(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as not the partial text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.contains(partialValue);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + partialValue + "\" as the partial text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + partialValue + "\" as the partial the text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfListElement(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.contains(expectedPartialValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfListElement(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.contains(partialValue)) {
					flgTextFound = true;
					this.log.error("I saw \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeePartialTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.contains(expectedPartialValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeePartialTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.contains(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.contains(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.contains(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if (!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}

	@Override
	public boolean see(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" displayed.");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = false;
		if (element != null) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\".");
		}
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" is not displayed.");
		this.setImplicitWait(2);
		this.seleniumWait.waitForObjectToBeInvisible(locator);
		List<WebElement> elements = this.driver.findElements(locator);
		boolean status = false;
		if (elements.size() > 0) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\".");
		}
		this.setImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSee = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectToSee, j);
					if (elementToSee.size() > 0) {
						status = true;
						this.log.debug("I saw the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					} else {
						this.log.debug("I didn't see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to not be displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.setImplicitWait(2);
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					List<WebElement> elementToSee = rows.get(j).findElements(rowObjectToSee);
					if (elementToSee.size() > 0) {
						this.log.error("I saw the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					} else {
						status = true;
						this.log.debug("I didn't see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		this.setImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" enabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\" enabled.");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\" enabled.");
		}
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" disabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\" enabled.");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\" enabled.");
		}
		this.log.info("Status Enabled? " + isEnabled);
		return status;
	}

	@Override
	public boolean selected(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, true);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (isSelected) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\" selected.");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\" selected.");
		}
		return status;
	}

	@Override
	public boolean deselected(By locator) {
		this.log.debug("I see element \"" + locator.toString() + "\" not selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, false);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (isSelected) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\" selected.");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\" selected.");
		}
		return status;
	}
	
	@Override
	public boolean counted(By locator, int count) {
		this.log.debug("I count Web Element: \"" + locator.toString() + "\".");
		boolean isEqual = this.seleniumWait.waitForCountToBe(locator, count);
		int size = this.driver.findElements(locator).size();
		boolean status = false;
		if (isEqual) {
			this.log.debug("I verified count of Web Element: \"" + locator.toString() + "\" is \"" + count + "\".");
			status = true;
		} else {
			this.log.error("I verified count of Web Element: \"" + locator.toString() + "\" is not \"" + count + "\". Actual count is \"" + size + "\".");
		}
		return status;
	}

	@Override
	public boolean seeAlertMessage(String expectedMessage) {
		this.log.debug("I see \"" + expectedMessage + "\" Alert Message displayed.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		String actualMessage = alert.getText().trim();
		boolean isValueEqual = actualMessage.equals(expectedMessage);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw alert message: \"" + expectedMessage + "\" displayed.");
		} else {
			this.log.error("I didn't see alert message: \"" + expectedMessage + "\" displayed.");
		}
		return status;
	}
}