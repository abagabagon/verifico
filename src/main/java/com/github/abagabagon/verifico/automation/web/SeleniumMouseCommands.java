package com.github.abagabagon.verifico.automation.web;

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
	
	void executeMouseCommands(MouseAction mouseAction, By locator) {
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
	
	void executeMouseCommands(MouseAction mouseAction, By parent, By child) {
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
	
	void executeMouseCommands(MouseAction mouseAction, By parent, By child, int index) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
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
	
	void executeListMouseCommands(MouseAction mouseAction, By objectList, String textToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.executeMouseCommands(MouseAction.CLICK, objectList, j);
						break;
					case CLICKJS:
						this.executeMouseCommands(MouseAction.CLICKJS, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.executeMouseCommands(MouseAction.DOUBLE_CLICK, objectList, j);
						break;
					case POINT:
						this.executeMouseCommands(MouseAction.POINT, objectList, j);
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
	
	void executeListMouseCommands(MouseAction mouseAction, By objectList, String attribute, String valueToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.executeMouseCommands(MouseAction.CLICK, objectList, j);
						break;
					case CLICKJS:
						this.executeMouseCommands(MouseAction.CLICKJS, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.executeMouseCommands(MouseAction.DOUBLE_CLICK, objectList, j);
						break;
					case POINT:
						this.executeMouseCommands(MouseAction.POINT, objectList, j);
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
	
	void executeMouseCommands(MouseAction mouseAction, By locator, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
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
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
				script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(elements.get(index)).perform();
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
	
	
	void executeListMouseCommands(MouseAction mouseAction, By parent, By objectList, String textToCheck) {
		this.seleniumWait.waitForListToBeVisible(objectList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.contains(textToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.executeListMouseCommands(MouseAction.CLICK, parent, objectList, j);
						break;
					case CLICKJS:
						this.executeListMouseCommands(MouseAction.CLICKJS, parent, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.executeListMouseCommands(MouseAction.CLICK_AND_HOLD, parent, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.executeListMouseCommands(MouseAction.DOUBLE_CLICK, parent, objectList, j);
						break;
					case POINT:
						this.executeListMouseCommands(MouseAction.POINT, parent, objectList, j);
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
	
	void executeListMouseCommands(MouseAction mouseAction, By parent, By objectList, String attribute, String valueToCheck) {
		this.seleniumWait.waitForListToBeVisible(objectList);
		List<WebElement> elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getAttribute(attribute).trim();
				if (text.contains(valueToCheck)) {
					switch(mouseAction) {
					case CLICK:
						this.executeListMouseCommands(MouseAction.CLICK, parent, objectList, j);
						break;
					case CLICKJS:
						this.executeListMouseCommands(MouseAction.CLICKJS, parent, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.executeListMouseCommands(MouseAction.CLICK_AND_HOLD, parent, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.executeListMouseCommands(MouseAction.DOUBLE_CLICK, parent, objectList, j);
						break;
					case POINT:
						this.executeListMouseCommands(MouseAction.POINT, parent, objectList, j);
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
	
	void executeListMouseCommands(MouseAction mouseAction, By parent, By child, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				this.seleniumWait.waitForObjectsToBeVisible(child);
				elements = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, child);
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
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				elements = this.seleniumWait.waitForObjectsToBeVisible(child);
				script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(elements.get(index)).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + child.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	void executeTableMouseCommands(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
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
							this.executeMouseCommands(MouseAction.CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICKJS:
							this.executeMouseCommands(MouseAction.CLICKJS, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICK_AND_HOLD:
							this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case DOUBLE_CLICK:
							this.executeMouseCommands(MouseAction.DOUBLE_CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case POINT:
							this.executeMouseCommands(MouseAction.POINT, rowObjectList,rowObjectToDoActionTo,  j);
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
	
	void executeTableMouseCommands(MouseAction mouseAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
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
							this.executeMouseCommands(MouseAction.CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICKJS:
							this.executeMouseCommands(MouseAction.CLICKJS, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICK_AND_HOLD:
							this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case DOUBLE_CLICK:
							this.executeMouseCommands(MouseAction.DOUBLE_CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case POINT:
							this.executeMouseCommands(MouseAction.POINT, rowObjectList,rowObjectToDoActionTo,  j);
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
	
	void executeMouseCommands(MouseAction mouseAction, By parent, By rowObjectList, By child, int index) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(index);
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
	
	void executeTableMouseCommands(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
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
							this.executeMouseCommands(MouseAction.CLICK, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICKJS:
							this.executeMouseCommands(MouseAction.CLICKJS, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICK_AND_HOLD:
							this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case DOUBLE_CLICK:
							this.executeMouseCommands(MouseAction.DOUBLE_CLICK, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case POINT:
							this.executeMouseCommands(MouseAction.POINT, parent, rowObjectList,rowObjectToDoActionTo,  j);
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
	
	void executeTableMouseCommands(MouseAction mouseAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoActionTo) {
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
							this.executeMouseCommands(MouseAction.CLICK, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICKJS:
							this.executeMouseCommands(MouseAction.CLICKJS, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICK_AND_HOLD:
							this.executeMouseCommands(MouseAction.CLICK_AND_HOLD, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case DOUBLE_CLICK:
							this.executeMouseCommands(MouseAction.DOUBLE_CLICK, parent, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case POINT:
							this.executeMouseCommands(MouseAction.POINT, parent, rowObjectList,rowObjectToDoActionTo,  j);
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