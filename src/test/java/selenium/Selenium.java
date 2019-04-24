/*******************************************************************************
 * @name    Test
 * @author  Arnel L. Bagabagon Jr.
 * @version 0.1
 * @since   02/03/2019
 * 
 * File contains the implementation of functions (from ITest Interface) in
 * performing tests.
 * 
 ******************************************************************************/

package selenium;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Selenium {

	private Logger log;
	private WebDriver driver;
	private WebDriverWait wait;
	private ExtentTest test;
	private Select select;
	private Alert alert;

	public Selenium(Logger log, WebDriver driver, WebDriverWait wait, ExtentTest test) {
		this.log = log;
		this.driver = driver;
		this.wait = wait;
		this.test = test;
	}
	
	/**
	 * Creates a WebElement
	 * 
	 * @param  locator By Object Locator of the Web Element to be created.
	 * @return Created WebElement
	 */
	
	public final WebElement createElement(By locator) {
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Text cannot be entered at WebElement. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
		}
		return element;
	}
	
	/**
	 * Creates a WebElement from
	 * 
	 * @param  element Web Element from which another Web Element will be created.
	 * @param  locator By Object Locator of the Web Element to be created.
	 * @return Created WebElement
	 */
	
	public final WebElement createElementFromAnElement(WebElement element, By locator) {
		WebElement newElement = wait.until(ExpectedConditions.visibilityOf(element.findElement(locator)));
		return newElement;
	}
	
	/**
	 * Creates a List of WebElements
	 * 
	 * @param  locator By Object Locator of the Web Element to be created.
	 * @return Created WebElement
	 */
	
	public final List<WebElement> createElements(By locator) {
		List<WebElement> elements;
		try {
			elements = this.waitForElementsToBeVisible(locator);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while entering text at WebElement.");
			elements = this.waitForElementsToBeVisible(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Text cannot be entered at WebElement. WebElement does not exist on page.");
			elements = this.waitForElementsToBeVisible(locator);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while entering text at WebElement.");
			elements = this.waitForElementsToBeVisible(locator);
		}
		return elements;
	}
	
	/**** USER ACTIONS ****/
	
	/**
	 * Clicks the element specified. Used for Elements that are clickable (excluding
	 * Radio Buttons & Check Boxes).
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 */

	public final void click(By locator) {
		WebElement element;
		try {
			element = this.waitForElementToBeClickable(locator);
			element.click();
			this.log.debug("I click \"" + this.getXPathFromBy(locator) + "\".");
		} catch (NoSuchElementException e) {
			this.log.warn("WebElement cannot be clicked. WebElement does not exist on page.");
			element = this.waitForElementToBeClickable(locator);
			element.click();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while clicking on WebElement.");
			element = this.waitForElementToBeClickable(locator);
			element.click();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clicking on WebElement.");
			element = this.waitForElementToBeClickable(locator);
			element.click();
		}
	}
	
	/**
	 * Simulates typing into an element, which may set its value. Text entry
	 * elements are INPUT and TEXTAREA elements.
	 * 
	 * @param map       Object containing a WebPage Object's Name, Type, XPath,
	 *                  WebElement & By Locator.
	 * @param inputText Text to enter.
	 */

	public final void fill(By locator, String inputText) {
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
			element.clear();
			element.sendKeys(inputText);
			this.log.debug("I fill \"" + this.getXPathFromBy(locator) + "\" with \"" + inputText + "\"." );
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (NoSuchElementException e) {
			this.log.warn("Text cannot be entered at WebElement. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.sendKeys(inputText);
		}
	}
	
	/**
	 * Clears value of a text area element. Text entry elements are INPUT and
	 * TEXTAREA elements.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 */

	public final void clear(By locator) {
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
			element.clear();
			this.log.debug("I clear \"" + this.getXPathFromBy(locator) + "\".");
		} catch (NoSuchElementException e) {
			this.log.warn("WebElement cannot be cleared. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
			element.clear();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while clearing on WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.clear();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clearing on WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.clear();
		}
	}

	/**
	 * Get the visible innerText of this element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved WebElement Text.
	 */
	
	public final String getText(By locator) {
		String text = null;
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
			text = element.getText();
			if (text.length() > 0) {
				this.log.debug("I get text from \"" + this.getXPathFromBy(locator) + "\".");
			} else {
				this.log.debug("WebElement has no text.");
			}
		} catch (NoSuchElementException e) {
			this.log.warn("WebElement Text cannot be retrieved. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving text from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving text from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		}
		return text;
	}

	/**
	 * Gets the value of the specified WebElement's Attribute.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved value of the WebElement's Attribute.
	 */
	
	public final String getAttribute(By locator, String attributeName) {
		String text = null;
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
			text = element.getAttribute(attributeName);
			if (text.length() > 0) {
				this.log.debug("I get attribute \"" + attributeName + "\" from \"" + this.getXPathFromBy(locator) + "\".");
			} else {
				this.log.debug("Attribute does not exist for the WebElement or the Attribute has no value.");
			}
		} catch (NoSuchElementException e) {
			this.log.warn("Attribute Value cannot be retrieved. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Attribute Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving Attribute Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getText();
		}
		return text;
	}

	/**
	 * Gets the value of the INPUT and TEXTAREA WebElements.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved value of the INPUT/TEXTAREA WebElement.
	 */
	
	public final String getValue(By locator) {
		String text = null;
		WebElement element;
		try {
			element = this.waitForElementToBeVisible(locator);
			text = element.getAttribute("value");
			if (text.length() > 0) {
				this.log.debug("I get value \"" + text + "\" from \"" + this.getXPathFromBy(locator) + "\".");
			} else {
				this.log.debug("The Text Box/Area Web Element  has no value.");
			}
		} catch (NoSuchElementException e) {
			this.log.warn("Text Box Value cannot be retrieved. WebElement does not exist on page.");
			element = this.waitForElementToBeVisible(locator);
			element.getAttribute("value");
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered Exception while retrieving Text Box Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getAttribute("value");
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving Text Box Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getAttribute("value");
		}
		return text;
	}
	
	/**
	 * Gets the selected option of the Drop-down List WebElement.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved value of the Drop-down List WebElement.
	 */

	public final String getDropDownListValue(By locator) {
		WebElement element = this.waitForElementToBeVisible(locator);
		this.select = new Select(element);
		String text = null;
		try {
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
			if (text.length() > 1) {
				this.log.debug("I get value \"" + text + "\" from \"" + this.getXPathFromBy(locator) + "\".");
			} else {
				this.log.warn("The Drop-down List WebElement has no value.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Drop-down List Value.");
			element = this.waitForElementToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		} catch (NoSuchElementException e) {
			this.log.warn("Encountered NoSuchElementException while retrieving Drop-down List Value.");
			element = this.waitForElementToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		} catch (Exception e) {
			this.log.debug("Encountered Exception while retrieving Drop-down List Value.");
			element = this.waitForElementToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		}
		return text;
	}
	
	/**
	 * Selects a Drop-down List WebElement Option.
	 * 
	 * @param map    Object containing a WebPage Object's Name, Type, XPath,
	 *               WebElement & By Locator.
	 * @param option Option to be selected.
	 * @param type   Type of option to be selected (can either be Index, VisibleText
	 *               or Value)
	 */

	public final void select(By locator, String option, String type) {
		WebElement element = this.waitForElementToBeVisible(locator);
		this.select = new Select(element);
		int size = this.select.getOptions().size();
		type = type.trim().toLowerCase();
		boolean flgOptionSelected = false;
		switch (type) {
		case "index":
			int index = Integer.parseInt(option);
			if (size >= index) {
				this.select.selectByIndex(index);
				this.log.debug("I select Index Option # " + index + ".");
			} else {
				this.test.log(LogStatus.ERROR, "Failed to select an option. Index Option # " + index + " is invalid!");
				this.log.error("Failed to select an option. Index Option # " + index + " is invalid!");
			}
			break;
		case "visibletext":
			for (int i = 0; i < size; i++) {
				if (option.equals(this.select.getOptions().get(i).getText().trim())) {
					this.select.selectByVisibleText(option);
					flgOptionSelected = true;
					this.log.debug("I select Visible Text Option \"" + option + "\".");
					break;
				}
				if (flgOptionSelected == false) {
					this.test.log(LogStatus.ERROR,
							"Failed to select an option. Visible Text Option \"" + option + "\" is invalid!");
					this.log.error("Failed to select an option. Visible Text Option \"" + option + "\" is invalid!");
				}
			}
			break;
		case "value":
			for (int i = 0; i < size; i++) {
				if (option.equals(this.select.getOptions().get(i).getAttribute("value").trim())) {
					this.select.selectByValue(option);
					flgOptionSelected = true;
					this.log.debug("I select Value Option \"" + option + "\".");
					break;
				}
				if (flgOptionSelected == false) {
					this.test.log(LogStatus.ERROR,
							"Failed to select an option. Value Option \"" + option + "\" is invalid!");
					this.log.error("Failed to select an option. Value Option \"" + option + "\" is invalid!");
				}
			}
			break;
		default:
			this.test.log(LogStatus.ERROR,
					"The type \"" + type + "\" is invalid! Failed to select from Drop-down List Options.");
			this.log.error("The type \"" + type + "\" is invalid! Failed to select from Drop-down List Options.");
		}
	}
	
	/**
	 * Deselects a Multi-select WebElement Option.
	 * 
	 * @param map    Object containing a WebPage Object's Name, Type, XPath,
	 *               WebElement & By Locator.
	 * @param option Option to be selected.
	 * @param type   Type of option to be selected (can either be Index, VisibleText
	 *               or Value)
	 */

	public final void deselect(By locator, String option, String type) {
		WebElement element = this.waitForElementToBeVisible(locator);
		this.select = new Select(element);
		int size = this.select.getOptions().size();
		type = type.trim().toLowerCase();
		boolean flgOptionDeselected = false;
		switch (type) {
		case "index":
			int index = Integer.parseInt(option);
			if (size >= index) {
				this.log.debug("I select Index Option # " + index + ".");
				this.select.deselectByIndex(index);
				flgOptionDeselected = true;
				this.log.debug("Successfully deselected Multi-select Index Option # " + index + ".");
			} else {
				this.test.log(LogStatus.ERROR, "Failed to deselect an option. Index Option # " + index + " is invalid!");
				this.log.error("Failed to deselect an option. Index Option # " + index + " is invalid!");
			}
			break;
		case "visibletext":
			for (int i = 0; i < size; i++) {
				if (option.equals(this.select.getOptions().get(i).getText().trim())) {
					this.log.debug("Deselecting Multi-select Visible Text Option \"" + option + "\".");
					this.select.deselectByVisibleText(option);
					flgOptionDeselected = true;
					this.log.debug("Successfully deselected Multi-select Visible Text Option \"" + option + "\".");
					break;
				}
			}
			if (flgOptionDeselected == false) {
				this.test.log(LogStatus.ERROR,
						"Failed to deselect an option. Visible Text Option \"" + option + "\" is invalid!");
				this.log.error("Failed to deselect an option. Visible Text Option \"" + option + "\" is invalid!");
			}
			break;
		case "value":
			for (int i = 0; i < size; i++) {
				if (option.equals(this.select.getOptions().get(i).getAttribute("value").trim())) {
					this.log.debug("Deselecting Multi-select Value Option \"" + option + "\".");
					this.select.deselectByValue(option);
					flgOptionDeselected = true;
					this.log.debug("Successfully deselected Multi-select Value Option \"" + option + "\".");
					break;
				}
			}
			if (flgOptionDeselected == false) {
				this.test.log(LogStatus.ERROR, "Failed to deselect an option. Value Option \"" + option + "\" is invalid!");
				this.log.error("Failed to deselect an option. Value Option \"" + option + "\" is invalid!");
			}
			break;
		default:
			this.test.log(LogStatus.ERROR,
					"The type \"" + type + "\" is invalid! Failed to select from Multi-select Options.");
			this.log.error("The type \"" + type + "\" is invalid! Failed to select from Multi-select Options.");
		}
	}
	
	/**** PAGE NAVIGATION ****/
	
	/**
	 * Navigate to the Url specified.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */

	public final void goTo(String url) {
		try {
			this.driver.get(url);
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
			Assert.fail("Encountered TimeoutException while navigating to Url: " + url);
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while navigating to Url \"" + url + "\" .");
			Assert.fail("Encountered Exception while navigating to Url \"" + url + "\" .");
		}
	}
	
	/**
	 * Navigate one item back from the browser's history.
	 */

	public final void back() {
		this.driver.navigate().back();
		this.log.debug("I go back.");
	}
	
	/**
	 * Navigate one item forward from the browser's history.
	 */

	public final void forward() {
		this.driver.navigate().forward();
		this.log.debug("I go forward.");
	}
	
	/**
	 * Refresh current page.
	 */

	public final void refresh() {
		this.driver.navigate().refresh();
		this.log.debug("I refresh.");
	}
	
	/**
	 * Closes Tab of a Web Browser.
	 */

	public final void closeTab() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while closing Web Browser.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing Web Browser.");
		}
		
		try {
			driver.close();
			this.log.debug("I close Tab.");
		} catch (NullPointerException e) {
			log.error("Encountered NullPointerException while closing WebDriver Instance.");
		} catch (Exception e) {
			log.error("Encountered Exception while closing WebDriver Instance.");
		}
	}
	
	/**
	 * Closes Web Browser.
	 */

	public final void closeBrowser() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while closing Web Browser.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing Web Browser.");
		}
		
		try {
			driver.quit();
			this.log.debug("I close Browser.");
		} catch (NullPointerException e) {
			log.error("Encountered NullPointerException while closing WebDriver Instance.");
		} catch (Exception e) {
			log.error("Encountered Exception while closing WebDriver Instance.");
		}
	}
	
	/**** SCREENSHOTS ****/
	
	/**
	 * Takes screenshot of Web Page.
	 * 
	 * @param fileName output file name
	 */

	public final void takeScreenShot(String log) {
		String fileName = Utilities.getCurrentDate("yyyyMMddHHmmss");
		String path = "C:\\SQA\\Selenium\\PouchCue\\automation-testing-selenium-pouchcue\\screenshots\\" + fileName + ".png";
		File scr = null;
		try {
			scr = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		} catch (WebDriverException e) {
			this.log.error("Encountered WebDriverException while taking screenshot!");
		} catch (UnsupportedOperationException e) {
			this.log.error("Encountered UnsupportedOperationException while taking screenshot!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while taking screenshot!");
		}
		try {
			FileUtils.copyFile(scr, new File(path));
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while generating " + fileName + " file.");
		} catch (IOException e) {
			this.log.error("Encountered IOException while generating " + fileName + " file.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while generating " + fileName + " file.");
		}
		this.test.log(LogStatus.INFO, log, this.test.addScreenCapture(path));
	}

	/**
	 * Takes screenshot of error from a Web Page.
	 * 
	 * @param fileName output file name (will be appended with "_Error.png")
	 */

	public final void takeScreenShotError(String log) {
		String fileName = Utilities.getCurrentDate("yyyyMMddHHmmss");
		String path = "C:\\SQA\\Selenium\\PouchCue\\automation-testing-selenium-pouchcue\\screenshots\\" + fileName + ".png";
		File scr = null;
		try {
			scr = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
		} catch (WebDriverException e) {
			this.log.error("Encountered WebDriverException while taking screenshot!");
		} catch (UnsupportedOperationException e) {
			this.log.error("Encountered UnsupportedOperationException while taking screenshot!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while taking screenshot!");
		}
		try {
			FileUtils.copyFile(scr, new File(path));
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while generating " + fileName + " file.");
		} catch (IOException e) {
			this.log.error("Encountered IOException while generating " + fileName + " file.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while generating " + fileName + " file.");
		}
		this.test.log(LogStatus.ERROR, log, this.test.addScreenCapture(path));
	}
	
	private final String getXPathFromBy(By locator) {
		String xpath = locator.toString().substring(locator.toString().indexOf("//"));
		return xpath;
	}

	/**** ASSERTIONS ****/

	/**
	 * Checks the value of INPUT or TEXTAREA WebElements if equal to the expected
	 * value.
	 * 
	 * @param map           Object containing a WebPage Object's Name, Type, XPath,
	 *                      WebElement & By Locator.
	 * @param expectedValue Expected value of the WebElement Text Box.
	 */

	public final void assertValue(By locator, String expectedValue) {
		this.waitForElementToBeVisible(locator);
		String actualValue = this.getValue(locator);
		Assert.assertEquals(actualValue, expectedValue, "Expected \"" + expectedValue + "\" but found \"" + actualValue + "\".");
		this.log.debug("I filled \"" + expectedValue + "\" at element \"" + this.getXPathFromBy(locator) + "\".");
	}

	/**
	 * Asserts Drop-down List WebElement Value if equal to expected text value.
	 * 
	 * @param map          Map Object containing WebElement, By, XPath & other String
	 *                     Objects relating to an Element.
	 * @param expectedText expected drop-down list value
	 */

	public final void assertDropDownListValue(By locator, String expectedValue) {
		WebElement element = this.waitForElementToBeVisible(locator);
		Select select = new Select(element);
		String actualValue = select.getFirstSelectedOption().getText().toLowerCase();
		Assert.assertEquals(actualValue, expectedValue, "Expected \"" + expectedValue + "\" but found \"" + actualValue + "\".");
		this.log.debug("I selected \"" + expectedValue + "\" at element \"" + this.getXPathFromBy(locator) + "\".");
	}

	/**
	 * Asserts WebElement with text if equal to expected text value.
	 * 
	 * @param map          Map Object containing WebElement, By, XPath & other String
	 *                     Objects relating to an Element.
	 * @param expectedText expected text value
	 */

	public final void assertText(By locator, String expectedValue) {
		this.waitForElementToBeVisible(locator);
		this.waitForTextToBePresentInElement(locator, expectedValue);
		String actualText = this.getText(locator);
		Assert.assertEquals(actualText, expectedValue, "Expected \"" + expectedValue + "\" but found \"" + actualText + "\".");
		this.log.debug("I see text \"" + expectedValue + "\" from element \"" + this.getXPathFromBy(locator) + "\".");
	}
	
	/**
	 * Asserts if Web Page contains expected text 
	 * @param expectedText Text expected to be visible at Web Page.
	 */
	
	public final void assertText(String expectedText) {
		Assert.assertEquals(this.see(By.xpath("//*[text()='" + expectedText + "']")), true);
	}
	
	/**
	 * Asserts Date WebElement if equal to expected date value.
	 * 
	 * @param map          Map Object containing WebElement, By, XPath & other String
	 *                     Objects relating to an Element.
	 * @param expectedDate expected date value
	 */

	public final void assertDate(By locator, Date expectedDate) {
		WebElement element = this.waitForElementToBeVisible(locator);
		Date actualDate = Utilities.convertStringToDate(element.getText(), "yyyy-MM-dd");
		Assert.assertEquals(actualDate, expectedDate, "Expected \"" + expectedDate + "\" but found \"" + actualDate + "\".");
		this.log.debug("I filled \"" + expectedDate + "\" from element \"" + this.getXPathFromBy(locator) + "\".");
	}
	
	/**
	 * Asserts WebElement is present on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */
	
	public final void assertPresent(By locator) {
		this.waitForElementPresence(locator);
		List<WebElement> elementList = driver.findElements(locator);
		Assert.assertFalse(elementList.isEmpty(), "The element \"" + this.getXPathFromBy(locator) + "\" is not present.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is present.");
	}
	
	/**
	 * Asserts WebElement is not present on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */
	
	public final void assertNotPresent(By locator) {
		List<WebElement> elementList = null;
		boolean status = false;
		for (int i = 0; i < 3; i++) {
			elementList = driver.findElements(locator);
			if (elementList.isEmpty()) {
				Assert.assertTrue(elementList.isEmpty());
				this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is not present.");
				status = true; 
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				log.error("Encountered InterruptedException while checking if Element is not present.");
			}
		}
		
		if (!status) {
			Assert.fail("The element \"" + this.getXPathFromBy(locator) + "\" is present.");
		}
	}
	
	/**
	 * Asserts WebElement is displayed on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */

	public final void assertDisplayed(By locator) {
		WebElement element = this.waitForElementToBeVisible(locator);
		boolean isDisplayed = element.isDisplayed();
		Assert.assertTrue(isDisplayed, "The element \"" + this.getXPathFromBy(locator) + "\" is not displayed.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is displayed.");
	}
	
	/**
	 * Asserts WebElement is not displayed on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 *            
	 */

	public final void assertNotDisplayed(By locator) {
		boolean isNotDisplayed = this.waitForElementToBeInvisible(locator);
		Assert.assertTrue(isNotDisplayed, "The element \"" + this.getXPathFromBy(locator) + "\" is displayed.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is not displayed.");
	}
	
	/**
	 * Asserts WebElement is enabled on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */

	public final void assertEnabled(By locator) {
		WebElement element = this.waitForElementToBeEnabled(locator);
		boolean isEnabled = element.isEnabled();
		Assert.assertTrue(isEnabled, "The element \"" + this.getXPathFromBy(locator) + "\" is disabled.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is enabled.");
	}
	
	/**
	 * Asserts WebElement is not disabled on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */

	public final void assertDisabled(By locator) {
		this.waitForElementToBeDisabled(locator);
		WebElement element = this.waitForElementToBeDisabled(locator);
		boolean isEnabled = element.isEnabled();
		Assert.assertFalse(isEnabled, "The element \"" + this.getXPathFromBy(locator) + "\" is enabled.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is disabled.");
	}
	
	/**
	 * Asserts WebElement is selected on the Web Page.
	 * 
	 * @param element WebElement to be verified.
	 * @param type    Type of option to be verified (can either be Index,
	 *                VisibleText or Value)
	 */

	public final void assertSelected(By locator) {
		WebElement element = this.waitForElementToBeVisible(locator);
		this.waitForElementSelectionStateToBe(locator, true);
		boolean isSelected = element.isSelected();
		Assert.assertTrue(isSelected, "The element \"" + this.getXPathFromBy(locator) + "\" is not selected.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is selected.");
	}
	
	/**
	 * Asserts WebElement is not selected on the Web Page.
	 * 
	 * @param element WebElement to be verified.
	 * @param type    Type of option to be verified (can either be Index,
	 *                VisibleText or Value)
	 */

	public final void assertNotSelected(By locator) {
		WebElement element = this.waitForElementToBeVisible(locator);
		this.waitForElementSelectionStateToBe(locator, false);
		boolean isSelected = element.isSelected();
		Assert.assertFalse(isSelected, "The element \"" + this.getXPathFromBy(locator) + "\" is selected.");
		this.log.debug("The element \"" + this.getXPathFromBy(locator) + "\" is not selected.");
	}
	
	/**
	 * Checks if current Web Page's Title is equal to the expected Title.
	 * 
	 * @param expectedTitle expected Web Page Title.
	 * @param pageName      name of the Web Page.
	 */

	public final void assertTitle(String expectedTitle) {
		this.waitForPageTitleToBe(expectedTitle);
		String actualTitle = this.driver.getTitle();
		Assert.assertEquals(actualTitle, expectedTitle, "Expected \"" + expectedTitle + "\" but found \"" + actualTitle + "\".");
		this.log.info("Page Title is \"" + expectedTitle + "\".");
	}
	
	/**
	 * Asserts if current Web Page's Url is equal to the expected Url.
	 * 
	 * @param expectedUrl expected Url
	 * @param pagename    name of the Web Page.
	 */

	public final void assertUrl(String expectedUrl) {
		this.waitForUrlToBe(expectedUrl);
		String actualUrl = this.driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Expected \"" + expectedUrl + "\" but found \"" + actualUrl + "\".");
		this.log.info("Url is \"" + expectedUrl + "\".");
	}
	
	/**
	 * Asserts if current Web Page's Url is equal to the expected Url.
	 * 
	 * @param expectedUrl expected Url
	 * @param pagename    name of the Web Page.
	 */

	public final void assertPartialUrl(String partialUrl) {
		this.waitForUrlToHavePartialUrl(partialUrl);
		String actualUrl = this.driver.getCurrentUrl();
		if (!actualUrl.contains(partialUrl)) {
			Assert.fail("Expected \"" + partialUrl + "\" but found \"" + actualUrl + "\".");
		}
		this.log.info("Url is \"" + partialUrl + "\".");
	}
	
	/**
	 * Asserts message displayed if equal to expected message.
	 * 
	 * @param map             Map Object containing WebElement, By, XPath & other String
	 *                        Objects relating to an Element.
	 * @param expectedMessage Expected message displayed.
	 */

	public final void assertMessage(By locator, String expectedMessage) {
		WebElement element = this.waitForElementToBeVisible(locator);
		String actualMessage = element.getText();
		Assert.assertEquals(actualMessage, expectedMessage, "Expected \"" + expectedMessage + "\" but found \"" + actualMessage + "\".");
		this.log.info("I see Message \"" + expectedMessage + "\" from \"" + this.getXPathFromBy(locator) + "\".");
	}
	
	/**
	 * Asserts Javascript Alert Message displayed if equal to
	 * expected message.
	 * 
	 * @param expectedMessage Expected message displayed.
	 */

	public final void assertAlertMessage(String expectedMessage) {
		this.test.log(LogStatus.INFO, "Verify \"" + expectedMessage + "\" Alert Message is displayed.");
		this.log.info("Verifying \"" + expectedMessage + "\" Alert Message is displayed.");
		this.waitForAlertToBePresent();
		Alert alert = driver.switchTo().alert();
		String actualMessage = alert.getText();
		Assert.assertEquals(actualMessage, expectedMessage, "Expected \"" + expectedMessage + "\" but found \"" + actualMessage + "\"."	);
		this.log.info("I see Alert Message \"" + expectedMessage + "\".");
	}

	/**** WAITS ****/

	/**
	 * Waits for a specific time (Seconds).
	 * 
	 * @param locator By locator of target WebElement to be present.
	 */

	public final void wait(int duration) {
		try {
			Thread.sleep(duration * 1000);
		} catch (InterruptedException e) {
			log.error("Encountered InterruptedException while waiting.");
		}
	}
	
	/**
	 * Waits for WebElement to be present on the Web Page.
	 * 
	 * @param locator By locator of target WebElement to be present.
	 */

	private final WebElement waitForElementPresence(By locator) {
		this.log.trace("Waiting for Web Element presence.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			this.log.trace("Web Element had become present!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be present has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be present has expired!");
			Assert.fail("Wait time for Web Element to be present has expired!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element presence!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element presence!");
			Assert.fail("Wait time for Web Element to be present has expired!");
		}
		return element;
	}
	
	/**
	 * Waits for WebElement to be present on the Web Page.
	 * 
	 * @param locator By locator of target WebElement to be present.
	 */

	public final boolean see(By locator) {
		boolean status = false;
		try {
			this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			status  = true;
			this.log.debug("I see \"" + locator + "\".");
		} catch (TimeoutException e) {
			this.log.debug("I don't see \"" + locator + "\".");
			status = false;
		} catch (Exception e) {
			this.log.debug("I don't see \"" + locator + "\".");
			status = false;
		}
		return status;
	}
	
	/**
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param element Target WebElement to be visible.
	 */

	private final WebElement waitForElementToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			this.log.trace("Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be visible has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be visible!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element to be visible!");
			e.printStackTrace();
			Assert.fail("Encountered Exception while waiting for element to be visible!");
		}
		return element;
	}

	/**
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param element Target WebElement to be invisible.
	 */

	private final boolean waitForElementToBeInvisible(By locator) {
		this.log.trace("Waiting for Web Element to be invisible.");
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			this.log.trace("Web Element had become invisible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be invisible has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be invisible has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be invisible!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be invisible!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element to be invisible!");
			Assert.fail("Encountered Exception while waiting for element to be invisible!");
		}
		return isVisible;
	}
	
	/**
	 * Waits for a list of WebElements to be visible at the Web Page.
	 * 
	 * @param element Target WebElement to be visible.
	 */

	private final List<WebElement> waitForElementsToBeVisible(By locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		List<WebElement> element = null;
		try {
			element = this.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			this.log.trace("Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be visible has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be visible!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element to be visible!");
			e.printStackTrace();
			Assert.fail("Encountered Exception while waiting for element to be visible!");
		}
		return element;
	}/**
	 * Waits for WebElement to be enabled at the Web Page.
	 * 
	 * @param element Target WebElement to be enabled.
	 */

	private final WebElement waitForElementToBeEnabled(By locator) {
		this.log.trace("Waiting for Web Element to be enabled.");
		WebElement element = this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		boolean isEnabled = false;
		for(int i = 0; i < 3; i++) {
			isEnabled = element.isEnabled();
			if (isEnabled) {
				this.log.trace("Web Element is enabled!");
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				this.log.error("Encountered InterruptedException while checking if element is enabled.");
			}
		}
		if (!isEnabled) {
			this.log.error("Wait time for Web Element to be enabled has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be enabled has expired!");
			Assert.fail("Wait time for Web Element to be enabled has expired!");
		}
		return element;
	}

	/**
	 * Waits for WebElement to be disabled at the Web Page.
	 * 
	 * @param element Target WebElement to be disabled.
	 */

	private final WebElement waitForElementToBeDisabled(By locator) {
		this.log.trace("Waiting for Web Element to be disabled.");
		WebElement element = this.wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		boolean isEnabled = true;
		for(int i = 0; i < 3; i++) {
			isEnabled = element.isEnabled();
			if (!isEnabled) {
				this.log.trace("Web Element is disabled!");
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				this.log.error("Encountered InterruptedException while checking if element is disabled.");
			}
		}
		if (isEnabled) {
			this.log.error("Wait time for Web Element to be disabled has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be disabled has expired!");
			Assert.fail("Wait time for Web Element to be disabled has expired!");
		}
		return element;
	}

	/**
	 * Waits for WebElement to be clickable at the Web Page.
	 * 
	 * @param element Target WebElement to be clickable.
	 */

	private final WebElement waitForElementToBeClickable(By locator) {
		this.log.trace("Waiting for Web Element to be clickable.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable(locator));
			this.log.trace("Web Element had become clickable!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be clickable has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be clickable has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be clickable!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be clickable!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element to be clickable!");
			Assert.fail("Encountered Exception while waiting for element to be clickable!");
		}
		return element;
	}

	/**
	 * Waits for an input text to be present in a WebElement.
	 * 
	 * @param element Target WebElement for checking of text.
	 * @param text    expected text value at the WebElement.
	 */

	private final boolean waitForTextToBePresentInElement(By locator, String text) {
		this.log.trace("Waiting for the text \"" + text + " to be present in Web Element.");
		boolean isPresent = false;
		try {
			isPresent = this.wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
			this.log.trace("The text \"" + text + " had become present in Web Element.");
		} catch (TimeoutException e) {
			this.log.error("Wait time for the text \"" + text + " to be present in Web Element has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for the text \"" + text + " to be present in Web Element has expired!");
			Assert.fail("Encountered TimeoutException while waiting for the text \"" + text + " to be present in Web Element!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for the text \"" + text + " to be present in Web Element!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for the text \"" + text + " to be present in Web Element!");
			Assert.fail( "Encountered Exception while waiting for the text \"" + text + " to be present in Web Element!");
		}
		return isPresent;
	}

	/**
	 * Waits for WebElement to be selected at the Web Page.
	 * 
	 * @param element Target WebElement to be selected.
	 */

	@SuppressWarnings("unused")
	private final boolean waitForElementToBeSelected(By locator) {
		this.log.trace("Waiting for Web Element to be selected.");
		boolean isSelected = false;
		try {
			isSelected = this.wait.until(ExpectedConditions.elementToBeSelected(locator));
			this.log.trace("Web Element had become selected!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be selected has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Web Element to be selected has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be selected!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be selected!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for element to be selected!");
			Assert.fail("Encountered Exception while waiting for element to be selected!");
		}
		return isSelected;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param element        Target WebElement for checking of selection state.
	 * @param selectionState expected selection state.
	 */

	private final boolean waitForElementSelectionStateToBe(By locator, boolean selectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + selectionState + "!");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe(locator, selectionState));
			this.log.trace("Web Element Selection State is " + selectionState + "!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element Selection State to be " + selectionState + " has expired!");
			this.test.log(LogStatus.ERROR,
					"Wait time for Web Element Selection State to be " + selectionState + " has expired!");
			Assert.fail("Encountered TimeoutException while waiting for Web Element Selection State to be "
					+ selectionState + " has expired!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be " + selectionState
					+ " has expired!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for Web Element Selection State to be "
					+ selectionState + " has expired!");
			Assert.fail("Encountered Exception while waiting for Web Element Selection State to be " + selectionState
					+ " has expired!");
		}
		return status;
	}

	/**
	 * Waits for a Page Url to be that of the expected input Url Value.
	 * 
	 * @param expectedUrl expected Url Value to be on the page
	 * @param page        name of the page.
	 */

	private final boolean waitForUrlToBe(String expectedUrl) {
		this.log.trace("Waiting for Url to be \"" + expectedUrl + "\".");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.urlToBe(expectedUrl));
			this.log.trace("Web Browser successfully changed to \"" + expectedUrl + "\".");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Url to be " + expectedUrl + " has expired.");
			this.test.log(LogStatus.ERROR, "Wait time for Url to be " + expectedUrl + " has expired.");
			Assert.fail("Encountered TimeoutException while waiting for Url to be " + expectedUrl + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Url to be " + expectedUrl + ".");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for Url to be " + expectedUrl + ".");
			Assert.fail("Encountered Exception while waiting for Url to be " + expectedUrl + ".");
		}
		return status;
	}
	
	/**
	 * Waits for Page Url to contain a specified partial Url.
	 * 
	 * @param partialUrl expected partial Url to be at current Url.
	 */

	private final boolean waitForUrlToHavePartialUrl(String partialUrl) {
		this.log.trace("Waiting for Page Url to contain a specified partial Url.");
		boolean status = false;
		String actualUrl;
		for(int i = 0; i < 3; i++) {
			actualUrl = this.driver.getCurrentUrl();
			if (actualUrl.contains(partialUrl)) {
				this.log.trace("I am at \"" + partialUrl + "\".");
				status = true;
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				this.log.error("Encountered InterruptedException while checking if element is enabled.");
			}
		}
		if (!status) {
			this.log.error("I am not at \"" + partialUrl + "\".");
			this.test.log(LogStatus.ERROR, "I am not at \"" + partialUrl + "\".");
			Assert.fail("I am not at \"" + partialUrl + "\".");
		}
		return status;
	}

	/**
	 * Waits for a Page Title to be that of the expected input Title Value.
	 * 
	 * @param expectedTitle expected Title Value to be on the page
	 * @param page          name of the page.
	 */

	private final boolean waitForPageTitleToBe(String expectedTitle) {
		this.log.trace("Waiting until Page Title is \"" + expectedTitle + "\".");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.titleIs(expectedTitle));
			this.log.trace("Page Title successfully changed to \"" + expectedTitle + "\".");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Page Title to be " + expectedTitle + " has expired.");
			this.test.log(LogStatus.ERROR, "Wait time for Page Title to be " + expectedTitle + " has expired.");
			Assert.fail("Encountered TimeoutException while waiting for Url to be " + expectedTitle + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Page Title to be " + expectedTitle + ".");
			this.test.log(LogStatus.ERROR, "Encountered Exception while waiting for Page Title to be " + expectedTitle + ".");
			Assert.fail("Encountered Exception while waiting for Page Title to be " + expectedTitle + ".");
		}
		return status;
	}

	/**
	 * Waits for a Javascript Alert to be present on the WebPage.
	 */

	private final Alert waitForAlertToBePresent() {
		try {
			alert = this.wait.until(ExpectedConditions.alertIsPresent());
		} catch (TimeoutException e) {
			this.log.error("Wait time for Alert to be displayed has expired!");
			this.test.log(LogStatus.ERROR, "Wait time for Alert to be displayed has expired!");
			Assert.fail("Encountered TimeoutException while getting Alert Message!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while getting Alert Message!");
			this.test.log(LogStatus.ERROR, "Encountered Exception while getting Alert Message!");
			Assert.fail("Encountered Exception while getting Alert Message!");
		}
		return alert;
	}
}
