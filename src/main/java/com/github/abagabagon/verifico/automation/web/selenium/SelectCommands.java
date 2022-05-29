package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

public class SelectCommands extends Commands {

	protected WebDriver driver;
	protected Logger log;
	private WaitCommands wait;
	
	private enum SelectAction {
		DESELECT, SELECT
	}
	
	public SelectCommands(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
	}
	
	private boolean execute(SelectAction selectAction, WebElement element, String option) {
		boolean actionPerformed = false;
		try {
			Select select = new Select(element);
			int size = select.getOptions().size();
			boolean flgOptionTicked = false;
			for (int j = 0; j < size; j++) {
				if (option.equals(select.getOptions().get(j).getText().trim())) {
					switch(selectAction) {
					case SELECT:
						select.selectByVisibleText(option);
						flgOptionTicked = true;
						break;
					case DESELECT:
						select.deselectByVisibleText(option);
						flgOptionTicked = true;
						break;
					default:
						this.log.fatal("Unsupported SELECT Mode.");
					}
					break;
				}
			}
			if (flgOptionTicked == false) {
				this.log.error("Failed to select an option. Option \"" + option + "\" is invalid!");
			}
			actionPerformed = true;
		} catch (NullPointerException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(selectAction) + "\" for Web Element \"" + element.toString() + "\". Element created is NULL.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(selectAction) + "\" for Web Element \"" + element.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (UnexpectedTagNameException e) {
			this.log.warn("Unable to perform \"" + String.valueOf(selectAction) + "\" for Web Element \"" + element.toString() + "\". Element does not have a SELECT Tag.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.warn("Unable to perform \"" + String.valueOf(selectAction) + "\" for Web Element \"" + element.toString() + "\".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return actionPerformed;
	}
	
	private void doCommand(SelectAction selectAction, By locator, String option) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.wait.waitForElementToBeVisible(locator);
			actionPerformed = this.execute(selectAction, element, option);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(selectAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(selectAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}

	private void doCommand(SelectAction selectAction, By parent, By child, String option) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.wait.waitForElementToBeVisible(parent);
			childElement = this.wait.waitForNestedElementToBePresent(parentElement, child);
			actionPerformed = this.execute(selectAction, childElement, option);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(selectAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(selectAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void doCommand(SelectAction selectAction, WebElement parent, By child, String option) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.wait.waitForElementToBeVisible(parent);
			childElement = this.wait.waitForNestedElementToBePresent(parentElement, child);
			actionPerformed = this.execute(selectAction, childElement, option);
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(selectAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(selectAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Selects a Drop-down List Web Element Option of the specified Locator.
	 * 
	 * @param locator	Locator of Web Element to select option.
	 * @param option	Option to be selected.
	 */
	
	public final void select(By locator, String option) {
		this.doCommand(SelectAction.SELECT, locator, option);
	}
	
	/**
	 * Selects a Drop-down List Web Element Option of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param locator	Locator of Parent Web Element.
	 * @param locator	Locator of Child Web Element to select option.
	 * @param option	Option to be selected.
	 */
	
	public final void select(By parent, By child, String option) {
		this.doCommand(SelectAction.SELECT, parent, child, option);
	}
	
	/**
	 * Selects a Drop-down List Web Element Option of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param locator	Parent Web Element.
	 * @param locator	Locator of Child Web Element to select option.
	 * @param option	Option to be selected.
	 */
	
	public final void select(WebElement parent, By child, String option) {
		this.doCommand(SelectAction.SELECT, parent, child, option);
	}
	
	/**
	 * De-selects a Drop-down List Web Element Option of the specified Locator.
	 * 
	 * @param locator	Locator of Web Element to de-select option.
	 * @param option	Option to be selected.
	 */
	
	public final void deselect(By locator, String option) {
		this.doCommand(SelectAction.DESELECT, locator, option);
	}
	
	/**
	 * De-selects a Drop-down List Web Element Option of the specified Child Locator within the context of the Web Element of the Parent Locator.
	 * 
	 * @param locator	Locator of Parent Web Element.
	 * @param locator	Locator of Child Web Element to de-select option.
	 * @param option	Option to be selected.
	 */
	
	public final void deselect(By parent, By child, String option) {
		this.doCommand(SelectAction.DESELECT, parent, child, option);
	}
	
	/**
	 * De-selects a Drop-down List Web Element Option of the specified Child Locator within the context of the Parent Web Element.
	 * 
	 * @param locator	Parent Web Element.
	 * @param locator	Locator of Child Web Element to de-select option.
	 * @param option	Option to be selected.
	 */
	
	public final void deselect(WebElement parent, By child, String option) {
		this.doCommand(SelectAction.DESELECT, parent, child, option);
	}
	
}