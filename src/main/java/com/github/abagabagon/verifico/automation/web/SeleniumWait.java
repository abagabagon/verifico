package com.github.abagabagon.verifico.automation.web;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWait {

	private Logger log;
	private WebDriverWait wait;
	private Alert alert;

	public SeleniumWait(WebDriver driver, WebDriverWait wait) {
		this.log = LogManager.getLogger(this.getClass());
		if (wait == null) {
			this.log.fatal("WebDriverWait is NULL.");
			System.exit(1);
		} else {
			this.wait = wait;
		}
	}
	
	/**
	 * Waits for Page to be fully loaded.
	 * 
	 * @return <code>true</code> if URL is same as expected value.
	 *         <code>false</code> if URL is different from expected value.
	 */

	final boolean waitForPage() {
		this.log.trace("Waiting for Page to fully load.");
		boolean isCountEqual = false;
		try {
			this.wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
					String script = "return document.readyState";  
					return javascriptExecutor.executeScript(script).equals("complete");
				}
			});
			this.log.trace("Page successfully loaded!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Object Count to match the expected Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Object Count to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isCountEqual;
	}

	/**
	 * Waits for Page URL Value to be the same as expected value.
	 * 
	 * @param expectedUrl Expected Page URL to compare into
	 * @return <code>true</code> if URL is same as expected value.
	 *         <code>false</code> if URL is different from expected value.
	 */

	final boolean waitForUrlToBe(String expectedUrl) {
		this.log.trace("Waiting for Page URL to match expected value.");
		this.waitForPage();
		boolean isUrlEqual = false;
		try {
			isUrlEqual = this.wait.until(ExpectedConditions.urlToBe(expectedUrl));
			this.log.trace("Page URL had matched the expected value!");
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
	 * Waits for Page URL Value to contain the expected value.
	 * 
	 * @param expectedUrl Expected Page URL to compare into
	 * @return <code>true</code> if URL contains the expected value.
	 *         <code>false</code> if URL does not contain the expected value.
	 */

	final boolean waitForUrlToContain(String expectedUrl) {
		this.log.trace("Waiting for Page URL to contain expected value.");
		this.waitForPage();
		boolean isUrlEqual = false;
		try {
			isUrlEqual = this.wait.until(ExpectedConditions.urlContains(expectedUrl));
			this.log.trace("Page URL contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Page URL to contain the expected URL Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Page URL to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isUrlEqual;
	}

	/**
	 * Waits for Page Title Value to be the same as expected value.
	 * 
	 * @param expectedTitle Expected Page Title to compare into
	 * @return <code>true</code> if Page Title is same as expected value.
	 *         <code>false</code> if Page Title is different from expected value.
	 */

	final boolean waitForTitleToBe(String expectedTitle) {
		this.log.trace("Waiting for Page Title to match expected value.");
		this.waitForPage();
		boolean isTitleEqual = false;
		try {
			isTitleEqual = this.wait.until(ExpectedConditions.titleIs(expectedTitle));
			this.log.trace("Page Title had matched the expected value!");
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
	 * Waits for Page Title Value to contain the expected value.
	 * 
	 * @param expectedTitle Expected Page Title to compare into
	 * @return <code>true</code> if Page Title contains the expected value.
	 *         <code>false</code> if Page Title does not contain the expected value.
	 */

	final boolean waitForTitleToContain(String expectedTitle) {
		this.log.trace("Waiting for Page Title to contain expected value.");
		this.waitForPage();
		boolean isTitleEqual = false;
		try {
			isTitleEqual = this.wait.until(ExpectedConditions.titleContains(expectedTitle));
			this.log.trace("Page Title contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Page Title to contain the expected Title Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Page Title to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isTitleEqual;
	}

	/**
	 * Waits for WebElement to be present at the Web Page.
	 * 
	 * @param locator Object used to locate element to wait for.
	 * @return Created Web Element
	 */

	final WebElement waitForObjectToBePresent(By locator) {
		this.log.trace("Waiting for Web Element to be present.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			this.log.trace("Web Element had become present!");
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
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param locator Object used to locate element to wait for.
	 * @return Created Web Element
	 */

	final WebElement waitForObjectToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			this.log.trace("Web Element had become visible!");
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
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param element Web Element used to locate element to wait for.
	 * @return Created Web Element
	 */

	final WebElement waitForObjectToBeVisible(WebElement element) {
		this.log.trace("Waiting for Web Element to be visible.");
		this.waitForPage();
		try {
			element = this.wait.until(ExpectedConditions.visibilityOf(element));
			this.log.trace("Web Element had become visible!");
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
	 * Waits for WebElement to be present at the Web Page.
	 * 
	 * @param parent Object used to locate parent element to wait for.
	 * @param child  Object used to locate child element to wait for.
	 * @return Created Web Element
	 */

	final WebElement waitForNestedObjectToBePresent(By parent, By child) {
		this.log.trace("Waiting for Nested Web Element to be present.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, child));
			this.log.trace("Nested Web Element had become present!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element to be present has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element to be present!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}

	/**
	 * Waits for WebElement to be present at the Web Page.
	 * 
	 * @param parent Web Element used to locate parent element to wait for.
	 * @param child  Object used to locate child element to wait for.
	 * @return Created Web Element
	 */

	final WebElement waitForNestedObjectToBePresent(WebElement parent, By child) {
		this.log.trace("Waiting for Nested Web Element to be present.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, child));
			this.log.trace("Nested Web Element had become present!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element to be present has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element to be present!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}
	
	final WebElement waitForNestedObjectToBeVisible(By parent, By child, int index) {
		this.log.trace("Waiting for Nested Web Element to be visible.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					List<WebElement> parentElement = driver.findElements(parent);
					WebElement childElement = parentElement.get(index).findElement(child);
					return childElement;
				}
			});
			this.log.trace("Nested Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}

	/**
	 * Waits for WebElements to be present at the Web Page.
	 * 
	 * @param locator Object used to locate elements to wait for.
	 * @return Created List of Web Elements
	 */

	final List<WebElement> waitForObjectsToBePresent(By locator) {
		this.log.trace("Waiting for Web Elements to be present.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			this.log.trace("Web Element had become present!");
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
	 * Waits for Web Elements to be present at the Web Page.
	 * 
	 * @param parent Object used to locate parent element to wait for.
	 * @param child  Object used to locate child element to wait for.
	 * @return Created List Web Element
	 */

	final List<WebElement> waitForNestedObjectsToBePresent(By parent, By child) {
		this.log.trace("Waiting for Nested Web Element to be present.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(parent, child));
			this.log.trace("Nested Web Element List had become present!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element List to be present has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element List to be present!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}

	/**
	 * Waits for Web Elements to be visible at the Web Page.
	 * 
	 * @param parent Object used to locate parent element to wait for.
	 * @param child  Object used to locate child element to wait for.
	 * @return Created List Web Element
	 */

	final List<WebElement> waitForNestedObjectsToBeVisible(By parent, By child) {
		this.log.trace("Waiting for Nested Web Element to be visible.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, child));
			this.log.trace("Nested Web Element List had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element List to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element List to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}
	
	final List<WebElement> waitForNestedObjectsToBeVisible(By parent, By child, int index) {
		this.log.trace("Waiting for Nested List Web Element to be visible.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(new ExpectedCondition<List<WebElement>>() {
				@Override
				public List<WebElement> apply(WebDriver driver) {
					List<WebElement> parentElement = driver.findElements(parent);
					List<WebElement> childElement = parentElement.get(index).findElements(child);
					return childElement;
				}
			});
			this.log.trace("Nested List Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested List Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested List Web Element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}

	/**
	 * Waits for Web Elements to be visible at the Web Page.
	 * 
	 * @param parent Web Element used to locate parent element to wait for.
	 * @param child  Object used to locate child element to wait for.
	 * @return Created List Web Element
	 */

	final List<WebElement> waitForNestedObjectsToBeVisible(WebElement parent, By child) {
		this.log.trace("Waiting for Nested Web Element to be visible.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, child));
			this.log.trace("Nested Web Element List had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element List to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element List to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return elements;
	}



	/**
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param locator Object used to locate element to wait for.
	 * @return <code>true</code> if Web Element became invisible. <code>false</code>
	 *         if Web Element did not become invisible.
	 */

	final boolean waitForObjectToBeInvisible(By locator) {
		this.log.trace("Waiting for Web Element to be invisible.");
		this.waitForPage();
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			this.log.trace("Web Element had become invisible!");
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
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param element Web Element used to locate element to wait for.
	 * @return <code>true</code> if Web Element became invisible. <code>false</code>
	 *         if Web Element did not become invisible.
	 */

	final boolean waitForObjectToBeInvisible(WebElement element) {
		this.log.trace("Waiting for Web Element to be invisible.");
		this.waitForPage();
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOf(element));
			this.log.trace("Web Element had become invisible!");
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
	 * Waits for WebElements to be visible at the Web Page.
	 * 
	 * @param locator Object used to locate element to wait for.
	 * @return Created List of Web Elements
	 */

	final List<WebElement> waitForObjectsToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		this.waitForPage();
		List<WebElement> elements = null;
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			this.log.trace("Web Element had become visible!");
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
	 * Waits for WebElements to be visible at the Web Page.
	 * 
	 * @param elements Web Element List used to locate element to wait for.
	 * @return Created List of Web Elements
	 */

	final List<WebElement> waitForObjectsToBeVisible(List<WebElement> elements) {
		this.log.trace("Waiting for Web Element to be visible.");
		this.waitForPage();
		try {
			elements = this.wait.until(ExpectedConditions.visibilityOfAllElements(elements));
			this.log.trace("Web Element had become visible!");
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
	 * @param locator Object used to locate element to wait for.
	 * @return Created Web Element
	 */

	WebElement waitForObjectToBeClickable(By locator) {
		this.log.trace("Waiting for Web Element to be clickable.");
		this.waitForPage();
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable(locator));
			this.log.trace("Web Element had become clickable!");
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
	 * Waits for WebElement to be clickable at the Web Page.
	 * 
	 * @param element Web Element used to locate element to wait for.
	 * @return Created Web Element
	 */

	WebElement waitForObjectToBeClickable(WebElement element) {
		this.log.trace("Waiting for Web Element to be clickable.");
		this.waitForPage();
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable(element));
			this.log.trace("Web Element had become clickable!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be clickable has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be clickable!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}
	
	final List<WebElement> waitForTableRowsToBeVisible(By locator) {
		this.log.trace("Waiting for Table Rows to be visible.");
		this.waitForPage();
		List<WebElement> element = null;
		try {
			element = this.wait.until(new ExpectedCondition<List<WebElement>>() {
				@Override
				public List<WebElement> apply(WebDriver driver) {
					List<WebElement> elements = null;
					int count = 0;
					int size = 0;
					int previousSize = 0;
					while(count != 10) {
						elements = driver.findElements(locator);
						size = elements.size();
						if (size == previousSize) {
							count++;
						} else {
							count = 0;
						}
						previousSize = size;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							ExceptionUtils.getStackTrace(e);
						}
					}
					return elements;
				}
			});
			this.log.trace("Nested Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Nested Web Element to be visible has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to wait for Nested Web Element to be visible!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return element;
	}

	/**
	 * Waits for Text Box/Area Value to be the same as expected value.
	 * 
	 * @param locator       By Object used to locate element to wait for.
	 * @param expectedValue Expected Text Box/Area Value to compare into
	 * @return <code>true</code> if Value is same as expected value.
	 *         <code>false</code> if Value is different from expected value.
	 */

	final boolean waitForValueToBe(By locator, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to match expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeToBe(locator, "value", expectedValue));
			this.log.trace("Text Box/Area Value had matched the expected value!");
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
	 * Waits for Text Box/Area Value to be the same as expected value.
	 * 
	 * @param element       Web Element used to locate element to wait for.
	 * @param expectedValue Expected Text Box/Area Value to compare into
	 * @return <code>true</code> if Value is same as expected value.
	 *         <code>false</code> if Value is different from expected value.
	 */

	final boolean waitForValueToBe(WebElement element, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to match expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeToBe(element, "value", expectedValue));
			this.log.trace("Text Box/Area Value had matched the expected value!");
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
	 * Waits for Text Box/Area Value to contain the expected value.
	 * 
	 * @param locator       By Object used to locate element to wait for.
	 * @param expectedValue Expected Text Box/Area Value to compare into
	 * @return <code>true</code> if Value contains the expected value.
	 *         <code>false</code> if Value does not contain expected value.
	 */

	final boolean waitForValueToContain(By locator, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to contain expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeContains(locator, "value", expectedValue));
			this.log.trace("Text Box/Area Value contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Box/Area Value to contain the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Box/Area Value to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for Text Box/Area Value to contain the expected value.
	 * 
	 * @param element       Web Element used to locate element to wait for.
	 * @param expectedValue Expected Text Box/Area Value to compare into
	 * @return <code>true</code> if Value contains the expected value.
	 *         <code>false</code> if Value does not contain the expected value.
	 */

	final boolean waitForValueToContain(WebElement element, String expectedValue) {
		this.log.trace("Waiting for Text Box/Area Value to contain expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.attributeContains(element, "value", expectedValue));
			this.log.trace("Text Box/Area Value contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Box/Area Value to contain the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Box/Area Value to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for Text Value to be the same as expected value.
	 * 
	 * @param locator       By Object used to locate element to wait for.
	 * @param expectedValue Expected Text Value to compare into
	 * @return <code>true</code> if Text Value is same as expected value.
	 *         <code>false</code> if Text Value is different from expected value.
	 */

	final boolean waitForTextToBe(By locator, String expectedValue) {
		this.log.trace("Waiting for Text Value to match expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.textToBe(locator, expectedValue));
			this.log.trace("Text Value had matched the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Value to match the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Value to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for Text Value to be the same as expected value.
	 * 
	 * @param element       Web Element used to locate element to wait for.
	 * @param expectedValue Expected Text Value to compare into
	 * @return <code>true</code> if Text Value is same as expected value.
	 *         <code>false</code> if Text Value is different from expected value.
	 */

	final boolean waitForTextToBe(WebElement element, String expectedValue) {
		this.log.trace("Waiting for Text Value to match expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.textToBePresentInElement(element, expectedValue));
			this.log.trace("Text Value had matched the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Value to match the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Value to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for Text Value to contain the expected value.
	 * 
	 * @param locator       By Object used to locate element to wait for.
	 * @param expectedValue Expected Text Value to compare into
	 * @return <code>true</code> if Text Value contains the expected value.
	 *         <code>false</code> if Text Value does not contain expected value.
	 */

	final boolean waitForTextToContain(By locator, String expectedValue) {
		this.log.trace("Waiting for Text Value to contain expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedValue));
			this.log.trace("Text Value contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Value to contain the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Value to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for Text Value to contain the expected value.
	 * 
	 * @param element       Web Element used to locate element to wait for.
	 * @param expectedValue Expected Text Value to compare into
	 * @return <code>true</code> if Text Value contains the expected value.
	 *         <code>false</code> if Text Value does not contain the expected value.
	 */

	final boolean waitForTextToContain(WebElement element, String expectedValue) {
		this.log.trace("Waiting for Text Value to contain expected value.");
		this.waitForPage();
		boolean isValueEqual = false;
		try {
			isValueEqual = this.wait.until(ExpectedConditions.textToBePresentInElement(element, expectedValue));
			this.log.trace("Text Value contains the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Text Value to contain the expected value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Text Value to contain expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isValueEqual;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param locator        Target Object for checking of selection state.
	 * @param selectionState expected selection state.
	 * @return <code>true</code> if Selection State is same as expected value.
	 *         <code>false</code> if Selection State is different from expected
	 *         value.
	 */

	final boolean waitForObjectSelectionStateToBe(By locator, boolean expectedSelectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + expectedSelectionState + "!");
		this.waitForPage();
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe(locator, expectedSelectionState));
			this.log.trace("Web Element Selection State is " + expectedSelectionState + "!");
		} catch (TimeoutException e) {
			this.log.error(
					"Wait time for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be "
					+ expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return status;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param element        Target Web Element for checking of selection state.
	 * @param selectionState expected selection state.
	 * @return <code>true</code> if Selection State is same as expected value.
	 *         <code>false</code> if Selection State is different from expected
	 *         value.
	 */

	final boolean waitForObjectSelectionStateToBe(WebElement element, boolean expectedSelectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + expectedSelectionState + "!");
		this.waitForPage();
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe(element, expectedSelectionState));
			this.log.trace("Web Element Selection State is " + expectedSelectionState + "!");
		} catch (TimeoutException e) {
			this.log.error(
					"Wait time for Web Element Selection State to be " + expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be "
					+ expectedSelectionState + " has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return status;
	}

	/**
	 * Waits for Object Count to be the same as expected count.
	 * 
	 * @param locator Object to count
	 * @param expectedCount Expected Page URL to compare into
	 * @return <code>true</code> if URL is same as expected value.
	 *         <code>false</code> if URL is different from expected value.
	 */

	final boolean waitForCountToBe(By locator, int expectedCount) {
		this.log.trace("Waiting for Object Count to match expected value.");
		this.waitForPage();
		boolean isCountEqual = false;
		try {
			this.wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					int elementCount = driver.findElements(locator).size();
					if (elementCount == expectedCount) {
						return true;
					} else {
						return false;
					}
				}
			});
			this.log.trace("Object Count had matched the expected value!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Object Count to match the expected Value has expired!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Object Count to match expected value!");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return isCountEqual;
	}

	/**
	 * Waits for a Javascript Alert to be present on the WebPage.
	 * 
	 * @return Alert Object
	 */

	final Alert waitForAlertToBePresent() {
		try {
			this.alert = this.wait.until(ExpectedConditions.alertIsPresent());
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
