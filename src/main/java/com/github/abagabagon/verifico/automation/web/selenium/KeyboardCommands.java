package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
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

public class KeyboardCommands extends Commands {

	private Actions action;
	
	private enum KeyboardAction {
		CLEAR, PRESS, TYPE
	}
	
	public KeyboardCommands(WebDriver driver, Actions action, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.action = action;
		this.wait = wait;
	}
	
	private boolean execute(KeyboardAction keyboardAction, WebElement element, String inputText, Keys keyButton) {
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
			case TYPE:
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
	
	private void doCommand(KeyboardAction keyboardAction, By locator, String inputText, Keys keyButton) {
		this.log.debug("Performing " + String.valueOf(keyboardAction).replace('_', ' ') + " to the Web Element: \"" + locator.toString() + "\".");
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.wait.waitForElementToBeVisible(locator);
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
	
	private void doCommand(KeyboardAction keyboardAction, By parent, By child, String inputText, Keys keyButton) {
		this.log.debug("Performing " + String.valueOf(keyboardAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.wait.waitForElementToBeVisible(parent);
			childElement = this.wait.waitForNestedElementToBePresent(parentElement, child);
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
	
	private void doCommand(KeyboardAction keyboardAction, WebElement parent, By child, String inputText, Keys keyButton) {
		this.log.debug("Performing " + String.valueOf(keyboardAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.wait.waitForElementToBeVisible(parent);
			childElement = this.wait.waitForNestedElementToBePresent(parentElement, child);
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
	
	/**
	 * Clears value of the Web Element of the specified Locator.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param locator Locator of Web Element to clear the value from.
	 */
	
	public final void clear(By locator) {
		this.doCommand(KeyboardAction.CLEAR, locator, null, null);
	}
	
	/**
	 * Clears value of the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param locator	Locator of Child Web Element to clear the value from.
	 */
	
	public final void clear(By parent, By child) {
		this.doCommand(KeyboardAction.CLEAR, parent, child, null, null);
	}
	
	/**
	 * Clears value of the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param parent	Parent Web Element
	 * @param locator	Locator of Child Web Element to clear the value from.
	 */
	
	public final void clear(WebElement parent, By child) {
		this.doCommand(KeyboardAction.CLEAR, parent, child, null, null);
	}
	
	/**
	 * Simulates pressing of characters.
	 * 
	 * @param keyButton Key Button to press.
	 */
	
	public final void press(Keys keyButton) { 
		this.action.sendKeys(keyButton).perform();;
	}
	
	/**
	 * Simulates pressing of characters into the Web Element of the specified Locator.
	 * 
	 * @param locator 	Locator of Web Element to simulate pressing of characters into.
	 * @param keyButton	Key Button to press
	 */
	
	public final void press(By locator, Keys keyButton) {
		this.doCommand(KeyboardAction.PRESS, locator, null, keyButton);
	}
	
	/**
	 * Simulates pressing of characters into the Web Element of the specified Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child 	Locator of Child Web Element to simulate pressing of characters into.
	 * @param keyButton	Key Button to press
	 */
	
	public final void press(By parent, By child, Keys keyButton) {
		this.doCommand(KeyboardAction.PRESS, parent, child, null, keyButton);
	}
	
	/**
	 * Simulates pressing of characters into the Web Element of the specified Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Parent Web Element
	 * @param child 	Locator of Child Web Element to simulate pressing of characters into.
	 * @param keyButton	Key Button to press
	 */
	
	public final void press(WebElement parent, By child, Keys keyButton) {
		this.doCommand(KeyboardAction.PRESS, parent, child, null, keyButton);
	}
	
	/**
	 * Types the specified input text to the Web Element of the specified Locator.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param locator 	Locator of Web Element to type the input text into.
	 * @param inputText Text value to input.
	 */
	
	public final void type(By locator, String inputText) {
		this.doCommand(KeyboardAction.TYPE, locator, inputText, null);
	}
	
	/**
	 * Types the specified input text to the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child 	Locator of Child Web Element to type the input text into.
	 * @param inputText Text value to input.
	 */
	
	public final void type(By parent, By child, String inputText) {
		this.doCommand(KeyboardAction.TYPE, parent, child, inputText, null);
	}
	
	/**
	 * Types the specified input text to the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * Applicable for INPUT and TEXTAREA Web Elements.
	 * 
	 * @param parent	Parent Web Element
	 * @param child 	Locator of Child Web Element to type the input text into.
	 * @param inputText Text value to input.
	 */
	
	public final void type(WebElement parent, By child, String inputText) {
		this.doCommand(KeyboardAction.TYPE, parent, child, inputText, null);
	}

}