package com.github.abagabagon.verifico.automation.web;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ValueAssertions extends Commands {

	private WebElementFactory elementFactory;
	private String actualValue;

	private enum ValueAssertion {
		URL, PARTIAL_URL, TITLE, PARTIAL_TITLE, ATTRIBUTE, PARTIAL_ATTRIBUTE, DROPDOWN, PARTIAL_DROPDOWN, TEXT, PARTIAL_TEXT, ALERT_MESSAGE
	}

	public ValueAssertions(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
		this.elementFactory = new WebElementFactory(this.wait);
	}

	private boolean isValueDisplayed(ValueAssertion valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = false;
		Select select = null;
		switch(valueAssertionAction) {
		case URL:
			this.wait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.equals(value);
			break;
		case TITLE:
			this.wait.waitForPage();
			this.actualValue = this.driver.getTitle().trim();
			status = this.actualValue.equals(value);
			break;
		case ATTRIBUTE:
			this.actualValue = element.getAttribute(attribute);
			if (this.actualValue != null) {
				status = this.actualValue.equals(value);
			} else {
				status = false;
			}
			break;
		case DROPDOWN:
			select = new Select(element);
			this.actualValue = select.getFirstSelectedOption().getText();
			status = this.actualValue.equals(value);
			break;
		case TEXT:
			this.actualValue = element.getText();
			status = this.actualValue.equals(value);
			break;
		case PARTIAL_URL:
			this.wait.waitForPage();
			this.actualValue = this.driver.getCurrentUrl().trim();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_TITLE:
			this.wait.waitForPage();
			this.actualValue = this.driver.getTitle().trim();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_ATTRIBUTE:
			this.actualValue = element.getAttribute(attribute);
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_DROPDOWN:
			select = new Select(element);
			this.actualValue = select.getFirstSelectedOption().getText().toLowerCase();
			status = this.actualValue.contains(value);
			break;
		case PARTIAL_TEXT:
			this.actualValue = element.getText();
			status = this.actualValue.contains(value);
			break;
		case ALERT_MESSAGE:
			Alert alert = this.wait.waitForAlertToBePresent();
			this.actualValue = alert.getText().trim();
			status = this.actualValue.equals(value);
		default:
			this.log.fatal("Unsupported Value Assertion Action.");
		}
		return status;
	}

	private boolean isEqual(ValueAssertion valueAssertionAction, WebElement element, String attribute, String expectedValue) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, expectedValue);
		if(status) {
			this.log.debug("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\".");
		} else {
			this.log.error("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + expectedValue + "\". Actual value is \"" + this.actualValue + "\".");
		}

		return status;
	}

	private boolean isNotEqual(ValueAssertion valueAssertionAction, WebElement element, String attribute, String value) {
		boolean status = this.isValueDisplayed(valueAssertionAction, element, attribute, value);
		if(status) {
			this.log.error("I saw " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\".");
		} else {
			this.log.debug("I didn't see " + String.valueOf(valueAssertionAction) + " Value: \"" + value + "\". Actual value is \"" + this.actualValue + "\".");
		}

		return !status;
	}

	/**
	 * Verifies Page URL of Web Page if equal to the expected URL.
	 *
	 * @param url Expected Page URL to compare into
	 * @return <code>true</code> if Page URL is equal to expected URL.
	 *         <code>false</code> if Page URL is not equal to expected URL.
	 */

	public final boolean seeUrl(String url) {
		boolean status = this.isEqual(ValueAssertion.URL, null, null, url);
		return status;
	}

	/**
	 * Verifies Page URL of Web Page if not equal to the specified URL.
	 *
	 * @param url Page URL to compare into
	 * @return <code>true</code> if Page URL is not equal to specified URL.
	 *         <code>false</code> if Page URL is equal to specified URL.
	 */

	public final boolean dontSeeUrl(String url) {
		boolean status = this.isNotEqual(ValueAssertion.URL, null, null, url);
		return status;
	}

	/**
	 * Verifies Page URL of Web Page if partially equal to the expected URL.
	 *
	 * @param partialUrl Expected Partial Page URL to compare into
	 * @return <code>true</code> if Page URL is partially equal to expected URL.
	 *         <code>false</code> if Page URL is not partially equal to expected
	 *         URL.
	 */

	public final boolean seePartialUrl(String partialUrl) {
		boolean status = this.isEqual(ValueAssertion.PARTIAL_URL, null, null, partialUrl);
		return status;
	}

	/**
	 * Verifies Page URL of Web Page if not partially equal to the specified URL.
	 *
	 * @param partialUrl Expected Partial Page URL to compare into
	 * @return <code>true</code> if Page URL is not partially equal to specified
	 *         URL. <code>false</code> if Page URL is partially equal to specified
	 *         URL.
	 */

	public final boolean dontSeePartialUrl(String partialUrl) {
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_URL, null, null, partialUrl);
		return status;
	}

	/**
	 * Verifies Page Title of Web Page if equal to the expected Title.
	 *
	 * @param title Expected Page Title to compare into
	 * @return <code>true</code> if Page Title is equal to expected Title.
	 *         <code>false</code> if Page Title is not equal to expected Title.
	 */

	public final boolean seeTitle(String title) {
		boolean status = this.isEqual(ValueAssertion.TITLE, null, null, title);
		return status;
	}

	/**
	 * Verifies Page Title of Web Page if not equal to the specified Title.
	 *
	 * @param title Page Title to compare into
	 * @return <code>true</code> if Page Title is not equal to specified Title.
	 *         <code>false</code> if Page Title is equal to specified Title.
	 */

	public final boolean dontSeeTitle(String title) {
		boolean status = this.isNotEqual(ValueAssertion.TITLE, null, null, title);
		return status;
	}

	/**
	 * Verifies Page Title of Web Page if partially equal to the expected Title.
	 *
	 * @param partialTitle Expected Page Title to compare into
	 * @return <code>true</code> if Page Title is partially equal to expected Title.
	 *         <code>false</code> if Page Title is not partially equal to expected
	 *         Title.
	 */

	public final boolean seePartialTitle(String partialTitle) {
		boolean status = this.isEqual(ValueAssertion.PARTIAL_TITLE, null, null, partialTitle);
		return status;
	}

	/**
	 * Verifies Page Title of Web Page if not partially equal to the specified
	 * Title.
	 *
	 * @param partialTitle Page Title to compare into
	 * @return <code>true</code> if Page Title is not partially equal to specified
	 *         Title. <code>false</code> if Page Title is partially equal to
	 *         specified Title.
	 */

	public final boolean dontSeePartialTitle(String partialTitle) {
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_TITLE, null, null, partialTitle);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Locator if
	 * equal to the expected value.
	 *
	 * @param locator   Object used to locate Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected attribute value.
	 *         <code>false</code> if value is not equal to expected attribute value.
	 */

	public final boolean seeAttributeValue(By locator, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Web Element of the specified Parent Locator
	 * if equal to the expected value.
	 *
	 * @param parent    Locator of the Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected attribute value.
	 *         <code>false</code> if value is not equal to expected attribute value.
	 */

	public final boolean seeAttributeValue(By parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Parent Web Element Locator if equal to the
	 * expected value.
	 *
	 * @param parent    Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected attribute value.
	 *         <code>false</code> if value is not equal to expected attribute value.
	 */

	public final boolean seeAttributeValue(WebElement parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Locator if
	 * not equal to the expected value.
	 *
	 * @param locator   Object used to locate Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected attribute value.
	 *         <code>false</code> if value is equal to expected attribute value.
	 */

	public final boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Web Element of the specified Parent Locator
	 * if not equal to the expected value.
	 *
	 * @param parent    Locator of the Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected attribute value.
	 *         <code>false</code> if value is equal to expected attribute value.
	 */

	public final boolean dontSeeAttributeValue(By parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Parent Web Element Locator if equal to the
	 * expected value.
	 *
	 * @param parent    Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected attribute value.
	 *         <code>false</code> if value is equal to expected attribute value.
	 */

	public final boolean dontSeeAttributeValue(WebElement parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Locator if
	 * partially equal to the expected value.
	 *
	 * @param locator   Object used to locate Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected partial value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected attribute
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         attribute value.
	 */

	public final boolean seePartialAttributeValue(By locator, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Web Element of the specified Parent Locator
	 * if partially equal to the expected value.
	 *
	 * @param parent    Locator of the Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected partial value of the Child Web Element Text
	 *                  Dropdown.
	 * @return <code>true</code> if value is partially equal to expected attribute
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         attribute value.
	 */

	public final boolean seePartialAttributeValue(By parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Parent Web Element Locator if partially
	 * equal to the expected value.
	 *
	 * @param parent    Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Expected partial value of the Child Web Element Text
	 *                  Dropdown.
	 * @return <code>true</code> if value is partially equal to expected attribute
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         attribute value.
	 */

	public final boolean seePartialAttributeValue(WebElement parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Locator if
	 * not partially equal to the expected value.
	 *
	 * @param locator   Object used to locate Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         attribute value. <code>false</code> if value is partially equal to
	 *         expected attribute value.
	 */

	public final boolean dontSeePartialAttributeValue(By locator, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Web Element of the specified Parent Locator
	 * if not partially equal to the expected value.
	 *
	 * @param parent    Locator of the Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         attribute value. <code>false</code> if value is partially equal to
	 *         expected attribute value.
	 */

	public final boolean dontSeePartialAttributeValue(By parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the attribute value of the Web Element of the specified Child
	 * Locator within the context of the Parent Web Element Locator if partially
	 * equal to the expected value.
	 *
	 * @param parent    Parent Web Element
	 * @param child     Locator of the Child Web Element to assert the attribute
	 *                  value from.
	 * @param attribute Name of attribute to assert the value from.
	 * @param value     Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         attribute value. <code>false</code> if value is partially equal to
	 *         expected attribute value.
	 */

	public final boolean dontSeePartialAttributeValue(WebElement parent, By child, String attribute, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_ATTRIBUTE, element, attribute, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Locator if equal
	 * to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the text value
	 *                from.
	 * @param value   Expected value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected text value.
	 *         <code>false</code> if value is not equal to expected text value.
	 */

	public final boolean seeText(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if
	 * equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected text value.
	 *         <code>false</code> if value is not equal to expected text value.
	 */

	public final boolean seeText(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if equal to the expected
	 * value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected text value.
	 *         <code>false</code> if value is not equal to expected text value.
	 */

	public final boolean seeText(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Locator if not
	 * equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the text value
	 *                from.
	 * @param value   Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected text value.
	 *         <code>false</code> if value is equal to expected text value.
	 */

	public final boolean dontSeeText(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if not
	 * equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected text value.
	 *         <code>false</code> if value is equal to expected text value.
	 */

	public final boolean dontSeeText(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if equal to the expected
	 * value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected text value.
	 *         <code>false</code> if value is equal to expected text value.
	 */

	public final boolean dontSeeText(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Locator if
	 * partially equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the text value
	 *                from.
	 * @param value   Expected partial value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected text value.
	 *         <code>false</code> if value is not partially equal to expected text
	 *         value.
	 */

	public final boolean seePartialText(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if
	 * partially equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Expected partial value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected text value.
	 *         <code>false</code> if value is not partially equal to expected text
	 *         value.
	 */

	public final boolean seePartialText(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if partially equal to
	 * the expected value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Expected partial value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected text value.
	 *         <code>false</code> if value is not partially equal to expected text
	 *         value.
	 */

	public final boolean seePartialText(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Locator if not
	 * partially equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the text value
	 *                from.
	 * @param value   Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected text
	 *         value. <code>false</code> if value is partially equal to expected
	 *         text value.
	 */

	public final boolean dontSeePartialText(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if not
	 * partially equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected text
	 *         value. <code>false</code> if value is partially equal to expected
	 *         text value.
	 */

	public final boolean dontSeePartialText(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the text value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if partially equal to
	 * the expected value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the text value from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected text
	 *         value. <code>false</code> if value is partially equal to expected
	 *         text value.
	 */

	public final boolean dontSeePartialText(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_TEXT, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Locator if
	 * equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the dropdown value
	 *                from.
	 * @param value   Expected value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected dropdown value.
	 *         <code>false</code> if value is not equal to expected dropdown value.
	 */

	public final boolean seeDropdownValue(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if
	 * equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected dropdown value.
	 *         <code>false</code> if value is not equal to expected dropdown value.
	 */

	public final boolean seeDropdownValue(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if equal to the expected
	 * value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Expected value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is equal to expected dropdown value.
	 *         <code>false</code> if value is not equal to expected dropdown value.
	 */

	public final boolean seeDropdownValue(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Locator if
	 * not equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the dropdown value
	 *                from.
	 * @param value   Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected dropdown value.
	 *         <code>false</code> if value is equal to expected dropdown value.
	 */

	public final boolean dontSeeDropdownValue(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if not
	 * equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected dropdown value.
	 *         <code>false</code> if value is equal to expected dropdown value.
	 */

	public final boolean dontSeeDropdownValue(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if equal to the expected
	 * value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not equal to expected dropdown value.
	 *         <code>false</code> if value is equal to expected dropdown value.
	 */

	public final boolean dontSeeDropdownValue(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Locator if
	 * partially equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the dropdown value
	 *                from.
	 * @param value   Expected partial value of the Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected dropdown
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         dropdown value.
	 */

	public final boolean seePartialDropdownValue(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if
	 * partially equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Expected partial value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected dropdown
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         dropdown value.
	 */

	public final boolean seePartialDropdownValue(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if partially equal to
	 * the expected value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Expected partial value of the Child Web Element Text Dropdown.
	 * @return <code>true</code> if value is partially equal to expected dropdown
	 *         value. <code>false</code> if value is not partially equal to expected
	 *         dropdown value.
	 */

	public final boolean seePartialDropdownValue(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Locator if
	 * not partially equal to the expected value.
	 *
	 * @param locator Object used to locate Web Element to assert the dropdown value
	 *                from.
	 * @param value   Value of the Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         dropdown value. <code>false</code> if value is partially equal to
	 *         expected dropdown value.
	 */

	public final boolean dontSeePartialDropdownValue(By locator, String value) {
		WebElement element = this.elementFactory.createElement(locator);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Web Element of the specified Parent Locator if not
	 * partially equal to the expected value.
	 *
	 * @param parent Locator of the Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         dropdown value. <code>false</code> if value is partially equal to
	 *         expected dropdown value.
	 */

	public final boolean dontSeePartialDropdownValue(By parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies the dropdown value of the Web Element of the specified Child Locator
	 * within the context of the Parent Web Element Locator if partially equal to
	 * the expected value.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of the Child Web Element to assert the dropdown value
	 *               from.
	 * @param value  Value of the Child Web Element Text Dropdown to check.
	 * @return <code>true</code> if value is not partially equal to expected
	 *         dropdown value. <code>false</code> if value is partially equal to
	 *         expected dropdown value.
	 */

	public final boolean dontSeePartialDropdownValue(WebElement parent, By child, String value) {
		WebElement element = this.elementFactory.createElement(parent, child);
		boolean status = this.isNotEqual(ValueAssertion.PARTIAL_DROPDOWN, element, null, value);
		return status;
	}

	/**
	 * Verifies Javascript Alert Message displayed if equal to expected message.
	 *
	 * @param message Expected message displayed.
	 * @return <code>true</code> if message is equal to expected alert message.
	 *         <code>false</code> if message is not equal to expected alert message.
	 */

	public final boolean seeAlertMessage(String message) {
		boolean status = this.isEqual(ValueAssertion.ALERT_MESSAGE, null, null, message);
		return status;
	}

	/**
	 * Verifies Web Element Instance count is equal to expected count.
	 *
	 * @param locator Object used to locate Web Element to assert.
	 * @param count   Expected Web Element instance count.
	 * @return <code>true</code> if count is equal to expected count.
	 *         <code>false</code> if count is not equal to expected count.
	 */

	public final boolean counted(By locator, int count) {
		boolean isEqual = this.wait.waitForElementCountToBe(locator, count);
		int size = this.driver.findElements(locator).size();
		boolean status = false;
		if (isEqual) {
			this.log.debug("I verified count of Web Element: \"" + locator.toString() + "\" is \"" + count + "\".");
			status = true;
		} else {
			this.log.error("I verified count of Web Element: \"" + locator.toString() + "\" is not \"" + count + "\". Actual count is \"" + size + "\".");
		}
		return status;
	}

}