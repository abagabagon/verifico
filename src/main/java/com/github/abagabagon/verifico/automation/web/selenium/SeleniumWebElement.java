package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumWebElement {

	private Logger log;
	private SeleniumWait seleniumWait;
	
	public SeleniumWebElement(WebDriver driver, SeleniumWait seleniumWait) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWait = seleniumWait;
	}
	
	enum ValueCheckType {
		TEXT, ATTRIBUTE
	}
	
	int getListIndex(ValueCheckType valueCheckType, List<WebElement> elements, String attribute, String searchValue) {
		int size = elements.size();
		int index = 0;
		boolean flgTextFound = false;
		boolean status = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			WebElement element = elements.get(i);
			switch(valueCheckType) {
			case TEXT:
				retrievedValue = element.getText().trim();
				status = retrievedValue.contains(searchValue);
				break;
			case ATTRIBUTE:
				retrievedValue = element.getAttribute(attribute).trim();
				status = retrievedValue.contains(searchValue);
				break;
			default:
				this.log.fatal("Unsupported Value Check Type.");
			}
			if (status) {
				flgTextFound = true;
				index = i;
				this.log.debug("I saw \"" + searchValue + "\" " + valueCheckType + " value at index " + index + " from the Web Element List: \"" + elements.toString() + "\".");
				break;
			}
		}
		if (!flgTextFound) {
			index = 999999;
			this.log.error("I didn't see \"" + searchValue + "\" as the " + valueCheckType + " value of one of the Web Elements from the Web Element List: \"" + elements.toString() + "\".");
		}
		return index;
	}
	
	WebElement getElement(By locator) {
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		return element;
	}
	
	WebElement getElementFromParentElement(By parent, By child) {
		WebElement parentElement = this.getElement(parent);
		WebElement childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
		return childElement;
	}
	
	WebElement getElementFromParentElement(WebElement parent, By child) {
		WebElement childElement = this.seleniumWait.waitForNestedObjectToBePresent(parent, child);
		return childElement;
	}
	
	WebElement getElementFromListElementBasedOnIndex(By objectList, int index) {
		List<WebElement> elements = this.getListElement(objectList);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(elements.get(index));
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnIndex(By parent, By childList, int index) {
		this.seleniumWait.waitForObjectsToBeVisible(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(elements.get(index));
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnIndex(WebElement parent, By childList, int index) {
		this.seleniumWait.waitForObjectsToBeVisible(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(elements.get(index));
		return element;
	}
	
	WebElement getElementFromListElementBasedOnText(By objectList, String textToCheck) {
		WebElement element = null;
		List<WebElement> elements = this.getListElement(objectList);
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		element = this.getElementFromListElementBasedOnIndex(objectList, index);
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnText(By parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		WebElement element = null;
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnText(WebElement parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		WebElement element = null;
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return element;
	}
	
	WebElement getElementFromListElementBasedOnAttributeValue(By objectList, String attribute, String textToCheck) {
		WebElement element = null;
		List<WebElement> elements = this.getListElement(objectList);
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		element = this.getElementFromListElementBasedOnIndex(objectList, index);
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnAttributeValue(By parent, By childList, String attribute, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		WebElement element = null;
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return element;
	}
	
	WebElement getElementFromListElementOfParentElementBasedOnAttributeValue(WebElement parent, By childList, String attribute, String textToCheck) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		WebElement element = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return element;
	}
	
	WebElement getElementFromParentElementBasedOnTextOfAnotherElement(By parent, By childListToCheckText, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckText);
		WebElement element = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return element;
	}
	
	WebElement getElementFromParentElementBasedOnTextOfAnotherElement(WebElement parent, By childListToCheckText, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckText);
		WebElement element = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return element;
	}
	
	WebElement getElementFromParentElementBasedOnAttributeValueOfAnotherElement(By parent, By childListToCheckAttribute, String attribute, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckAttribute);
		WebElement element = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return element;
	}
	
	WebElement getElementFromParentElementBasedOnAttributeValueOfAnotherElement(WebElement parent, By childListToCheckAttribute, String attribute, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckAttribute);
		WebElement element = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		element = this.getElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return element;
	}
	
	List<WebElement> getListElement(By locator) {
		List<WebElement> elements = this.seleniumWait.waitForListElement(locator);
		return elements;
	}
	
	List<WebElement> getListElementFromParentElement(By parent, By childList) {
		WebElement parentElement = this.getElement(parent);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElement, childList);
		return elements;
	}
	
	List<WebElement> getListElementFromParentElement(WebElement parent, By childList) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
		return elements;
	}
	
	List<WebElement> getListElementFromListElementBasedOnIndex(By objectList, int index) {
		List<WebElement> elements = this.getListElement(objectList);
		List<WebElement> elementsToGet = this.seleniumWait.waitForObjectsToBeVisible(elements.get(index));
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnIndex(By parent, By childList, int index) {
		this.seleniumWait.waitForObjectsToBeVisible(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = this.seleniumWait.waitForObjectsToBeVisible(elements.get(index));
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnIndex(WebElement parent, By childList, int index) {
		this.seleniumWait.waitForObjectsToBeVisible(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = this.seleniumWait.waitForObjectsToBeVisible(elements.get(index));
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementBasedOnText(By objectList, String textToCheck) {
		List<WebElement> elementToGet = null;
		List<WebElement> elements = this.getListElement(objectList);
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		elementToGet = this.getListElementFromListElementBasedOnIndex(objectList, index);
		return elementToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnText(By parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnText(WebElement parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementBasedOnAttributeValue(By objectList, String attribute, String textToCheck) {
		List<WebElement> elementToGet = null;
		List<WebElement> elements = this.getListElement(objectList);
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		elementToGet = this.getListElementFromListElementBasedOnIndex(objectList, index);
		return elementToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnAttributeValue(By parent, By childList, String attribute, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromListElementOfParentElementBasedOnAttributeValue(WebElement parent, By childList, String attribute, String textToCheck) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childList);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childList, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromParentElementBasedOnTextOfAnotherElement(By parent, By childListToCheckText, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckText);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromParentElementBasedOnTextOfAnotherElement(WebElement parent, By childListToCheckText, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckText);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.TEXT, elements, "", textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromParentElementBasedOnAttributeValueOfAnotherElement(By parent, By childListToCheckAttribute, String attribute, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckAttribute);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return elementsToGet;
	}
	
	List<WebElement> getListElementFromParentElementBasedOnAttributeValueOfAnotherElement(WebElement parent, By childListToCheckAttribute, String attribute, String textToCheck, By childListToCreateElementFrom) {
		List<WebElement> elements = this.getListElementFromParentElement(parent, childListToCheckAttribute);
		List<WebElement> elementsToGet = null;
		int index = this.getListIndex(ValueCheckType.ATTRIBUTE, elements, attribute, textToCheck);
		elementsToGet = this.getListElementFromListElementOfParentElementBasedOnIndex(parent, childListToCreateElementFrom, index);
		return elementsToGet;
	}
	
}