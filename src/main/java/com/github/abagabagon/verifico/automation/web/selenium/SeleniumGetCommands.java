package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

public class SeleniumGetCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private SeleniumWait seleniumWait;
	private String retrievedValue;
	
	public SeleniumGetCommands(WebDriver driver, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}
	
	boolean execute(GetAction getAction, WebElement element, String attribute) {
		boolean actionPerformed = false;
		this.retrievedValue = null;
		Select select = null;
		try {
			switch(getAction) {
			case GET_ATTRIBUTE:
				retrievedValue = element.getAttribute(attribute);
				break;
			case GET_DROPDOWN:
				select = new Select(element);
				this.retrievedValue = select.getFirstSelectedOption().getText().toLowerCase();
				break;
			case GET_TEXT:
				this.retrievedValue = element.getText().trim();
				break;
			default:
				this.log.fatal("Unsupported User Action.");
			}
			actionPerformed = true;
		} catch (NullPointerException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + element.toString() + "\". Element created is NULL.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (UnexpectedTagNameException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + element.toString() + "\". Element does not have a SELECT Tag.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + element.toString() + "\".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return actionPerformed;
	}
	
	String doBasicCommand(GetAction getAction, By locator, String attribute) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.seleniumWait.waitForObjectToBePresent(locator);
			actionPerformed = this.execute(getAction, element, attribute);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return retrievedValue;
	}
	
	String doBasicCommand(GetAction getAction, By parent, By child, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(getAction, childElement, attribute);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return retrievedValue;
	}
	
	String doBasicCommand(GetAction getAction, By parentList, int index, By child, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectsToBeVisible(parentList).get(index);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(getAction, childElement, attribute);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return retrievedValue;
	}
	
	String doTableCommand(GetAction getAction, By parent, By rowObjectList, int index, By child, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(index);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(getAction, childElement, attribute);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return this.retrievedValue;
	}
	
	String doTableCommand(GetAction getAction, By parentList, int parentIndex, By rowObjectList, int rowIndex, By child, String attribute) {
		boolean actionPerformed = false;
		List<WebElement> parentElementList = null;
		List<WebElement> rowChildElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
			rowChildElement = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(rowChildElement.get(rowIndex), child);
			this.seleniumWait.waitForObjectToBeVisible(childElement);
			actionPerformed = this.execute(getAction, childElement, attribute);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return this.retrievedValue;
	}
	
	String doTableCommandBasedOnText(GetAction getAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		List<WebElement> rows = this.seleniumWait.waitForListElement(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					value = this.doBasicCommand(getAction, rowObjectList, j, rowObjectToDoActionTo, attribute);
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
		return value;
	}
	
	String doTableCommandBasedOnText(GetAction getAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					value = this.doTableCommand(getAction, parent, rowObjectList, j, rowObjectToDoActionTo, attribute);
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
		return value;
	}
	
	String doTableCommandBasedOnText(GetAction getAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForListElement(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					value = this.doTableCommand(getAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo, attribute);
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
		return value;
	}
	
	String doTableCommandBasedOnAttributeValue(GetAction getAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToDoActionTo, String attribute) {
		List<WebElement> rows = this.seleniumWait.waitForListElement(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attributeToCheck).trim();
				if (text.contains(valueToCheck)) {
					value = this.doBasicCommand(getAction, rowObjectList, j, rowObjectToDoActionTo, attribute);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	String doTableCommandBasedOnAttributeValue(GetAction getAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					value = this.doTableCommand(getAction, parent, rowObjectList, j, rowObjectToDoActionTo, attribute);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	String doTableCommandBasedOnAttributeValue(GetAction getAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					value = this.doTableCommand(getAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo, attribute);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}

}