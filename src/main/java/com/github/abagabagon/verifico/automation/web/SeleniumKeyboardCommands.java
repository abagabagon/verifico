package com.github.abagabagon.verifico.automation.web;

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

public class SeleniumKeyboardCommands {

	protected WebDriver driver;
	protected Logger log;
	private Actions action;
	private SeleniumWait seleniumWait;
	
	public SeleniumKeyboardCommands(WebDriver driver, Actions action, SeleniumWait seleniumWait) {
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.action = action;
		this.seleniumWait = seleniumWait;
	}
	
	enum KeyboardAction {
		CLEAR, PRESS, SEND_KEYS
	}
	
	void executeKeyboardCommands(KeyboardAction keyboardAction, By locator, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(keyboardAction) {
				case CLEAR:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
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
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					element.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					element.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementNotInteractableException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\". Element cannot be interacted with.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				element.click();
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				element.click();
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	void executeKeyboardCommands(KeyboardAction keyboardAction, By parent, By child, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(keyboardAction) {
				case CLEAR:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					Platform platform = OperatingSystem.getOS();
					if (platform == Platform.MAC) {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					} else {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					}
					break;
				case PRESS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	void executeKeyboardCommands(KeyboardAction keyboardAction, By parent, By child, int index, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(keyboardAction) {
				case CLEAR:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					Platform platform = OperatingSystem.getOS();
					if (platform == Platform.MAC) {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					} else {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					}
					break;
				case PRESS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	
	void executeTableKeyboardCommands(KeyboardAction keyboardAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
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
						switch(keyboardAction) {
						case CLEAR:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case PRESS:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, null, keyButton);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(keyboardAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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
	
	void executeTableKeyboardCommands(KeyboardAction keyboardAction, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(keyboardAction) {
						case CLEAR:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case PRESS:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, null, keyButton);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(keyboardAction, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(keyboardAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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
	}
	
	void executeKeyboardCommands(KeyboardAction keyboardAction, By parent, By rowObjectList, By child, int index, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForNestedObjectsToBePresent(parent, rowObjectList).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(keyboardAction) {
				case CLEAR:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					Platform platform = OperatingSystem.getOS();
					if (platform == Platform.MAC) {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					} else {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					}
					break;
				case PRESS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(keyboardAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
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
	
	void executeTableKeyboardCommands(KeyboardAction keyboardAction, By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
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
						switch(keyboardAction) {
						case CLEAR:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case PRESS:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, null, keyButton);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(keyboardAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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
	
	void executeTableKeyboardCommands(KeyboardAction keyboardAction, By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		List<WebElement> rows = this.seleniumWait.waitForNestedObjectsToBeVisible(parent, rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToCheckAttributeValue, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckAttributeValue.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getAttribute(attribute).trim();
				}
				if (text.contains(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(parent, rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(keyboardAction) {
						case CLEAR:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case PRESS:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, null, keyButton);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(keyboardAction, parent, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(keyboardAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
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