package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ValueAssertions extends Commands {

	protected WebDriver driver;
	protected Logger log;
	private WaitCommands wait;
	private String actualValue;
	
	private enum ValueAssertionAction {
		URL, PARTIAL_URL, TITLE, PARTIAL_TITLE, ATTRIBUTE, PARTIAL_ATTRIBUTE, DROPDOWN, PARTIAL_DROPDOWN, TEXT, PARTIAL_TEXT, ALERT_MESSAGE
	}
	
	public ValueAssertions(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
	}
	
	private boolean isValueDisplayed(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = false;
		Select select = null;
		switch(valueAssertionAction) {
		case URL:
			this.wait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.equals(value);
			break;
		case TITLE:
			this.wait.waitForPage();
			this.actualValue = this.driver.getTitle().trim();
			status = this.actualValue.equals(value);
			break;
		case ATTRIBUTE:
			this.actualValue = element.getAttribute(attribute);
			if (this.actualValue != null) {
				status = this.actualValue.equals(value);
			} else {
				status = false;
			}
			break;
		case DROPDOWN:
			select = new Select(element);
			this.actualValue = select.getFirstSelectedOption().getText();
			status = this.actualValue.equals(value);
			break;
		case TEXT:
			this.actualValue = element.getText();
			status = this.actualValue.equals(value);
			break;
		case PARTIAL_URL:
			this.wait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_TITLE:
			this.wait.waitForPage();
			this.actualValue = this.driver.getTitle().trim();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_ATTRIBUTE:
			this.actualValue = element.getAttribute(attribute);
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_DROPDOWN:
			select = new Select(element);
			this.actualValue = select.getFirstSelectedOption().getText().toLowerCase();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_TEXT:
			this.actualValue = element.getText();
			status = this.actualValue.contains(value);
			break;
		case ALERT_MESSAGE:
			Alert alert = this.wait.waitForAlertToBePresent();
			this.actualValue = alert.getText().trim();
			status = this.actualValue.equals(value);
		default:
			this.log.fatal("Unsupported Value Assertion Action.");
		}
		return status;
	}
	
	private boolean isEqual(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String expectedValue) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, expectedValue);
		if(status) {
			this.log.debug("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\".");
		} else {
			this.log.error("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\". Actual value is \"" + this.actualValue + "\".");
		}
		
		return status;
	}
	
	private boolean isNotEqual(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, value);
		if(status) {
			this.log.error("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\".");
		} else {
			this.log.debug("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\". Actual value is \"" + this.actualValue + "\".");
		}
		
		return !status;
	}
	
	
	public boolean counted(By locator, int count) {
		boolean isEqual = this.wait.waitForElementCountToBe(locator, count);
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
	
}