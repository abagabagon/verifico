package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;

public class MouseCommands extends Commands {

	private Logger log;
	private JavascriptExecutor javascriptExecutor;
	private Actions action;
	private SeleniumWait seleniumWait;
	
	private enum MouseAction {
		CLICK, CLICKJS, CLICK_AND_HOLD, DOUBLE_CLICK, DRAG_AND_DROP, POINT
	}
	
	public MouseCommands(WebDriver driver, JavascriptExecutor javascriptExecutor, Actions action, SeleniumWait seleniumWait) {
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
			element = this.seleniumWait.waitForElementToBeVisible(element);
			String script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
			this.javascriptExecutor.executeScript(script);
			this.action.moveToElement(element).perform();
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (MoveTargetOutOfBoundsException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(mouseAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is out-of-bounds.");
			element = this.seleniumWait.waitForElementToBeVisible(element);
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
	 * @param locator		Element to perform Mouse Action to
	 */
	
	private void doCommand(MouseAction mouseAction, By locator) {
		this.log.debug("Performing " + String.valueOf(mouseAction).replace('_', ' ') + " to the Web Element: \"" + locator.toString() + "\".");
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.seleniumWait.waitForElementToBeVisible(locator);
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
	 * @param parent		Parent Element of Child Element to perform Mouse Action to
	 * @param child			Child Element to perform Mouse Action to
	 */
	
	private void doCommand(MouseAction mouseAction, By parent, By child) {
		this.log.debug("Performing " + String.valueOf(mouseAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForElementToBeVisible(parent);
			childElement = this.seleniumWait.waitForNestedElementToBePresent(parentElement, child);
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
	 * Performs Mouse Commands on specified child object nested within a parent object.
	 * 
	 * @param mouseAction	Mouse Action to perform
	 * @param parent		Parent Element of Child Element to perform Mouse Action to
	 * @param child			Child Element to perform Mouse Action to
	 */
	
	private void doCommand(MouseAction mouseAction, WebElement parent, By child) {
		this.log.debug("Performing " + String.valueOf(mouseAction).replace('_', ' ') + " to the Child Web Element: \"" + child.toString() + "\" of the Parent Web Element: \"" + parent.toString() + "\".");
		boolean actionPerformed = false;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			childElement = this.seleniumWait.waitForNestedElementToBePresent(parent, child);
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
	 * Clicks the Web Element of the specified Locator.
	 * 
	 * @param locator Locator of Web Element to click.
	 */
	
	public final void click(By locator) {
		this.doCommand(MouseAction.CLICK, locator);
	}
	
	/**
	 * Clicks the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child		Locator of Child Web Element to click.
	 */
	
	public final void click(By parent, By child) {
		this.doCommand(MouseAction.CLICK, parent, child);
	}
	
	/**
	 * Clicks the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param parent	Parent Web Element
	 * @param child		Locator of Child Web Element to click.
	 */
	
	public final void click(WebElement parent, By child) {
		this.doCommand(MouseAction.CLICK, parent, child);
	}
	
	/**
	 * Clicks the Web Element of the specified Locator using Javascript.
	 * 
	 * @param locator Locator of Web Element to click.
	 */
	
	public final void clickJs(By locator) {
		this.doCommand(MouseAction.CLICKJS, locator);
	}
	
	/**
	 * Clicks the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator using Javascript.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child		Locator of Child Web Element to click.
	 */
	
	public final void clickJs(By parent, By child) {
		this.doCommand(MouseAction.CLICKJS, parent, child);
	}
	
	/**
	 * Clicks the Web Element of the specified Child Locator within the context of the Parent Web Element using Javascipt.
	 * 
	 * @param parent	Parent Web Element
	 * @param child		Locator of Child Web Element to click.
	 */
	
	public final void clickJs(WebElement parent, By child) {
		this.doCommand(MouseAction.CLICKJS, parent, child);
	}
	
	/**
	 * Clicks and holds the Web Element of the specified Locator.
	 * 
	 * @param locator Locator of Web Element to click and hold.
	 */
	
	public final void clickAndHold(By locator) {
		this.doCommand(MouseAction.CLICK_AND_HOLD, locator);
	}
	
	/**
	 * Clicks and holds the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child		Locator of Child Web Element to click and hold.
	 */
	
	public final void clickAndHold(By parent, By child) {
		this.doCommand(MouseAction.CLICK_AND_HOLD, parent, child);
	}
	
	/**
	 * Clicks and holds the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param parent	Parent Web Element
	 * @param child		Locator of Child Web Element to click and hold.
	 */
	
	public final void clickAndHold(WebElement parent, By child) {
		this.doCommand(MouseAction.CLICK_AND_HOLD, parent, child);
	}
	
	/**
	 * Double-clicks the Web Element of the specified Locator.
	 * 
	 * @param locator Locator of Web Element to double-click.
	 */
	
	public final void doubleClick(By locator) {
		this.doCommand(MouseAction.DOUBLE_CLICK, locator);
	}
	
	/**
	 * Double-clicks the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child		Locator of Child Web Element to double-click.
	 */
	
	public final void doubleClick(By parent, By child) {
		this.doCommand(MouseAction.DOUBLE_CLICK, parent, child);
	}
	
	/**
	 * Double-clicks the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param parent	Parent Web Element
	 * @param child		Locator of Child Web Element to double-click.
	 */
	
	public final void doubleClick(WebElement parent, By child) {
		this.doCommand(MouseAction.DOUBLE_CLICK, parent, child);
	}
	
	/**
	 * Points mouse to the Web Element of the specified Locator.
	 * 
	 * @param locator Locator of Web Element to point the mouse into.
	 */
	
	public final void point(By locator) {
		this.doCommand(MouseAction.POINT, locator);
	}
	
	/**
	 * Points mouse to the Web Element of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param parent	Locator of Parent Web Element
	 * @param child		Locator of Child Web Element to point the mouse into.
	 */
	
	public final void point(By parent, By child) {
		this.doCommand(MouseAction.POINT, parent, child);
	}
	
	/**
	 * Points mouse to the Web Element of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param parent	Parent Web Element
	 * @param child		Locator of Child Web Element to point the mouse into.
	 */
	
	public final void point(WebElement parent, By child) {
		this.doCommand(MouseAction.POINT, parent, child);
	}
	
	/**
	 * Drags a Web Element and drops it at target Web Element. Used for Elements that can be dragged.
	 * 
	 * @param sourceObject Object used to locate Web Element to be dragged.
	 * @param targetObject Object used to locate Web Element where the dragged Web Element will be dropped into.
	 */
	
	public final void dragAndDrop(By sourceLocator, By targetLocator) {
		this.log.debug("Performing " + String.valueOf(MouseAction.DRAG_AND_DROP) + " to the Web Element: \"" + sourceLocator.toString() + "\".");
		boolean actionPerformed = false;
		WebElement sourceElement = null;
		WebElement targetElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceLocator);
				targetElement = this.seleniumWait.waitForObjectToBeClickable(targetLocator);
				this.action.dragAndDrop(sourceElement, targetElement).perform();
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (TimeoutException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\". Wait time has expired.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(MouseAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceLocator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
}