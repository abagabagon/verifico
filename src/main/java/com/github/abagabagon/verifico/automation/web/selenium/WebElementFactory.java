package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebElementFactory {

	private Logger log;
	private WaitCommands wait;
	
	public WebElementFactory(WebDriver driver, WaitCommands wait) {
		this.log = LogManager.getLogger(this.getClass());
		this.wait = wait;
	}
	
	private enum ValueCheckType {
		TEXT, ATTRIBUTE
	}
	
	private int getListIndex(ValueCheckType valueCheckType, List<WebElement> elements, String attribute, String searchValue) {
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
	
	public final WebElement createElement(By locator) {
		WebElement element = this.wait.waitForElementToBePresent(locator);
		return element;
	}
	
	public final WebElement createElement(By parent, By child) {
		WebElement nestedElement = this.wait.waitForNestedElementToBePresent(parent, child);
		return nestedElement;
	}
	
	public final WebElement createElement(WebElement parent, By child) {
		WebElement nestedElement = this.wait.waitForNestedElementToBePresent(parent, child);
		return nestedElement;
	}
	
	public final List<WebElement> createListElement(By locator) {
		List<WebElement> elements = this.wait.waitForListElementToBePresent(locator);
		return elements;
	}
	
	public final List<WebElement> createListElement(By parent, By child) {
		List<WebElement> nestedElement = this.wait.waitForNestedListElementToBePresent(parent, child);
		return nestedElement;
	}
	
	public final List<WebElement> createListElement(WebElement parent, By child) {
		List<WebElement> nestedElement = this.wait.waitForNestedListElementToBeVisible(parent, child);
		return nestedElement;
	}
	
}