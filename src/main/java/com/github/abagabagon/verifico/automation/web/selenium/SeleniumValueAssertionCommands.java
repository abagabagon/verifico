package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SeleniumValueAssertionCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private SeleniumWait seleniumWait;
	private String actualValue;
	
	public SeleniumValueAssertionCommands(WebDriver driver, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}
	
	private boolean isValueDisplayed(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = false;
		Select select = null;
		switch(valueAssertionAction) {
		case URL:
			this.seleniumWait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.equals(value);
			break;
		case TITLE:
			this.seleniumWait.waitForPage();
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
			this.seleniumWait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_TITLE:
			this.seleniumWait.waitForPage();
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
			Alert alert = this.seleniumWait.waitForAlertToBePresent();
			this.actualValue = alert.getText().trim();
			status = this.actualValue.equals(value);
		default:
			this.log.fatal("Unsupported Value Assertion Action.");
		}
		return status;
	}
	
	boolean isEqual(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String expectedValue) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, expectedValue);
		if(status) {
			this.log.debug("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\".");
		} else {
			this.log.error("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\". Actual value is \"" + this.actualValue + "\".");
		}
		
		return status;
	}
	
	boolean isNotEqual(ValueAssertionAction valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, value);
		if(status) {
			this.log.error("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\".");
		} else {
			this.log.debug("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\". Actual value is \"" + this.actualValue + "\".");
		}
		
		return !status;
	}
	
	private boolean isValueDisplayedFromList(ValueAssertionAction valueAssertionAction, By locator, String attribute, String searchValue) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, i);
			status = this.isValueDisplayed(valueAssertionAction, element, attribute, searchValue);
			if (status) {
				break;
			}
		}
		return status;
	}
	
	boolean isListEqual(ValueAssertionAction valueAssertionAction, By locator, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromList(valueAssertionAction, locator, attribute, searchValue);
		if(isFound) {
			this.log.debug("I saw \"" + searchValue + "\" " + String.valueOf(valueAssertionAction) + " value from one of the Elements of Web Element List: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isListNotEqual(ValueAssertionAction valueAssertionAction, By locator, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromList(valueAssertionAction, locator, attribute, searchValue);
		if(isFound) {
			this.log.error("I saw \"" + searchValue + "\" " + String.valueOf(valueAssertionAction) + " value from one of the Elements of Web Element List: \"" + locator.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		}
		return !isFound;
	}
	
	private boolean isValueDisplayedFromNestedList(ValueAssertionAction valueAssertionAction, By objectList, By child, String attribute, String searchValue) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(objectList);
		int size = rows.size();
		boolean status = false;
		for(int j = 0; j < size; j++) {
			WebElement element = this.seleniumWait.waitForNestedObjectToBeVisible(objectList, child, j);
			status = this.isValueDisplayed(valueAssertionAction, element, attribute, searchValue);
			if (status) {
				break;
			}
		}
		return status;
	}
	
	boolean isTableEqual(ValueAssertionAction valueAssertionAction, By objectList, By child, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedList(valueAssertionAction, objectList, child, attribute, searchValue);
		if(isFound) {
			this.log.debug("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + child.toString() + "\" within one of the Rows of the Web Element: \"" + objectList.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + child.toString() + "\" in one of the Rows of the Web Element: \"" + objectList.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isTableNotEqual(ValueAssertionAction valueAssertionAction, By objectList, By child, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedList(valueAssertionAction, objectList, child, attribute, searchValue);
		if(isFound) {
			this.log.error("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + child.toString() + "\" within one of the Rows of the Web Element: \"" + objectList.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + child.toString() + "\" in one of the Rows of the Web Element: \"" + objectList.toString() + "\".");
		}
		return !isFound;
	}
	
	public boolean isValueDisplayedFromNestedListBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, i);
			String checkText = elementToCheckText.getText().trim();
			if (checkText.contains(textToCheck)) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, i);
				status = this.isValueDisplayed(valueAssertionAction, elementToSeeTextFrom, attribute, searchValue);
				break;	
			}
		}
		return status;
	}
	
	boolean isTableEqualBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedListBasedOnText(valueAssertionAction, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, searchValue);
		if(isFound) {
			this.log.debug("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isTableNotEqualBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedListBasedOnText(valueAssertionAction, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToSeeTextFrom, attribute, searchValue);
		if(isFound) {
			this.log.error("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return !isFound;
	}
	
	public boolean isValueDisplayedFromNestedListBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, i);
			String checkText = elementToCheckText.getAttribute(attributeToCheck).trim();
			if (checkText != null && checkText.contains(valueToCheck)) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, i);
				status = this.isValueDisplayed(valueAssertionAction, elementToSeeTextFrom, attribute, searchValue);
				break;	
			}
		}
		return status;
	}
	
	boolean isTableEqualBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedListBasedOnAttributeValue(valueAssertionAction, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, searchValue);
		if(isFound) {
			this.log.debug("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isTableNotEqualBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSeeTextFrom, String attribute, String searchValue) {
		boolean isFound = this.isValueDisplayedFromNestedListBasedOnAttributeValue(valueAssertionAction, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToSeeTextFrom, attribute, searchValue);
		if(isFound) {
			this.log.error("I saw \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + searchValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return !isFound;
	}
	
	private boolean isListValueDisplayedFromNestedListBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, i);
			String checkText = elementToCheckText.getText().trim();
			if (checkText.contains(textToCheck)) {
				List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, i);
				int listSize = elementToSeeTextFrom.size();
				for(int j = 0; j < listSize; j++) {
					status = this.isValueDisplayed(valueAssertionAction, elementToSeeTextFrom.get(j), attribute, expectedValue);
					if(status) {
						break;
					}
				}
				break;
			}
		}
		return status;
	}
	
	boolean isListTableEqualBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		boolean isFound = this.isListValueDisplayedFromNestedListBasedOnText(valueAssertionAction, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, textToCheck, expectedValue);
		if(isFound) {
			this.log.debug("I saw \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isListTableNotEqualBasedOnText(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		boolean isFound = this.isListValueDisplayedFromNestedListBasedOnText(valueAssertionAction, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectListToSeeTextFrom, textToCheck, expectedValue);
		if(isFound) {
			this.log.error("I saw \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return !isFound;
	}
	
	private boolean isListValueDisplayedFromNestedListBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, i);
			String checkText = elementToCheckText.getAttribute(attributeToCheck).trim();
			if (checkText.contains(valueToCheck)) {
				List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, i);
				int listSize = elementToSeeTextFrom.size();
				for(int j = 0; j < listSize; j++) {
					status = this.isValueDisplayed(valueAssertionAction, elementToSeeTextFrom.get(j), attribute, expectedValue);
					if(status) {
						break;
					}
				}
				break;
			}
		}
		return status;
	}
	
	boolean isListTableEqualBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		boolean isFound = this.isListValueDisplayedFromNestedListBasedOnAttributeValue(valueAssertionAction, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		if(isFound) {
			this.log.debug("I saw \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return isFound;
	}
	
	boolean isListTableNotEqualBasedOnAttributeValue(ValueAssertionAction valueAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectListToSeeTextFrom, String attribute, String expectedValue) {
		boolean isFound = this.isListValueDisplayedFromNestedListBasedOnAttributeValue(valueAssertionAction, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectListToSeeTextFrom, attribute, expectedValue);
		if(isFound) {
			this.log.error("I saw \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		} else {
			this.log.debug("I didn't see \"" + expectedValue + "\" as the " + String.valueOf(valueAssertionAction) + " value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		}
		return !isFound;
	}
	
}