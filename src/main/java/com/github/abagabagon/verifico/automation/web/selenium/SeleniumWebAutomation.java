package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.ArrayList;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.abagabagon.verifico.automation.web.WebAutomation;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.BrowserAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.GetAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.KeyboardAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.MouseAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.SelectAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.StateAssertionAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.SwitchAction;
import com.github.abagabagon.verifico.automation.web.selenium.SeleniumCommands.ValueAssertionAction;
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
	
	
	private SeleniumCommands generalCommand;
	private SeleniumBrowserCommands browserCommand;
	private SeleniumGetCommands getCommand;
	private SeleniumKeyboardCommands keyboardCommand;
	private SeleniumMouseCommands mouseCommand;
	private SeleniumSelectCommands selectCommand;
	private SeleniumValueAssertionCommands valueAssertionCommand;
	private SeleniumStateAssertionCommands stateAssertionCommand;

	
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
		this.seleniumWait = new SeleniumWait(this.driver, this.setExplicitWait(5));
		this.action = new Actions(this.driver);
		this.javascriptExecutor = (JavascriptExecutor)this.driver;
		this.generalCommand = new SeleniumCommands(this.driver, this.seleniumWait);
		this.browserCommand = new SeleniumBrowserCommands(this.driver, this.javascriptExecutor, this.seleniumWait);
		this.getCommand = new SeleniumGetCommands(this.driver, this.seleniumWait);
		this.keyboardCommand = new SeleniumKeyboardCommands(this.driver, this.action, this.seleniumWait);
		this.mouseCommand = new SeleniumMouseCommands(this.driver, this.javascriptExecutor, this.action, this.seleniumWait);
		this.selectCommand = new SeleniumSelectCommands(this.driver, this.seleniumWait);
		this.valueAssertionCommand = new SeleniumValueAssertionCommands(this.driver, this.seleniumWait);
		this.stateAssertionCommand = new SeleniumStateAssertionCommands(this.driver, this.seleniumWait);
		this.maximize();
		this.deleteAllCookies();
		this.setImplicitWait(5);
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
			this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
	public void pointOnListElement(By parentList, int parentIndex, By objectList, int index) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.POINT, parentList, parentIndex, objectList, index);
	}
	
	@Override
	public void clickOnListElement(By parentList, int parentIndex, By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICK, parentList, parentIndex, objectList, index);
	}
	
	@Override
	public void clickJSOnListElement(By parentList, int parentIndex, By objectList, int index) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.CLICKJS, parentList, parentIndex, objectList, index);
	}
	
	@Override
	public void doubleClickOnListElement(By parentList, int parentIndex, By objectList, int index) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the index: \"" + index + "\".");
		this.mouseCommand.doListCommand(MouseAction.DOUBLE_CLICK, parentList, parentIndex, objectList, index);
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
	public void pointOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.POINT, parentList, parentIndex, objectList, textToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICK, parentList, parentIndex, objectList, textToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.CLICKJS, parentList, parentIndex, objectList, textToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnText(MouseAction.DOUBLE_CLICK, parentList, parentIndex, objectList, textToCheck);
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
	public void pointOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I point a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.POINT, parentList, parentIndex, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICK, parentList, parentIndex, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.CLICKJS, parentList, parentIndex, objectList, attribute, valueToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck) {
		this.log.debug("I double-click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\".");
		this.mouseCommand.doListCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, parentList, parentIndex, objectList, attribute, valueToCheck);
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
	public void pointOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.POINT, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICK, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.CLICKJS, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnText(MouseAction.DOUBLE_CLICK, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToDoubleClick);
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
	
	@Override
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint) {
		this.log.debug("I point the Web Element: \"" + rowObjectToPoint.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToPoint);
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICK, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.CLICKJS, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I double-click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		this.mouseCommand.doTableCommandBasedOnAttributeValue(MouseAction.DOUBLE_CLICK, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attribute, valueToCheck, rowObjectToDoubleClick);
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
	
	/********************** KEYBOARD COMMANDS *********************/
	
	
	@Override
	public void clear(By locator) {
		this.log.debug("I clear Web Element: \"" + locator.toString() + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.CLEAR, locator, null, null);
	}
	
	@Override
	public void press(Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "."); 
		this.action.sendKeys(keyButton).perform();;
	}
	
	@Override
	public void press(By locator, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\"."); 
		this.keyboardCommand.doBasicCommand(KeyboardAction.SEND_KEYS, locator, null, keyButton);
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.SEND_KEYS, locator, inputText, null);
	}
	
	@Override
	public void clear(By parent, By child) {
		this.log.debug("I clear Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.CLEAR, parent, child, null, null);
	}
	
	@Override
	public void press(By parent, By child, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\"."); 
		this.keyboardCommand.doBasicCommand(KeyboardAction.SEND_KEYS, parent, child, null, keyButton);
	}
	
	@Override
	public void type(By parent, By child, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.SEND_KEYS, parent, child, inputText, null);
	}
	
	@Override
	public void clear(By parent, int index, By child) {
		this.log.debug("I clear Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.CLEAR, parent, index, child, null, null);
	}
	
	@Override
	public void press(By parent, int index, By child, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.PRESS, parent, index, child, null, keyButton);
	}
	
	@Override
	public void type(By parent, int index, By child, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		this.keyboardCommand.doBasicCommand(KeyboardAction.SEND_KEYS, parent, index, child, inputText, null);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.CLEAR, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, null, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.PRESS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.SEND_KEYS, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.PRESS, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.CLEAR, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, null, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.SEND_KEYS, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.PRESS, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnText(KeyboardAction.CLEAR, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear, null, null);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\"attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.CLEAR, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClear, valueToCheck, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.PRESS, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.SEND_KEYS, parent, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.PRESS, parent, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\"attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.CLEAR, parent, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClear, valueToCheck, null);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.SEND_KEYS, parentList, parentIndex, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\" attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.PRESS, parentList, parentIndex, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the \"" + attribute + "\"attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.keyboardCommand.doTableCommandBasedOnAttributeValue(KeyboardAction.CLEAR, parentList, parentIndex, rowObjectList, rowObjectToCheckText, attribute, valueToCheck, rowObjectToClear, valueToCheck, null);
	}
	
	/********************** GET COMMANDS *********************/
	
	
	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_TEXT, locator, null);
		return text;
	}
	
	@Override
	public String getValue(By locator) {
		this.log.debug("I get value from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, locator, "value");
		return text;
	}
	
	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, locator, attribute);
		return text;
	}
	
	@Override
	public String getDropDownListValue(By locator) {
		this.log.debug("I get value from Drop-down List Web Element: \"" + locator.toString() + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_DROPDOWN, locator, null);
		return text;
	}
	
	@Override
	public String getText(By parent, By child) {
		this.log.debug("I get text from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_TEXT, parent, child, null);
		return text;
	}
	
	@Override
	public String getValue(By parent, By child) {
		this.log.debug("I get value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, parent, child, "value");
		return text;
	}
	
	@Override
	public String getAttributeValue(By parent, By child, String attribute) {
		this.log.debug("I get attribute value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, parent, child, attribute);
		return text;
	}
	
	@Override
	public String getDropDownListValue(By parent, By child) {
		this.log.debug("I get value from Drop-down List Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String text = this.getCommand.doBasicCommand(GetAction.GET_DROPDOWN, parent, child, null);
		return text;
	}
	
	@Override
	public String getText(By parent, int index, By child) {
		this.log.debug("I get text from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedText = this.getCommand.doBasicCommand(GetAction.GET_TEXT, parent, index, child,  null);
		return retrievedText;
	}
	
	@Override
	public String getValue(By parent, int index, By child) {
		this.log.debug("I get value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedValue = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, parent, index, child, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValue(By parent, int index, By child, String attribute) {
		this.log.debug("I get attribute value from Child Web Element: \"" + child.toString() + "\" within Parent Web Element: \"" + parent + "\".");
		String retrievedValue = this.getCommand.doBasicCommand(GetAction.GET_ATTRIBUTE, parent, index, child, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnText(GetAction.GET_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnText(GetAction.GET_TEXT, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnText(GetAction.GET_TEXT, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnText(GetAction.GET_ATTRIBUTE, parentList, parentIndex, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_TEXT, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, parent, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedText = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_TEXT, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		String retrievedValue = this.getCommand.doTableCommandBasedOnAttributeValue(GetAction.GET_ATTRIBUTE, parentList, parentIndex, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	
	/********************** SELECT COMMANDS *********************/
	
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

	/********************** GENERAL COMMANDS *********************/
	
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
		int size = this.generalCommand.count(locator);
		return size;
	}
	
	@Override
	public void wait(int duration) {
		this.log.debug("I wait for " + duration + " Second(s).");
		this.generalCommand.wait(duration);
	}
	
	/* ####################################################### */
	/*                        ASSERTIONS                       */
	/* ####################################################### */
	
	@Override
	public boolean seeUrl(String expectedUrl) {
		this.log.debug("I see Page URL: \"" + expectedUrl + "\".");
		this.seleniumWait.waitForUrlToBe(expectedUrl);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.URL, null, null, expectedUrl);
		return status;
	}
	
	@Override
	public boolean seeTitle(String expectedTitle) {
		this.log.debug("I see Page Title: \"" + expectedTitle + "\".");
		this.seleniumWait.waitForTitleToBe(expectedTitle);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.TITLE, null, null, expectedTitle);
		return status;
	}
	
	@Override
	public boolean typed(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is typed on Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		this.seleniumWait.waitForAttributeValueToBe(element, "value", expectedValue);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.ATTRIBUTE, element, "value", expectedValue);
		return status;	
	}
	
	@Override
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		this.seleniumWait.waitForAttributeValueToBe(element, attribute, expectedValue);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.ATTRIBUTE, element, attribute, expectedValue);
		return status;	
	}
	
	@Override
	public boolean selectedDropDown(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is selected at Drop-down List Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.DROPDOWN, element, null, expectedValue);
		return status;	
	}
	
	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		this.seleniumWait.waitForTextToBe(element, expectedValue);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.TEXT, element, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeUrl(String url) {
		this.log.debug("I see Page URL is not \"" + url + "\".");
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.URL, null, null, url);
		return status;
	}
	
	@Override
	public boolean dontSeeTitle(String title) {
		this.log.debug("I see Page Title is not \"" + title + "\".");
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.TITLE, null, null, title);
		return status;
	}
	
	@Override
	public boolean didntType(By locator, String value) {
		this.log.debug("I see \"" + value + "\" is not typed on Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.ATTRIBUTE, element, "value", value);
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.ATTRIBUTE, element, attribute, value);
		return status;	
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.TEXT, element, null, value);
		return status;	
	}
	
	@Override
	public boolean seePartialUrl(String expectedPartialUrl) {
		this.log.debug("I see partial Page URL: \"" + expectedPartialUrl + "\".");
		this.seleniumWait.waitForUrlToContain(expectedPartialUrl);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.PARTIAL_URL, null, null, expectedPartialUrl);
		return status;
	}
	
	@Override
	public boolean seePartialTitle(String expectedPartialTitle) {
		this.log.debug("I see partial Page Title: \"" + expectedPartialTitle + "\".");
		this.seleniumWait.waitForTitleToContain(expectedPartialTitle);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.PARTIAL_TITLE, null, null, expectedPartialTitle);
		return status;
	}
	
	@Override
	public boolean partialTyped(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" is partially typed on Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, element, "value", expectedPartialValue);
		return status;	
	}
	
	@Override
	public boolean seePartialAttributeValue(By locator, String attribute, String expectedPartialAttributeValue) {
		this.log.debug("I see \"" + expectedPartialAttributeValue + "\" as the partial value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		this.seleniumWait.waitForAttributeValueToContain(element, attribute, expectedPartialAttributeValue);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, element, attribute, expectedPartialAttributeValue);
		return status;	
	}
	
	@Override
	public boolean seePartialText(By locator, String expectedPartialText) {
		this.log.debug("I see \"" + expectedPartialText + "\" as the partial text value of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		this.seleniumWait.waitForTextToContain(element, expectedPartialText);
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.PARTIAL_TEXT, element, null, expectedPartialText);
		return status;
	}
	
	@Override
	public boolean dontSeePartialUrl(String partialUrl) {
		this.log.debug("I see partial Page URL is not \"" + partialUrl + "\".");
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.PARTIAL_URL, null, null, partialUrl);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTitle(String partialTitle) {
		this.log.debug("I see partial Page Title is not \"" + partialTitle + "\".");
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.PARTIAL_TITLE, null, null, partialTitle);
		return status;
	}
	
	@Override
	public boolean didntPartiallyType(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" is not partially typed on Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, element, "value", partialValue);
		return status;	
	}
	
	@Override
	public boolean dontSeePartialAttributeValue(By locator, String attribute, String partialAttributeValue) {
		this.log.debug("I see \"" + partialAttributeValue + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, element, attribute, partialAttributeValue);
		return status;	
	}
	
	@Override
	public boolean dontSeePartialText(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as not the partial text value of Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.valueAssertionCommand.isNotEqual(ValueAssertionAction.PARTIAL_TEXT, element, null, partialValue);
		return status;	
	}
	
	@Override
	public boolean seeTextOfListElement(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListEqual(ValueAssertionAction.TEXT, locator, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfListElement(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListEqual(ValueAssertionAction.PARTIAL_TEXT, locator, null, expectedPartialValue);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfListElement(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the attribute value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListEqual(ValueAssertionAction.ATTRIBUTE, locator, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfListElement(By locator, String attribute, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial attribute value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, locator, attribute, expectedPartialValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfListElement(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListNotEqual(ValueAssertionAction.TEXT, locator, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfListElement(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListNotEqual(ValueAssertionAction.PARTIAL_TEXT, locator, null, partialValue);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfListElement(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as the attribute value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListNotEqual(ValueAssertionAction.ATTRIBUTE, locator, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfListElement(By locator, String attribute, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as the partial attribute value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		boolean status = this.valueAssertionCommand.isListNotEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, locator, null, partialValue);
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableEqual(ValueAssertionAction.TEXT, rowObjectList, rowObjectToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableEqual(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToSeePartialTextFrom, null, expectedPartialValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableNotEqual(ValueAssertionAction.TEXT, rowObjectList, rowObjectToSeeTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableNotEqual(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToSeePartialTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the attribute value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableEqual(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String attribute, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial attribute value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToSeePartialTextFrom, attribute, expectedPartialValue);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as the attribute value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableNotEqual(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToSeeTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as the partial attribute value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean status = this.valueAssertionCommand.isTableNotEqual(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToSeePartialTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnText(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnText(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnText(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnText(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnText(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnText(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnText(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnText(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnText(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnText(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnText(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnText(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnText(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnText(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnText(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnText(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnAttributeValue(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnAttributeValue(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnAttributeValue(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, null, value);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnAttributeValue(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isTableNotEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, value);
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnAttributeValue(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean seeAttributeValueOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnAttributeValue(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean seePartialAttributeValueOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnAttributeValue(ValueAssertionAction.TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_TEXT, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, null, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeeAttributeValueOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnAttributeValue(ValueAssertionAction.ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean dontSeePartialAttributeValueOfTableRowListElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial attribute value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.valueAssertionCommand.isListTableNotEqualBasedOnAttributeValue(ValueAssertionAction.PARTIAL_ATTRIBUTE, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		return status;
	}
	
	@Override
	public boolean see(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" is displayed.");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.DISPLAYED, element, 0);
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" is not displayed.");
		int size = this.count(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.NOT_DISPLAYED, null, size);
		return status;
	}
	
	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" is enabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.ENABLED, element, 0);
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" is disabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.DISABLED, element, 0);
		return status;
	}

	@Override
	public boolean selected(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" is selected.");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.SELECTED, element, 0);
		return status;
	}

	@Override
	public boolean deselected(By locator) {
		this.log.debug("I see element \"" + locator.toString() + "\" is deselected.");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean status = this.stateAssertionCommand.isElementStateEqual(StateAssertionAction.DESELECTED, element, 0);
		return status;
	}
	
	@Override
	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" is displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.DISPLAYED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to not be displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.NOT_DISPLAYED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeEnabledOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to be enabled within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.ENABLED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeDisabledOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to be disabled within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.DISABLED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeSelectedOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to be selected within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.SELECTED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeDeselectedOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to be deselected within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnText(StateAssertionAction.DESELECTED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" is displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.DISPLAYED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean dontSeeTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to not be displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.NOT_DISPLAYED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeEnabledOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" si enabled within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.ENABLED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeDisabledOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" is disabled within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.DISABLED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeSelectedOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" is selected within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.SELECTED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean seeDeselectedOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" is deselected within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the " + attributeToCheck + " attribute value: \"" + valueToCheck + "\" from the Web Element: \"" + rowObjectToCheckAttributeValue.toString() + "\" within the same row.");
		boolean status = this.stateAssertionCommand.isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction.DESELECTED, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSee);
		return status;
	}
	
	@Override
	public boolean counted(By locator, int count) {
		this.log.debug("I count Web Element: \"" + locator.toString() + "\".");
		boolean status = this.generalCommand.counted(locator, count);
		return status;
	}
	
	@Override
	public boolean seeAlertMessage(String expectedMessage) {
		this.log.debug("I see \"" + expectedMessage + "\" Alert Message displayed.");
		boolean status = this.valueAssertionCommand.isEqual(ValueAssertionAction.ALERT_MESSAGE, null, null, expectedMessage);
		return status;
	}

}