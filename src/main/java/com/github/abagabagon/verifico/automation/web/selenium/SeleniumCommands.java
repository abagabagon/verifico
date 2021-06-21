package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumCommands {

	private Logger log;
	private WebDriver driver;
	private SeleniumWait seleniumWait;
	
	enum BrowserAction {
		OPEN_TAB, GO_TO, MAXIMIZE, DELETE_ALL_COOKIES, BACK, FORWARD, REFRESH, CLOSE_TAB, CLOSE_BROWSER
	}
	
	enum SwitchAction {
		BY_TITLE, BY_URL
	}
	
	enum MouseAction {
		CLICK, CLICKJS, CLICK_AND_HOLD, DOUBLE_CLICK, DRAG_AND_DROP, POINT
	}
	
	enum GetAction {
		GET_ATTRIBUTE, GET_DROPDOWN, GET_TEXT
	}
	
	enum KeyboardAction {
		CLEAR, PRESS, SEND_KEYS
	}
	
	enum SelectAction {
		DESELECT, SELECT
	}
	
	enum ValueAssertionAction {
		URL, PARTIAL_URL, TITLE, PARTIAL_TITLE, ATTRIBUTE, PARTIAL_ATTRIBUTE, DROPDOWN, PARTIAL_DROPDOWN, TEXT, PARTIAL_TEXT, ALERT_MESSAGE
	}
	
	enum StateAssertionAction {
		DISPLAYED, NOT_DISPLAYED, ENABLED, DISABLED, SELECTED, DESELECTED
	}
	
	public SeleniumCommands(WebDriver driver, SeleniumWait seleniumWait) {
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}
	
	protected int getListIndex(ValueAssertionAction valueAssertionAction, By locator, String attribute, String searchValue) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		int index = 0;
		boolean flgTextFound = false;
		boolean status = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, i);
			switch(valueAssertionAction) {
			case TEXT:
				retrievedValue = element.getText().trim();
				status = retrievedValue.equals(searchValue);
				break;
			case PARTIAL_TEXT:
				retrievedValue = element.getText().trim();
				status = retrievedValue.contains(searchValue);
				break;
			case ATTRIBUTE:
				retrievedValue = element.getAttribute(attribute).trim();
				status = retrievedValue.equals(searchValue);
				break;
			case PARTIAL_ATTRIBUTE:
				retrievedValue = element.getAttribute(attribute).trim();
				status = retrievedValue.contains(searchValue);
				break;
			default:
				this.log.fatal("Unsupported Get Index Action.");
			}
			if (status) {
				flgTextFound = true;
				index = i;
				this.log.debug("I saw \"" + searchValue + "\" " + valueAssertionAction + " value at index " + index + " from the Web Element List: \"" + locator.toString() + "\".");
				break;
			}
		}
		if (!flgTextFound) {
			index = 999999;
			this.log.error("I didn't see \"" + searchValue + "\" as the " + valueAssertionAction + " value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		}
		return index;
	}
	
	protected int count(By locator) {
		this.seleniumWait.waitForPage();
		this.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		List<WebElement> element = this.driver.findElements(locator);
		int size = element.size();
		this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return size;
	}
	
	protected boolean counted(By locator, int count) {
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
	
	protected void wait(int duration) {
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
	
}