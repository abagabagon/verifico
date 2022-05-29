package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class StateAssertions extends Commands {

	protected WebDriver driver;
	protected Logger log;
	private WaitCommands wait;
	
	private enum StateAssertionAction {
		DISPLAYED, NOT_DISPLAYED, ENABLED, DISABLED, SELECTED, DESELECTED
	}
	
	public StateAssertions(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
	}
	
	private boolean execute(StateAssertionAction stateAssertionAction, WebElement element, int size) {
		boolean status = false;
		switch(stateAssertionAction) {
		case DISPLAYED:
			status = element.isDisplayed();
			break;
		case NOT_DISPLAYED:
			if(size == 0) {
				status = true;
			}
			break;
		case ENABLED:
			status = element.isEnabled();
			break;
		case DISABLED:
			status = element.isEnabled();
			break;
		case SELECTED:
			this.wait.waitForObjectSelectionStateToBe(element, true);
			status = element.isSelected();
			break;
		case DESELECTED:
			status = this.wait.waitForObjectSelectionStateToBe(element, false);
			break;
		default:
			this.log.fatal("Unsupported Assertion Action.");
		}
		return status;
	}
	
	private boolean doCommand(StateAssertionAction stateAssertionAction, WebElement element, int size) {
		boolean status = this.execute(stateAssertionAction, element, size);
		if(status) {
			if (stateAssertionAction == StateAssertionAction.NOT_DISPLAYED) {
				this.log.debug("I saw state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.debug("I saw state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
		} else {
			if (stateAssertionAction == StateAssertionAction.NOT_DISPLAYED) {
				this.log.error("I didn't see state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.error("I didn't see state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
			
		}
		return status;
	}
	
}