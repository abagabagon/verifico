package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumStateAssertionCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private SeleniumWait seleniumWait;
	
	public SeleniumStateAssertionCommands(WebDriver driver, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}
	
	private boolean isElementStateSetAs(StateAssertionAction stateAssertionAction, WebElement element, int size) {
		boolean status = false;
		switch(stateAssertionAction) {
		case DISPLAYED:
			status = element.isDisplayed();
			break;
		case NOT_DISPLAYED:
			if(size == 0) {
				status = true;
			}
			break;
		case ENABLED:
			status = element.isEnabled();
			break;
		case DISABLED:
			status = element.isEnabled();
			break;
		case SELECTED:
			this.seleniumWait.waitForObjectSelectionStateToBe(element, true);
			status = element.isSelected();
			break;
		case DESELECTED:
			status = this.seleniumWait.waitForObjectSelectionStateToBe(element, false);
			break;
		default:
			this.log.fatal("Unsupported Assertion Action.");
		}
		return status;
	}
	
	boolean isElementStateEqual(StateAssertionAction stateAssertionAction, WebElement element, int size) {
		boolean status = this.isElementStateSetAs(stateAssertionAction, element, size);
		if(status) {
			if (stateAssertionAction == StateAssertionAction.NOT_DISPLAYED) {
				this.log.debug("I saw state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.debug("I saw state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
		} else {
			if (stateAssertionAction == StateAssertionAction.NOT_DISPLAYED) {
				this.log.error("I saw state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.error("I saw state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
			
		}
		return status;
	}
	
	public boolean isElementStateSetAsFromNestedListBasedOnText(StateAssertionAction stateAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToCheckStateFrom) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, i);
			String checkText = elementToCheckText.getText().trim();
			if (checkText.contains(textToCheck)) {
				WebElement elementToCheckStateFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckStateFrom, i);
				List<WebElement> elementsToCheckStateFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectToCheckStateFrom, i);
				status = this.isElementStateSetAs(stateAssertionAction, elementToCheckStateFrom, elementsToCheckStateFrom.size());
				break;	
			}
		}
		return status;
	}
	
	boolean isNestedElementStateEqualBasedOnText(StateAssertionAction stateAssertionAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		boolean status = this.isElementStateSetAsFromNestedListBasedOnText(StateAssertionAction.DISPLAYED, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToCheckText);
		if(status) {
			this.log.debug("I saw state of the Web Element: \"" + rowObjectToSee.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
		} else {
			this.log.error("I saw state of the Web Element: \"" + rowObjectToSee.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
		}
		return status;
	}
	
	public boolean isElementStateSetAsFromNestedListBasedOnAttributeValue(StateAssertionAction stateAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToCheckStateFrom) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean status = false;
		for(int i = 0; i < size; i++) {
			WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, i);
			String checkText = elementToCheckText.getText().trim();
			if (checkText.contains(valueToCheck)) {
				WebElement elementToCheckStateFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckStateFrom, i);
				List<WebElement> elementsToCheckStateFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectToCheckStateFrom, i);
				status = this.isElementStateSetAs(stateAssertionAction, elementToCheckStateFrom, elementsToCheckStateFrom.size());
				break;	
			}
		}
		return status;
	}
	
	boolean isNestedElementStateEqualBasedOnAttributeValue(StateAssertionAction stateAssertionAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToSee) {
		boolean status = this.isElementStateSetAsFromNestedListBasedOnAttributeValue(stateAssertionAction, rowObjectList, rowObjectToCheckAttributeValue, attributeToCheck, valueToCheck, rowObjectToCheckAttributeValue);
		if(status) {
			this.log.debug("I saw state of the Web Element: \"" + rowObjectToSee.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
		} else {
			this.log.error("I saw state of the Web Element: \"" + rowObjectToSee.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
		}
		return status;
	}
	
}