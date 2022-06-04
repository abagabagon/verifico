package com.github.abagabagon.verifico.automation.web;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

public class GetCommands extends Commands {

	private String retrievedValue;

	private enum GetAction {
		GET_ATTRIBUTE, GET_DROPDOWN, GET_TEXT
	}

	public GetCommands(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
		this.elementFactory = new WebElementFactory(this.wait);
	}

	private boolean execute(GetAction getAction, WebElement element, String attribute) {
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

	private String doCommand(GetAction getAction, By locator, String attribute) {
		this.log.debug("Performing " + String.valueOf(getAction).replace('_', ' ') + " to the Web Element: \"" + locator.toString() + "\".");
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.elementFactory.createElement(locator);
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

	private String doCommand(GetAction getAction, By parent, By child, String attribute) {
		this.log.debug("Performing " + String.valueOf(getAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.elementFactory.createElement(parent);
			childElement = this.elementFactory.createElement(parentElement, child);
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

	private String doCommand(GetAction getAction, WebElement parent, By child, String attribute) {
		this.log.debug("Performing " + String.valueOf(getAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			childElement = this.elementFactory.createElement(parent, child);
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

	/**
	 * Gets the text of the Web Element of the specified Locator.
	 *
	 * @param locator Locator of Web Element to get text from.
	 * @return Retrieved text from Web Element
	 */

	public final String getText(By locator) {
		String text = this.doCommand(GetAction.GET_TEXT, locator, null);
		return text;
	}

	/**
	 * Gets the text of the Web Element of the specified Child Locator within the
	 * context of the Web Element of the specified Parent Locator.
	 *
	 * @param parent Locator of Parent Web Element
	 * @param child  Locator of Child Web Element to get text from.
	 * @return Retrieved text from Child Web Element
	 */

	public final String getText(By parent, By child) {
		String text = this.doCommand(GetAction.GET_TEXT, parent, child, null);
		return text;
	}

	/**
	 * Gets the text of the Web Element of the specified Child Locator within the
	 * context of the Parent Web Element.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of Child Web Element to get text from.
	 * @return Retrieved text from Child Web Element
	 */

	public final String getText(WebElement parent, By child) {
		String text = this.doCommand(GetAction.GET_TEXT, parent, child, null);
		return text;
	}

	/**
	 * Gets the attribute value of the Web Element of the specified Locator.
	 *
	 * @param locator   Locator of Web Element to get attribute value from.
	 * @param attribute Web Element Attribute to get value of
	 * @return Retrieved attribute from Web Element
	 */

	public final String getAttributeValue(By locator, String attribute) {
		String text = this.doCommand(GetAction.GET_ATTRIBUTE, locator, attribute);
		return text;
	}

	/**
	 * Gets the attribute value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator.
	 *
	 * @param parent    Locator of Parent Web Element
	 * @param child     Locator of Child Web Element to get attribute value from.
	 * @param attribute Web Element Attribute to get value of.
	 * @return Retrieved attribute from Child Web Element
	 */

	public final String getAttributeValue(By parent, By child, String attribute) {
		String text = this.doCommand(GetAction.GET_ATTRIBUTE, parent, child, attribute);
		return text;
	}

	/**
	 * Gets the attribute value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element.
	 *
	 * @param parent    Parent Web Element
	 * @param child     Locator of Child Web Element to get attribute value from.
	 * @param attribute Web Element Attribute to get value of
	 * @return Retrieved attribute from Child Web Element
	 */

	public final String getAttributeValue(WebElement parent, By child, String attribute) {
		String text = this.doCommand(GetAction.GET_ATTRIBUTE, parent, child, attribute);
		return text;
	}

	/**
	 * Gets the drop-down list value of the Web Element of the specified Locator.
	 *
	 * @param locator Locator of Web Element to get drop-down list value from.
	 * @return Retrieved drop-down list value from Web Element
	 */

	public final String getDropDownListValue(By locator) {
		String text = this.doCommand(GetAction.GET_DROPDOWN, locator, null);
		return text;
	}

	/**
	 * Gets the drop-down list value of the Web Element of the specified Child
	 * Locator within the context of the Web Element of the specified Parent
	 * Locator.
	 *
	 * @param parent Locator of Parent Web Element
	 * @param child  Locator of Web Element to get drop-down list value from.
	 * @return Retrieved drop-down list value from Child Web Element
	 */

	public final String getDropDownListValue(By parent, By child) {
		String text = this.doCommand(GetAction.GET_DROPDOWN, parent, child, null);
		return text;
	}

	/**
	 * Gets the drop-down list value of the Web Element of the specified Child
	 * Locator within the context of the Parent Web Element.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of Web Element to get drop-down list value from.
	 * @return Retrieved drop-down list value from Child Web Element
	 */

	public final String getDropDownListValue(WebElement parent, By child) {
		String text = this.doCommand(GetAction.GET_DROPDOWN, parent, child, null);
		return text;
	}

}