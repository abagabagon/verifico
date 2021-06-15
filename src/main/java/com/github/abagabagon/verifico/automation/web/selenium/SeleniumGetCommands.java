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

public class SeleniumGetCommands {

	protected WebDriver driver;
	protected Logger log;
	private SeleniumWait seleniumWait;
	
	public SeleniumGetCommands(WebDriver driver, SeleniumWait seleniumWait) {
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}

	enum GetAction {
		GET_ATTRIBUTE, GET_DROPDOWN, GET_TEXT
	}
	
	String executeGetCommands(GetAction getAction, By locator, String attribute) {
		boolean actionPerformed = false;
		WebElement element = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(getAction) {
				case GET_ATTRIBUTE:
					element = this.seleniumWait.waitForObjectToBePresent(locator);
					value = element.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					element = this.seleniumWait.waitForObjectToBePresent(locator);
					value = element.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
		return value;
	}
	
	String executeGetCommands(GetAction getAction, By parent, By child, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(getAction) {
				case GET_ATTRIBUTE:
					value = childElement.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					value = childElement.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElement.click();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
		return value;
	}
	
	String executeGetCommands(GetAction getAction, By parent, By child, int index, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(getAction) {
				case GET_ATTRIBUTE:
					value = childElement.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					value = childElement.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElement.click();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
		return value;
	}
	
	String executeTableGetCommands(GetAction getAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
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
						switch(getAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_DROPDOWN:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(getAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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
		return value;
	}
	
	String executeTableGetCommands(GetAction getAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attributeToCheck).trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(getAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_DROPDOWN:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(getAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	String executeGetCommands(GetAction getAction, By parent, By rowObjectList, By child, int index, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(getAction) {
				case GET_ATTRIBUTE:
					value = childElement.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					value = childElement.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElement.click();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(getAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
		return value;
	}
	
	String executeTableGetCommands(GetAction getAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
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
						switch(getAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(getAction, parent, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_DROPDOWN:
							value = this.executeGetCommands(getAction, parent, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(getAction, parent, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(getAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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
		return value;
	}
	
	String executeTableGetCommands(GetAction getAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attributeToCheck).trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(getAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_DROPDOWN:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(getAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(getAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
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