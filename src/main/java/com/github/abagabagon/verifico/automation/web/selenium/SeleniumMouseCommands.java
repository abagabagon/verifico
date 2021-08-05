package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

public class SeleniumMouseCommands extends SeleniumCommands {

	private Logger log;
	private JavascriptExecutor javascriptExecutor;
	private Actions action;
	private SeleniumWait seleniumWait;
	
	public SeleniumMouseCommands(WebDriver driver, JavascriptExecutor javascriptExecutor, Actions action, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.javascriptExecutor = javascriptExecutor;
		this.action = action;
		this.seleniumWait = seleniumWait;
	}
	
	/**
	 * Implementation of Selenium Mouse Commands.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param element		Web Element to perform Mouse Action to.
	 * @return	<code>true</code> if able to perform Mouse Action.
	 * 			<code>false</code> if unable to perform Mouse Action.
	 */
	
	private boolean execute(MouseAction mouseAction, WebElement element) {
		boolean actionPerformed = false;
		try {
			switch(mouseAction) {
			case CLICK:
				element.click();
				break;
			case CLICKJS:
				this.javascriptExecutor.executeScript("arguments[0].click();", element);
				break;
			case CLICK_AND_HOLD:
				this.action.clickAndHold(element).perform();
				break;
			case DOUBLE_CLICK:
				this.action.doubleClick(element).perform();
				break;
			case POINT:
				String script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(element).perform();
				break;
			default:
				this.log.fatal("Unsupported User Action.");
			}
			actionPerformed = true;
		} catch (NullPointerException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\". Element created is NULL.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (ElementClickInterceptedException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is unclickable because it's not on view.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(element);
			String script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
			this.javascriptExecutor.executeScript(script);
			this.action.moveToElement(element).perform();
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (MoveTargetOutOfBoundsException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is out-of-bounds.");
			element = this.seleniumWait.waitForObjectToBeVisible(element);
			String script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
			this.javascriptExecutor.executeScript(script);
			this.action.moveToElement(element).perform();
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return actionPerformed;
	}
	
	/**
	 * Performs Mouse Commands on specified object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param locator		Object to perform Mouse Action to
	 */
	
	void doBasicCommand(MouseAction mouseAction, By locator) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			actionPerformed = this.execute(mouseAction, element);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on specified child object nested within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object to perform Mouse Action to
	 * @param child			Child Object to perform Mouse Action to
	 */
	
	void doBasicCommand(MouseAction mouseAction, By parent, By child) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(mouseAction, childElement);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on specified child object nested within a parent list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object would be located 
	 * @param child			Child Object to perform Mouse Action to
	 */
	
	void doBasicCommand(MouseAction mouseAction, By parentList, int parentIndex, By child) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectsToBeVisible(parentList).get(parentIndex);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(mouseAction, childElement);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param index			Index of Object from Object List to perform Mouse Action to
	 */
	
	void doListCommand(MouseAction mouseAction, By objectList, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
			element = this.seleniumWait.waitForObjectToBeVisible(elements.get(index));
			actionPerformed = this.execute(mouseAction, element);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param index			Index of Child Object from Child Object List to perform Mouse Action to
	 */
	
	void doListCommand(MouseAction mouseAction, By parent, By childList, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			this.seleniumWait.waitForObjectsToBeVisible(childList);
			elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
			element = this.seleniumWait.waitForObjectToBeVisible(elements.get(index));
			actionPerformed = this.execute(mouseAction, element);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object within a parent list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child List Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object List would be located 
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param childIndex	Index of Child Object from Child Object List to perform Mouse Action to
	 */
	
	void doListCommand(MouseAction mouseAction, By parentList, int parentIndex, By childList, int childIndex) {
		boolean actionPerformed = false;
		List<WebElement> parentElementList = null;
		List<WebElement> childElementList = null;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
			this.seleniumWait.waitForObjectsToBeVisible(childList);
			childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
			this.seleniumWait.waitForObjectToBeVisible(childElementList.get(childIndex));
			actionPerformed = this.execute(mouseAction, element);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on text.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By objectList, String textToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListElement(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					this.doListCommand(mouseAction, objectList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on text within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Child Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					this.doListCommand(mouseAction, parent, childList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on text within a parent list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child List Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object List would be located 
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Child Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By parentList, int parentIndex, By childList, String textToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
		int size = childElementList.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = childElementList.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					this.doListCommand(mouseAction, parentList, parentIndex, childList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on attribute value.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By objectList, String attribute, String valueToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListElement(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doListCommand(mouseAction, objectList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on attribute value within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By parent, By childList, String attribute, String valueToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doListCommand(mouseAction, parent, childList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified list object based on attribute value within a parent list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child List Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object List would be located
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By parentList, int parentIndex, By childList, String attribute, String valueToCheck) {
		this.seleniumWait.waitForListElement(childList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
		int size = childElementList.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = childElementList.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doListCommand(mouseAction, parentList, childList, j);
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + attribute + "\" attribute value \"" + valueToCheck + "\" from the Web Element List: \"" +  childList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified table object within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object to perform Mouse Action to
	 * @param rowObjectList	Child Row Object List corresponding to the rows of the table
	 * @param rowIndex		Row Object List Index from which the Mouse Action will be performed 
	 * @param child			Child Object in relation to the Row Object List to perform Mouse Action to
	 */
	
	void doTableCommand(MouseAction mouseAction, By parent, By rowObjectList, int rowIndex, By child) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(rowIndex);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
			actionPerformed = this.execute(mouseAction, childElement);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified table object within a parent list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child Object to perform Mouse Action to
	 * @param parentIndex	Parent List Index from which the Mouse Action will be performed
	 * @param rowObjectList	Child Row Object List corresponding to the rows of the table
	 * @param rowIndex		Row Object List Index from which the Mouse Action will be performed 
	 * @param child			Child Object in relation to the Row Object List to perform Mouse Action to
	 */
	
	void doTableCommand(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, int rowIndex, By child) {
		boolean actionPerformed = false;
		List<WebElement> parentElementList = null;
		List<WebElement> rowElementList = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
			rowElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(rowElementList.get(rowIndex), child);
			actionPerformed = this.execute(mouseAction, childElement);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs Mouse Commands on a specified table object based on text value within a table row list object.
	 * 
	 * @param mouseAction			Mouse Action to perform
	 * @param rowObjectList			Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckText	Object relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo	Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		List<WebElement> rows = this.seleniumWait.waitForListElement(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					this.doBasicCommand(mouseAction, rowObjectList, j, rowObjectToDoActionTo);
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
	
	/**
	 * Performs Mouse Commands on a specified table object based on text value within a table row list object within a parent object.
	 * 
	 * @param mouseAction			Mouse Action to perform
	 * @param parent				Parent Object of Child Object to perform Mouse Action to
	 * @param rowObjectList			Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckText	Object relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo	Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();	
				if (text.contains(textToCheck)) {
					this.doTableCommand(mouseAction, parent, rowObjectList, j, rowObjectToDoActionTo);
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
	
	/**
	 * Performs Mouse Commands on a specified table object based on text value within a table row list object within a parent list object.
	 * 
	 * @param mouseAction			Mouse Action to perform
	 * @param parentList			Parent List Object of Child Object to perform Mouse Action to
	 * @param parentIndex			Parent List Index from which the Mouse Action will be performed
	 * @param rowObjectList			Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckText	Object relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo	Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> parentElement = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElement.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElement.get(parentIndex), rowObjectList, rowObjectToCheckText, j);
				String text = elementToCheckText.getText().trim();
				if (text.contains(textToCheck)) {
					this.doTableCommand(mouseAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
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
	
	/**
	 * Performs Mouse Commands on a specified table object based on text value within a table row list object.
	 * 
	 * @param mouseAction						Mouse Action to perform
	 * @param rowObjectList						Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo				Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		List<WebElement> rows = this.seleniumWait.waitForListElement(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doBasicCommand(mouseAction, rowObjectList, j, rowObjectToDoActionTo);
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
	
	/**
	 * Performs Mouse Commands on a specified table object based on text value within a table row list object within a parent object.
	 * 
	 * @param mouseAction						Mouse Action to perform
	 * @param parent							Parent Object of Child Object to perform Mouse Action to
	 * @param rowObjectList						Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo				Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doTableCommand(mouseAction, parent, rowObjectList, j, rowObjectToDoActionTo);
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
	
	/**
	 * Performs Mouse Commands on a specified table object based on attribute value within a table row list object within a parent list object.
	 * 
	 * @param mouseAction						Mouse Action to perform
	 * @param parentList						Parent List Object of Child Object to perform Mouse Action to
	 * @param parentIndex						Parent List Index from which the Mouse Action will be performed
	 * @param rowObjectList						Row Object List corresponding to the rows of the table
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToDoActionTo				Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForListElement(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = elementToCheckText.getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					this.doTableCommand(mouseAction, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
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