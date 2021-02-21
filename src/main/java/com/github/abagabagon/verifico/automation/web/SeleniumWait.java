package com.github.abagabagon.verifico.automation.web;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWait {
	
	private Logger log;
	private WebDriverWait wait;
	private Alert alert;
	
	public SeleniumWait(WebDriverWait wait) {
		this.log = LogManager.getLogger(this.getClass());
		this.wait = wait;
	}
	
	/**
	 * Waits for Page URL Value to be the same as expected value.
	 * 
	 * @param	expectedUrl Expected Page URL to compare into
	 * @return	<code>true</code> if URL is same as expected value.
	 * 			<code>false</code> if URL is different from expected value.
	 */

	final boolean waitForUrlToBe(String expectedUrl) {
		this.log.trace("Waiting for Page URL to match expected value.");
		boolean isUrlEqual = false;
		try {
			isUrlEqual = this.wait.until(ExpectedConditions.urlToBe(expectedUrl));
			this.log.trace("Page URL had matched the expected value!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Page URL to match the expected URL Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Page URL to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isUrlEqual;
	}
	
	/**
	 * Waits for Page Title Value to be the same as expected value.
	 * 
	 * @param	expectedTitle Expected Page Title to compare into
	 * @return	<code>true</code> if Page Title is same as expected value.
	 * 			<code>false</code> if Page Title is different from expected value.
	 */

	final boolean waitForTitleToBe(String expectedTitle) {
		this.log.trace("Waiting for Page Title to match expected value.");
		boolean isTitleEqual = false;
		try {
			isTitleEqual = this.wait.until(ExpectedConditions.titleIs(expectedTitle));
			this.log.trace("Page Title had matched the expected value!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Page Title to match the expected Title Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Page Title to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isTitleEqual;
	}
	
	/**
	 * Waits for WebElement to be present at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created Web Element
	 */

	final WebElement waitForObjectToBePresent(By locator) {
		this.log.trace("Waiting for Web Element to be present.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			this.log.trace("Web Element had become present!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be present has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for element to be present!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}
	
	/**
	 * Waits for WebElements to be present at the Web Page.
	 * 
	 * @param	locator Object used to locate elements to wait for.
	 * @return	Created List of Web Elements
	 */

	final List<WebElement> waitForObjectsToBePresent(By locator) {
		this.log.trace("Waiting for Web Elements to be present.");
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			this.log.trace("Web Element had become present!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Elements to be present has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for elements to be present!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}
	
	/**
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created Web Element
	 */

	final WebElement waitForObjectToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			this.log.trace("Web Element had become visible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}
	
	/**
	 * Waits for WebElements to be visible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created List of Web Elements
	 */
	
	final List<WebElement> waitForObjectsToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			this.log.trace("Web Element had become visible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}

	/**
	 * Waits for WebElement to be clickable at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created Web Element
	 */

	WebElement waitForObjectToBeClickable(By locator) {
		this.log.trace("Waiting for Web Element to be clickable.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable(locator));
			this.log.trace("Web Element had become clickable!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be clickable has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be clickable!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}

	/**
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	<code>true</code> if Web Element became invisible.
	 * 			<code>false</code> if Web Element did not become invisible.	
	 */

	final boolean waitForObjectToBeInvisible(By locator) {
		this.log.trace("Waiting for Web Element to be invisible.");
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			this.log.trace("Web Element had become invisible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be invisible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be invisible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isVisible;
	}
	
	/**
	 * Waits for Text Box/Area Value to be the same as expected value.
	 * 
	 * @param 	locator			By Object used to locate element to wait for.
	 * @param 	expectedValue	Expected Text Box/Area Value to compare into
	 * @return	<code>true</code> if URL is same as expected value.
	 * 			<code>false</code> if URL is different from expected value.	
	 */

	final boolean waitForValueToBe(By locator, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to match expected value.");
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeContains(locator, "value", expectedValue));
			this.log.trace("Text Box/Area Value had matched the expected value!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Box/Area Value to match the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Box/Area Value to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param	locator        Target Object for checking of selection state.
	 * @param	selectionState expected selection state.
	 * @return	<code>true</code> if Selection State is same as expected value.
	 * 			<code>false</code> if Selection State is different from expected value.	
	 */

	final boolean waitForObjectSelectionStateToBe(By locator, boolean expectedSelectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + expectedSelectionState + "!");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe(locator, expectedSelectionState));
			this.log.trace("Web Element Selection State is " + expectedSelectionState + "!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return status;
	}
	
	/**
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created Web Element
	 */

	final WebElement waitForObjectToBeVisible(WebElement element) {
		this.log.trace("Waiting for Web Element to be visible.");
		try {
			element = this.wait.until(ExpectedConditions.visibilityOf(element));
			this.log.trace("Web Element had become visible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}
	
	/**
	 * Waits for WebElements to be visible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created List of Web Elements
	 */
	
	final List<WebElement> waitForObjectsToBeVisible(List<WebElement> elements) {
		this.log.trace("Waiting for Web Element to be visible.");
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfAllElements(elements));
			this.log.trace("Web Element had become visible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}

	/**
	 * Waits for WebElement to be clickable at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	Created Web Element
	 */

	WebElement waitForObjectToBeClickable(WebElement element) {
		this.log.trace("Waiting for Web Element to be clickable.");
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable(element));
			this.log.trace("Web Element had become clickable!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be clickable has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be clickable!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}

	/**
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param	locator Object used to locate element to wait for.
	 * @return	<code>true</code> if Web Element became invisible.
	 * 			<code>false</code> if Web Element did not become invisible.	
	 */

	final boolean waitForObjectToBeInvisible(WebElement element) {
		this.log.trace("Waiting for Web Element to be invisible.");
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOf(element));
			this.log.trace("Web Element had become invisible!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be invisible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be invisible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isVisible;
	}
	
	/**
	 * Waits for Text Box/Area Value to be the same as expected value.
	 * 
	 * @param 	locator			By Object used to locate element to wait for.
	 * @param 	expectedValue	Expected Text Box/Area Value to compare into
	 * @return	<code>true</code> if URL is same as expected value.
	 * 			<code>false</code> if URL is different from expected value.	
	 */

	final boolean waitForValueToBe(WebElement element, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to match expected value.");
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeToBe(element, "value", expectedValue));
			this.log.trace("Text Box/Area Value had matched the expected value!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Box/Area Value to match the expected URL Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Box/Area Value to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param	locator        Target Object for checking of selection state.
	 * @param	selectionState expected selection state.
	 * @return	<code>true</code> if Selection State is same as expected value.
	 * 			<code>false</code> if Selection State is different from expected value.	
	 */

	final boolean waitForObjectSelectionStateToBe(WebElement element, boolean expectedSelectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + expectedSelectionState + "!");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe(element, expectedSelectionState));
			this.log.trace("Web Element Selection State is " + expectedSelectionState + "!");
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return status;
	}

	/**
	 * Waits for a Javascript Alert to be present on the WebPage.
	 * 
	 * @return Alert Object
	 */

	final Alert waitForAlertToBePresent() {
		try {
			this.alert = this.wait.until(ExpectedConditions.alertIsPresent());
		} catch (NullPointerException e) {
			this.log.fatal("WebDriverWait is not initialized!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (TimeoutException e) {
			this.log.error("Wait time for Alert to be displayed has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while getting Alert Message!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return this.alert;
	}

}
