package com.github.abagabagon.verifico.automation.web;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class StateAssertions extends Commands {

	private WebElementFactory elementFactory;
	
	private enum StateAssertion {
		DISPLAYED, NOT_DISPLAYED, ENABLED, DISABLED, SELECTED, DESELECTED
	}
	
	public StateAssertions(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
		this.elementFactory = new WebElementFactory(this.driver, this.wait);
	}
	
	private boolean execute(StateAssertion stateAssertionAction, WebElement element, int size) {
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
	
	private boolean doCommand(StateAssertion stateAssertionAction, WebElement element, int size) {
		boolean status = this.execute(stateAssertionAction, element, size);
		if(status) {
			if (stateAssertionAction == StateAssertion.NOT_DISPLAYED) {
				this.log.debug("I saw state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.debug("I saw state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
		} else {
			if (stateAssertionAction == StateAssertion.NOT_DISPLAYED) {
				this.log.error("I didn't see state of the Web Element as " + String.valueOf(stateAssertionAction) + ".");
			} else {
				this.log.error("I didn't see state of the Web Element: \"" + element.toString() + "\" as " + String.valueOf(stateAssertionAction) + ".");
			}
			
		}
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is displayed on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is displayed on Web Page.
	 * 			<code>false</code> if Web Element is not displayed on Web Page.
	 */
	
	public final boolean see(By locator) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.doCommand(StateAssertion.DISPLAYED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is displayed on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is displayed on Web Page.
	 * 			<code>false</code> if Web Element is not displayed on Web Page.
	 */
	
	public final boolean see(By parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.DISPLAYED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is displayed on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is displayed on Web Page.
	 * 			<code>false</code> if Web Element is not displayed on Web Page.
	 */
	
	public final boolean see(WebElement parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.DISPLAYED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is not displayed on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is not displayed on Web Page.
	 * 			<code>false</code> if Web Element is displayed on Web Page.
	 */
	
	public final boolean dontSee(By locator) {
		this.wait.waitForListElementToBeInvisible(locator);
		List<WebElement> elements = this.driver.findElements(locator);
		boolean status = this.doCommand(StateAssertion.NOT_DISPLAYED, null, elements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is not displayed on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is not displayed on Web Page.
	 * 			<code>false</code> if Web Element is displayed on Web Page.
	 */
	
	public final boolean dontSee(By parent, By child) {
		WebElement parentElement = this.elementFactory.createElement(parent);
		List<WebElement> childElements = parentElement.findElements(child);
		boolean status = this.doCommand(StateAssertion.NOT_DISPLAYED, null, childElements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is not displayed on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is not displayed on Web Page.
	 * 			<code>false</code> if Web Element is displayed on Web Page.
	 */
	
	public final boolean dontSee(WebElement parent, By child) {
		List<WebElement> childElements = parent.findElements(child);
		boolean status = this.doCommand(StateAssertion.NOT_DISPLAYED, null, childElements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is enabled on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is enabled on Web Page.
	 * 			<code>false</code> if Web Element is disabled on Web Page.
	 */
	
	public final boolean seeEnabled(By locator) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.doCommand(StateAssertion.ENABLED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is enabled on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is enabled on Web Page.
	 * 			<code>false</code> if Web Element is disabled on Web Page.
	 */
	
	public final boolean seeEnabled(By parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.ENABLED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is enabled on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is enabled on Web Page.
	 * 			<code>false</code> if Web Element is disabled on Web Page.
	 */
	
	public final boolean seeEnabled(WebElement parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.ENABLED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is disabled on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is disabled on Web Page.
	 * 			<code>false</code> if Web Element is enabled on Web Page.
	 */
	
	public final boolean seeDisabled(By locator) {
		this.wait.waitForListElementToBeInvisible(locator);
		List<WebElement> elements = this.driver.findElements(locator);
		boolean status = this.doCommand(StateAssertion.DISABLED, null, elements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is disabled on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is disabled on Web Page.
	 * 			<code>false</code> if Web Element is enabled on Web Page.
	 */
	
	public final boolean seeDisabled(By parent, By child) {
		WebElement parentElement = this.elementFactory.createElement(parent);
		List<WebElement> childElements = parentElement.findElements(child);
		boolean status = this.doCommand(StateAssertion.DISABLED, null, childElements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is disabled on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is disabled on Web Page.
	 * 			<code>false</code> if Web Element is enabled on Web Page.
	 */
	
	public final boolean seeDisabled(WebElement parent, By child) {
		List<WebElement> childElements = parent.findElements(child);
		boolean status = this.doCommand(StateAssertion.DISABLED, null, childElements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is selected on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is selected on Web Page.
	 * 			<code>false</code> if Web Element is deselected on Web Page.
	 */
	
	public final boolean seeSelected(By locator) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.doCommand(StateAssertion.SELECTED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is selected on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is selected on Web Page.
	 * 			<code>false</code> if Web Element is deselected on Web Page.
	 */
	
	public final boolean seeSelected(By parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.SELECTED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is selected on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is selected on Web Page.
	 * 			<code>false</code> if Web Element is deselected on Web Page.
	 */
	
	public final boolean seeSelected(WebElement parent, By child) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.doCommand(StateAssertion.SELECTED, element, 0);
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator is deselected on Web Page.
	 * 
	 * @param	locator Locator of Web Element to check.
	 * @return	<code>true</code> if Web Element is deselected on Web Page.
	 * 			<code>false</code> if Web Element is selected on Web Page.
	 */
	
	public final boolean seeDeselected(By locator) {
		this.wait.waitForListElementToBeInvisible(locator);
		List<WebElement> elements = this.driver.findElements(locator);
		boolean status = this.doCommand(StateAssertion.DESELECTED, null, elements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Web Element of the specified Parent Locator is deselected on Web Page.
	 * 
	 * @param	parent	Locator of Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is deselected on Web Page.
	 * 			<code>false</code> if Web Element is selected on Web Page.
	 */
	
	public final boolean seeDeselected(By parent, By child) {
		WebElement parentElement = this.elementFactory.createElement(parent);
		List<WebElement> childElements = parentElement.findElements(child);
		boolean status = this.doCommand(StateAssertion.DESELECTED, null, childElements.size());
		return status;
	}
	
	/**
	 * Verifies if Web Element of specified Locator within the context of the
	 * Parent Web Element is deselected on Web Page.
	 * 
	 * @param	parent	Parent Web Element.
	 * @param	child	Locator of Child Web Element to check.
	 * @return	<code>true</code> if Web Element is deselected on Web Page.
	 * 			<code>false</code> if Web Element is selected on Web Page.
	 */
	
	public final boolean seeDeselected(WebElement parent, By child) {
		List<WebElement> childElements = parent.findElements(child);
		boolean status = this.doCommand(StateAssertion.DESELECTED, null, childElements.size());
		return status;
	}
	
}