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

public class SeleniumSelectCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private SeleniumWait seleniumWait;
	
	
	public SeleniumSelectCommands(WebDriver driver, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.seleniumWait = seleniumWait;
	}
	
	boolean execute(SelectAction selectAction, WebElement element, String option) {
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
					case DESELECT:
						select.deselectByVisibleText(option);
						flgOptionTicked = true;
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
	
	void executeSelectCommands(SelectAction selectAction, By locator, String option) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
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
	

	
	void executeSelectCommands(SelectAction selectAction, By parent, By child, String option) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectToBeVisible(parent);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
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
	
	void executeSelectCommands(SelectAction selectAction, By parent, By child, int index, String option) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
			childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
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
	
}