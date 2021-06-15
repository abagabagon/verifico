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

public class SeleniumMouseCommands {

	@SuppressWarnings("unused")
	private WebDriver driver;
	private Logger log;
	private JavascriptExecutor javascriptExecutor;
	private Actions action;
	private SeleniumWait seleniumWait;
	
	public SeleniumMouseCommands(WebDriver driver, JavascriptExecutor javascriptExecutor, Actions action, SeleniumWait seleniumWait) {
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.javascriptExecutor = javascriptExecutor;
		this.action = action;
		this.seleniumWait = seleniumWait;
	}
	
	enum MouseAction {
		CLICK, CLICKJS, CLICK_AND_HOLD, DOUBLE_CLICK, DRAG_AND_DROP, POINT
	}
	
	void isTextDisplayed() {
		
	}
	
	/**
	 * Performs basic Mouse Commands on specified object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param locator		Object to perform Mouse Action to
	 */
	
	void doBasicCommand(MouseAction mouseAction, By locator) {
		boolean actionPerformed = false;
		WebElement element = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(mouseAction) {
				case CLICK:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					element.click();
					break;
				case CLICKJS:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.javascriptExecutor.executeScript("arguments[0].click();", element);
					break;
				case CLICK_AND_HOLD:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.action.clickAndHold(element).perform();
					break;
				case DOUBLE_CLICK:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.action.doubleClick(element).perform();
					break;
				case POINT:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					this.action.moveToElement(element).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				element = this.seleniumWait.waitForObjectToBeVisible(locator);
				script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(element).perform();
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (MoveTargetOutOfBoundsException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is out-of-bounds.");
				element = this.seleniumWait.waitForObjectToBeVisible(locator);
				script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(element).perform();
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Performs basic Mouse Commands on specified child object nested within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object to perform Mouse Action to
	 * @param child			Child Object to perform Mouse Action to
	 */
	
	void doBasicCommand(MouseAction mouseAction, By parent, By child) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				switch(mouseAction) {
				case CLICK:
					childElement.click();
					break;
				case CLICKJS:
					this.javascriptExecutor.executeScript("arguments[0].click();", childElement);
					break;
				case CLICK_AND_HOLD:
					this.action.clickAndHold(childElement).perform();
					break;
				case DOUBLE_CLICK:
					this.action.doubleClick(childElement).perform();
					break;
				case POINT:
					this.action.moveToElement(childElement).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				this.seleniumWait.waitForObjectToBeVisible(childElement);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElement).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	 * Performs basic Mouse Commands on specified child object nested within a parent list object.
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
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parentList).get(parentIndex);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				switch(mouseAction) {
				case CLICK:
					childElement.click();
					break;
				case CLICKJS:
					this.javascriptExecutor.executeScript("arguments[0].click();", childElement);
					break;
				case CLICK_AND_HOLD:
					this.action.clickAndHold(childElement).perform();
					break;
				case DOUBLE_CLICK:
					this.action.doubleClick(childElement).perform();
					break;
				case POINT:
					this.action.moveToElement(childElement).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				this.seleniumWait.waitForObjectToBeVisible(childElement);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElement).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	 * Perform basic Mouse Commands on a specified list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param index			Index of Object from Object List to perform Mouse Action to
	 */
	
	void doListCommand(MouseAction mouseAction, By objectList, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
				switch(mouseAction) {
				case CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					elements.get(index).click();
					break;
				case CLICKJS:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.javascriptExecutor.executeScript("arguments[0].click();", elements.get(index));
					break;
				case CLICK_AND_HOLD:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.clickAndHold(elements.get(index)).perform();
					break;
				case DOUBLE_CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.doubleClick(elements.get(index)).perform();
					break;
				case POINT:
					script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
					this.javascriptExecutor.executeScript(script);
					this.action.moveToElement(elements.get(index)).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
				script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(elements.get(index)).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + objectList.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	 * Perform basic Mouse Commands on a specified list object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param index			Index of Child Object from Child Object List to perform Mouse Action to
	 */
	
	void doListCommand(MouseAction mouseAction, By parent, By childList, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				this.seleniumWait.waitForObjectsToBeVisible(childList);
				elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
				switch(mouseAction) {
				case CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					elements.get(index).click();
					break;
				case CLICKJS:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.javascriptExecutor.executeScript("arguments[0].click();", elements.get(index));
					break;
				case CLICK_AND_HOLD:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.clickAndHold(elements.get(index)).perform();
					break;
				case DOUBLE_CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.doubleClick(elements.get(index)).perform();
					break;
				case POINT:
					script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
					this.javascriptExecutor.executeScript(script);
					this.action.moveToElement(elements.get(index)).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				elements = this.seleniumWait.waitForObjectsToBeVisible(childList);
				script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(elements.get(index)).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	 * Perform basic Mouse Commands on a specified list object.
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
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
				this.seleniumWait.waitForObjectsToBeVisible(childList);
				childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
				switch(mouseAction) {
				case CLICK:
					this.seleniumWait.waitForObjectToBeClickable(childElementList.get(childIndex));
					childElementList.get(childIndex).click();
					break;
				case CLICKJS:
					this.seleniumWait.waitForObjectToBeClickable(childElementList.get(childIndex));
					this.javascriptExecutor.executeScript("arguments[0].click();", childElementList.get(childIndex));
					break;
				case CLICK_AND_HOLD:
					this.seleniumWait.waitForObjectToBeClickable(childElementList.get(childIndex));
					this.action.clickAndHold(childElementList.get(childIndex)).perform();
					break;
				case DOUBLE_CLICK:
					this.seleniumWait.waitForObjectToBeClickable(childElementList.get(childIndex));
					this.action.doubleClick(childElementList.get(childIndex)).perform();
					break;
				case POINT:
					script = "window.scrollTo(" + childElementList.get(childIndex).getLocation().x + ","+ childElementList.get(childIndex).getLocation().y + ")";
					this.javascriptExecutor.executeScript(script);
					this.action.moveToElement(childElementList.get(childIndex)).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElementList = this.seleniumWait.waitForObjectsToBeVisible(childList);
				script = "window.scrollTo(" + childElementList.get(childIndex).getLocation().x + ","+ childElementList.get(childIndex).getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElementList.get(childIndex)).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + childList.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	 * Perform basic Mouse Commands on a specified list object based on text.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By objectList, String textToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, objectList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, objectList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, objectList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	 * Perform basic Mouse Commands on a specified list object based on text.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child List Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object List would be located 
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Child Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By parentList, int parentIndex, By childList, String textToCheck) {
		this.seleniumWait.waitForListToBeVisible(childList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
		int size = childElementList.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = childElementList.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, parentList, parentIndex, childList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, parentList, parentIndex, childList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, parentList, parentIndex, childList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, parentList, parentIndex, childList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, parentList, parentIndex, childList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	 * Perform basic Mouse Commands on a specified list object based on text.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param textToCheck	Text value reference to use to determine which Object from the Child Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnText(MouseAction mouseAction, By parent, By childList, String textToCheck) {
		this.seleniumWait.waitForListToBeVisible(childList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, parent, childList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, parent, childList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, parent, childList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, parent, childList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, parent, childList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	 * Perform basic Mouse Commands on a specified list object based on attribute value.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param objectList	Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By objectList, String attribute, String valueToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, objectList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, objectList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, objectList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	 * Perform basic Mouse Commands on a specified list object based on attribute value.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Object of Child Object List to perform Mouse Action to
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By parent, By childList, String attribute, String valueToCheck) {
		this.seleniumWait.waitForListToBeVisible(childList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, childList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, parent, childList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, parent, childList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, parent, childList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, parent, childList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, parent, childList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	 * Perform basic Mouse Commands on a specified list object based on attribute value.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parentList	Parent List Object of Child List Object to perform Mouse Action to
	 * @param parentIndex	Index of Object from the Parent List where Child Object List would be located
	 * @param childList		Child Object List to perform Mouse Action to
	 * @param attribute     Attribute to check from Objects of the Object List
	 * @param valueToCheck	Attribute value reference to use to determine which Object from the Object List to perform Mouse Action to
	 */
	
	void doListCommandBasedOnAttributeValue(MouseAction mouseAction, By parentList, int parentIndex, By childList, String attribute, String valueToCheck) {
		this.seleniumWait.waitForListToBeVisible(childList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> childElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), childList);
		int size = childElementList.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = childElementList.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.doListCommand(MouseAction.CLICK, parentList, childList, j);
						break;
					case CLICKJS:
						this.doListCommand(MouseAction.CLICKJS, parentList, childList, j);
						break;
					case CLICK_AND_HOLD:
						this.doListCommand(MouseAction.CLICK_AND_HOLD, parentList, childList, j);
						break;
					case DOUBLE_CLICK:
						this.doListCommand(MouseAction.DOUBLE_CLICK, parentList, childList, j);
						break;
					case POINT:
						this.doListCommand(MouseAction.POINT, parentList, childList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
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
	
	void doTableCommand(MouseAction mouseAction, By parent, By rowObjectList, int rowIndex, By child) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(rowIndex);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				switch(mouseAction) {
				case CLICK:
					childElement.click();
					break;
				case CLICKJS:
					this.javascriptExecutor.executeScript("arguments[0].click();", childElement);
					break;
				case CLICK_AND_HOLD:
					this.action.clickAndHold(childElement).perform();
					break;
				case DOUBLE_CLICK:
					this.action.doubleClick(childElement).perform();
					break;
				case POINT:
					this.action.moveToElement(childElement).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				this.seleniumWait.waitForObjectToBeVisible(childElement);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElement).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	void doTableCommand(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, int rowIndex, By child) {
		boolean actionPerformed = false;
		List<WebElement> parentElementList = null;
		List<WebElement> rowElementList = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElementList = this.seleniumWait.waitForObjectsToBePresent(parentList);
				rowElementList = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(rowElementList.get(rowIndex), child);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				switch(mouseAction) {
				case CLICK:
					childElement.click();
					break;
				case CLICKJS:
					this.javascriptExecutor.executeScript("arguments[0].click();", childElement);
					break;
				case CLICK_AND_HOLD:
					this.action.clickAndHold(childElement).perform();
					break;
				case DOUBLE_CLICK:
					this.action.doubleClick(childElement).perform();
					break;
				case POINT:
					this.action.moveToElement(childElement).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				this.seleniumWait.waitForObjectToBeVisible(childElement);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElement).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parentList.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doBasicCommand(MouseAction.CLICK, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doBasicCommand(MouseAction.CLICKJS, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doBasicCommand(MouseAction.CLICK_AND_HOLD, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doBasicCommand(MouseAction.DOUBLE_CLICK, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doBasicCommand(MouseAction.POINT, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doTableCommand(MouseAction.CLICK, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doTableCommand(MouseAction.CLICKJS, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doTableCommand(MouseAction.CLICK_AND_HOLD, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doTableCommand(MouseAction.DOUBLE_CLICK, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doTableCommand(MouseAction.POINT, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	void doTableCommandBasedOnText(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> parentElement = this.seleniumWait.waitForObjectsToBePresent(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElement.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElement.get(parentIndex), rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parentElement.get(parentIndex), rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doTableCommand(MouseAction.CLICK, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doTableCommand(MouseAction.CLICKJS, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doTableCommand(MouseAction.CLICK_AND_HOLD, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doTableCommand(MouseAction.DOUBLE_CLICK, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doTableCommand(MouseAction.POINT, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking attribute value at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(valueToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doBasicCommand(MouseAction.CLICK, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doBasicCommand(MouseAction.CLICKJS, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doBasicCommand(MouseAction.CLICK_AND_HOLD, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doBasicCommand(MouseAction.DOUBLE_CLICK, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doBasicCommand(MouseAction.POINT, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking attribute value at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(valueToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doTableCommand(MouseAction.CLICK, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doTableCommand(MouseAction.CLICKJS, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doTableCommand(MouseAction.CLICK_AND_HOLD, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doTableCommand(MouseAction.DOUBLE_CLICK, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doTableCommand(MouseAction.POINT, parent, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	void doTableCommandBasedOnAttributeValue(MouseAction mouseAction, By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> parentElementList = this.seleniumWait.waitForObjectsToBeVisible(parentList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parentElementList.get(parentIndex), rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking attribute value at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(valueToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parentElementList.get(parentIndex), rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(mouseAction) {
						case CLICK:
							this.doTableCommand(MouseAction.CLICK, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICKJS:
							this.doTableCommand(MouseAction.CLICKJS, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case CLICK_AND_HOLD:
							this.doTableCommand(MouseAction.CLICK_AND_HOLD, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case DOUBLE_CLICK:
							this.doTableCommand(MouseAction.DOUBLE_CLICK, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						case POINT:
							this.doTableCommand(MouseAction.POINT, parentList, parentIndex, rowObjectList, j, rowObjectToDoActionTo);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(mouseAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
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
	
	private void wait(int duration) {
		this.log.debug("I wait for " + duration + " Second(s).");
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