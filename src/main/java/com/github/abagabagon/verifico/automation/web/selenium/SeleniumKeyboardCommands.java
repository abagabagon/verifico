package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.github.abagabagon.verifico.utilities.OperatingSystem;

public class SeleniumKeyboardCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private Actions action;
	private SeleniumWait seleniumWait;
	
	public SeleniumKeyboardCommands(WebDriver driver, Actions action, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.action = action;
		this.seleniumWait = seleniumWait;
	}
	
	boolean execute(KeyboardAction keyboardAction, WebElement element, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		try {
			switch(keyboardAction) {
			case CLEAR:
				Platform platform = OperatingSystem.getOS();
				if (platform == Platform.MAC) {
					this.action.click(element)
			        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
			        .pause(200).sendKeys(Keys.DELETE)
			        .perform();
				} else {
					this.action.click(element)
			        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
			        .pause(200).sendKeys(Keys.DELETE)
			        .perform();
				}
				break;
			case PRESS:
				element.sendKeys(keyButton);
				break;
			case SEND_KEYS:
				element.sendKeys(inputText);
				break;
			default:
				this.log.fatal("Unsupported User Action.");
			}
			actionPerformed = true;
		} catch (NullPointerException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\". Element created is NULL.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (ElementNotInteractableException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\". Element cannot be interacted with.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			element.click();
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			element.click();
		} catch (IllegalArgumentException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\". Input Text is NULL.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + element.toString() + "\".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return actionPerformed;
	}
	
	void doBasicCommand(KeyboardAction keyboardAction, By locator, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			actionPerformed = this.execute(keyboardAction, element, inputText, keyButton);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	void doBasicCommand(KeyboardAction keyboardAction, By parent, By child, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(keyboardAction, childElement, inputText, keyButton);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	void doBasicCommand(KeyboardAction keyboardAction, By parentList, int index, By child, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectsToBeVisible(parentList).get(index);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			this.seleniumWait.waitForObjectToBeVisible(childElement);
			actionPerformed = this.execute(keyboardAction, childElement, inputText, keyButton);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	
	void doTableCommand(KeyboardAction keyboardAction, By parent, By rowObjectList, int index, By child, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(index);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			this.seleniumWait.waitForObjectToBeVisible(childElement);
			actionPerformed = this.execute(keyboardAction, childElement, inputText, keyButton);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	void doTableCommand(KeyboardAction keyboardAction, By parentList, int parentIndex, By rowObjectList, int rowIndex, By child, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		List<WebElement> parentElementList = null;
		List<WebElement> rowChildElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
			rowChildElement = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(rowChildElement.get(rowIndex), child);
			this.seleniumWait.waitForObjectToBeVisible(childElement);
			actionPerformed = this.execute(keyboardAction, childElement, inputText, keyButton);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	
	void doTableCommandBasedOnText(KeyboardAction keyboardAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					this.doBasicCommand(keyboardAction, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}
	
	void doTableCommandBasedOnText(KeyboardAction keyboardAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					this.doTableCommand(keyboardAction, parent, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}
	
	void doTableCommandBasedOnText(KeyboardAction keyboardAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForTableRowsToBeVisible(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					this.doTableCommand(keyboardAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}
	
	void doTableCommandBasedOnAttributeValue(KeyboardAction keyboardAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doBasicCommand(keyboardAction, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}
	
	void doTableCommandBasedOnAttributeValue(KeyboardAction keyboardAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doTableCommand(keyboardAction, parent, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}
	
	void doTableCommandBasedOnAttributeValue(KeyboardAction keyboardAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doTableCommand(keyboardAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo, inputText, keyButton);
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
	}

}