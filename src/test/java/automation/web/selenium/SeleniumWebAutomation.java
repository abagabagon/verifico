package automation.web.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import automation.web.GeneralWebAutomation;
import enums.Browser;
import settings.Settings;

public class SeleniumWebAutomation extends GeneralWebAutomation {

	private WebDriver driver;
	private Logger log;
	private WebDriverWait wait;
	private Select select;
	private Alert alert;
	private Actions action;
	private ArrayList<String> tabs;
	private long implicitWaitDuration;
	private long explicitWaitDuration;

	private SeleniumWebDriver seleniumWebDriver;

	public SeleniumWebAutomation() {
		this.log = LogManager.getLogger(SeleniumWebAutomation.class);
		this.log.debug("Initializing SeleniumWebAutomation Class.");
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.implicitWaitDuration = Settings.getImplicitWaitDuration();
		this.explicitWaitDuration = Settings.getExplicitWaitDuration();
		this.log.debug("Successfully initialized SeleniumWebAutomation Class.");
	}

	/* ####################################################### */
	/*                     BROWSER ACTIONS                     */
	/* ####################################################### */

	@Override
	public void openBrowser(Browser browser) {
		this.log.debug("Initializing Selenium Web Driver.");
		this.log.info("I open Web Browser.");

		try {
			switch (browser) {
			case PHANTOMJS:
				this.driver = this.seleniumWebDriver.getPhantomJSDriver();
				break;
			case CHROME:
				this.driver = this.seleniumWebDriver.getChromeDriver();
				break;
			case SAFARI:
				this.driver = this.seleniumWebDriver.getSafariDriver();
				break;
			case FIREFOX:
				this.driver = this.seleniumWebDriver.getFirefoxDriver();
				break;
			case OPERA:
				this.driver = this.seleniumWebDriver.getOperaDriver();
				break;
			case EDGE:
				this.driver = this.seleniumWebDriver.getEdgeDriver();
				break;
			case IE:
				this.driver = this.seleniumWebDriver.getIEDriver();
				break;
			default:
				this.log.fatal("Unsupported Web Browser. Please report issue to QA Team.");
				System.exit(1);
			}
		} catch (WebDriverException e) {
			this.log.fatal("Encountered WebDriverException while initializing Selenium Web Driver.");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while initializing Selenium Web Driver.");
			e.printStackTrace();
			System.exit(1);
		}

		initializeImplicitWait();
		initializeExplicitWait();
		maximizeBrowserWindow();
		deleteAllCookies();
		this.log.info("I opened Web Browser.");
	}
	
	@Override
	public void openTab(String url) {
		this.log.info("Creating New Tab.");
		JavascriptExecutor jse = (JavascriptExecutor)this.driver;
		String link = "window.open('" + url + "', '_blank');";
		jse.executeScript(link);
		this.log.info("Successfully created new tab.");
	}

	@Override
	public void goTo(String url) {
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
	 * 	Switching to Tabs
	 * 
	 * @param option - Title or URL , identifier - the title or the URL itself
	 */

	@Override
	public boolean switchTabByTitle(String title) {
		String parentWindow = this.driver.getWindowHandle();
		Set<String> windows = this.driver.getWindowHandles();
		this.log.info("Total Tabs Open: " + windows.size());
		boolean isExisting = false;
		for (String winID : windows) {
			if (!winID.equals(parentWindow)) {
				this.driver.switchTo().window(winID);
				if (this.driver.getTitle().equals(title)) {
					this.log.info("Successfully switched tab to " + title + ".");
					isExisting = true;
					return isExisting;
				}
				this.driver.switchTo().window(parentWindow);
			}
		}
		this.log.info("Tab with the title " + title + " was not found.");
		return isExisting;
	}
	
	@Override
	public boolean switchTabByURL(String url) {
		String parentWindow = this.driver.getWindowHandle();
		Set<String> windows = this.driver.getWindowHandles();
		this.log.info("Total Tabs Open " + windows.size());
		boolean isExisting = false;
		for (String winID : windows) {
			if (!winID.equals(parentWindow)) {
				this.driver.switchTo().window(winID);
				if (this.driver.getCurrentUrl().equals(url)) {
					this.log.info("Successfully switched tab to " + url + ".");
					isExisting = true;
					return isExisting;
				}
				this.driver.switchTo().window(parentWindow);
			}
		}
		this.log.info("Tab with the url " + url + " was not found.");
		return isExisting;
	}
	
	@Override
	public void switchTabToOriginal() {
		this.log.info("Switching back to original window.");
		this.tabs = new ArrayList<String>(this.driver.getWindowHandles());
		this.driver.switchTo().window(this.tabs.get(0));
		this.log.info("Switched back to Original.");
	}

	@Override
	public void back() {
		this.log.info("I click back.");
		this.driver.navigate().back();
		this.log.info("I clicked back.");
	}

	@Override
	public void forward() {
		this.log.info("I click forward.");
		this.driver.navigate().forward();
		this.log.info("I clicked forward.");
	}

	@Override
	public void refresh() {
		this.log.info("I click refresh.");
		this.driver.navigate().refresh();
		this.log.info("I clicked refresh.");
	}

	@Override
	public void closeTab() {
		try {
			this.log.info("I close Tab.");
			this.driver.close();
			this.log.info("I closed Tab.");
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while closing WebDriver Instance.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing WebDriver Instance.");
		}
	}

	@Override
	public void closeBrowser() {
		try {
			this.log.info("I close Browser.");
			this.driver.quit();
			this.log.info("I closed Browser.");
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while closing WebDriver Instance.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing WebDriver Instance.");
		}
	}
	
	/**
	 * Maximizes Browser Window.
	 */
	
	@Override
	public void maximizeBrowserWindow() {
		this.log.debug("Maximizing Web Browser Window.");
		this.driver.manage().window().maximize();
		this.log.debug("Successfully maximized Web Browser Window.");
	}

	/**
	 * Deletes all cookies.
	 */
	
	@Override
	public void deleteAllCookies() {
		this.log.debug("Deleting all cookies.");
		this.driver.manage().deleteAllCookies();
		this.log.debug("Successfully deleted all cookies.");
	}

	/**
	 * Initializes Selenium WebDriver Implicit Wait
	 */

	private void initializeImplicitWait() {
		this.log.debug("Initializing Implicit Wait.");
		this.driver.manage().timeouts().implicitlyWait(this.implicitWaitDuration, TimeUnit.SECONDS);
		this.log.debug("Successfully initialized Implicit Wait.");
	}

	/**
	 * Initializes Selenium WebDriver Explicit Wait
	 */

	private void initializeExplicitWait() {
		this.log.debug("Initializing Explicit Wait.");
		this.wait = new WebDriverWait(this.driver, this.explicitWaitDuration);
		this.log.debug("Successfully initialized Explicit Wait.");
	}
	
	/* ####################################################### */
	/*                       USER ACTIONS                      */
	/* ####################################################### */
	
	@Override
	public void mouseHover(Object locator) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.moveToElement(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while mouse hover action.");
			element = this.waitForElementToBeVisible(locator);
			element.clear();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while mouse hover action.");
			element = this.waitForElementToBeVisible(locator);
			element.clear();
		}
	}

	@Override
	public void click(Object locator) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
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

	@Override
	public void clickJS(Object locator) {
		WebElement element =  this.driver.findElement((By)locator);
		JavascriptExecutor executor = (JavascriptExecutor) this.driver;
		executor.executeScript("arguments[0].click();", element);
		
	}
	
	@Override
	public void fill(Object locator, String inputText) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
			element.clear();
			element.sendKeys(inputText);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (IllegalArgumentException e) {
			this.log.warn("Encountered IllegalArgumentException while entering text at WebElement. Input Text is NULL");
		} catch (Exception e) {
			this.log.warn("Encountered Exception while entering text at WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.sendKeys(inputText);
		}
	}

	@Override
	public void clear(Object locator) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
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

	@Override
	public boolean see(Object locator) {
		List<WebElement> elements = this.driver.findElements((By)locator);
		boolean seeElement = false;
		if (elements.size() > 0) {
			seeElement = true;
		}
		return seeElement;
	}
	
	@Override
	public boolean seeTitle(String expectedTitle) {
		return this.wait.until(ExpectedConditions.titleIs(expectedTitle));
	}

	@Override
	public boolean seeUrl(String expectedUrl) {
		return this.wait.until(ExpectedConditions.urlMatches(expectedUrl));
	}
	
	@Override
	public Boolean isClickable(String locator) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
			this.wait.until(ExpectedConditions.elementToBeClickable(element));
			this.log.info("The element" + locator + " is clickable.");
			return true;
		} catch (Exception e) {
			this.log.info("The element" + locator + " is not clickable.");
			return false;
		}
	}

	@Override
	public void select(Object locator, String option) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to select an option. Element \"" + locator + "\" is not a SELECT!");
			Assert.fail("Failed to select an option. Element \"" + locator + "\" is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while checking if WebElement is a SELECT.");
			Assert.fail("Encountered Exception while checking if WebElement is a SELECT.");
		}
		int size = this.select.getOptions().size();
		boolean flgOptionSelected = false;
		for (int i = 0; i < size; i++) {
			if (option.equals(this.select.getOptions().get(i).getText().trim())) {
				this.select.selectByVisibleText(option);
				flgOptionSelected = true;
				this.log.debug("I select Option \"" + option + "\".");
				break;
			}
			if (flgOptionSelected == false) {
				this.log.error("Failed to select an option. Option \"" + option + "\" is invalid!");
			}
		}

	}

	@Override
	public void deselect(Object locator, String option) {
		WebElement element = (WebElement) this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to select an option. Element \"" + locator + "\" is not a SELECT!");
			Assert.fail("Failed to select an option. Element \"" + locator + "\" is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while checking if WebElement is a SELECT.");
			Assert.fail("Encountered Exception while checking if WebElement is a SELECT.");
		}
		int size = this.select.getOptions().size();
		boolean flgOptionDeselected = false;
		for (int i = 0; i < size; i++) {
			if (option.equals(this.select.getOptions().get(i).getText().trim())) {
				this.log.debug("Deselecting Option \"" + option + "\".");
				this.select.deselectByVisibleText(option);
				flgOptionDeselected = true;
				this.log.debug("Successfully deselected Option \"" + option + "\".");
				break;
			}
		}
		if (flgOptionDeselected == false) {
			this.log.error("Failed to deselect an option. Option \"" + option + "\" is invalid!");
		}
	}

	@Override
	public Object getElement(Object locator) {
		WebElement element = null;
		try {
			element = this.driver.findElement((By)locator);
		} catch (NullPointerException e) {
			this.log.warn("Encountered NullPointerException while getting WebElement.");
			element = this.waitForElementToBeVisible(locator);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while getting WebElement.");
			element = this.waitForElementToBeVisible(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			element = this.waitForElementToBeVisible(locator);
		} catch (Exception e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			element = this.waitForElementToBeVisible(locator);
		}
		Object objElem = element;
		return objElem;
	}
	
	@Override
	public List<Object> getElements(Object locator) {
		List<WebElement> listOfElements = this.driver.findElements((By)locator);
		List<Object> elements = listOfElements.stream().collect(Collectors.toList());
		try {
			this.log.info("Elements found with xpath: " + locator.toString());
			return elements;	
		}catch (Exception e) {
			this.log.info("No element found.");
			return null;
		}	
	}

	@Override
	public String getText(Object locator) {
		WebElement element = (WebElement) this.getElement(locator);
		String text = null;
		try {
			text = element.getText();
			if (text.length() == 0) {
				this.log.debug("WebElement has no text.");
			}
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

	@Override
	public String getValue(Object locator) {
		String text = null;
		WebElement element = (WebElement) this.getElement(locator);
		try {
			text = element.getAttribute("value");
			if (text.length() == 0) {
				this.log.debug("The Text Box/Area Web Element has no value.");
			}
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

	@Override
	public String getAttributeValue(Object locator, String attribute) {
		String text = null;
		WebElement element = (WebElement) this.getElement(locator);
		try {
			text = element.getAttribute(attribute);
			if (text.length() == 0) {
				this.log.debug("The Attribute: " + attribute + " of Web Element has no value.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered Exception while retrieving Attribute Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getAttribute(attribute);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving Attribute Value from WebElement.");
			element = this.waitForElementToBeVisible(locator);
			element.getAttribute(attribute);
		}
		return text;
	}

	@Override
	public String getDropDownListValue(Object locator) {
		WebElement element = (WebElement) this.getElement(locator);
		this.select = new Select(element);
		String text = null;
		try {
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
			if (text.length() == 0) {
				this.log.warn("The Drop-down List WebElement has no value.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Drop-down List Value.");
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

	@Override
	public void wait(int duration) {
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting.");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting.");
		}
	}

	/* ####################################################### */
	/*                        ASSERTIONS                       */
	/* ####################################################### */

	@Override
	public void assertValue(Object locator, String expectedValue) {
		this.log.info("Verifying \"" + expectedValue + "\" Text Box or Area Value is displayed.");
		this.getElement(locator);
		String actualValue = this.getValue(locator.toString());
		Assert.assertEquals(actualValue, expectedValue,
				"Expected \"" + expectedValue + "\" but found \"" + actualValue + "\".");
		this.log.info("I filled \"" + expectedValue + "\" at element \"" + locator.toString() + "\".");
	}

	@Override
	public void assertDropDownListValue(Object locator, String expectedValue) {
		this.log.info("Verifying \"" + expectedValue + "\" Drop-down List Value is displayed.");
		WebElement element = (WebElement) this.getElement(locator);
		Select select = new Select(element);
		String actualValue = select.getFirstSelectedOption().getText().toLowerCase();
		Assert.assertEquals(actualValue, expectedValue,
				"Expected \"" + expectedValue + "\" but found \"" + actualValue + "\".");
		this.log.info("I selected \"" + expectedValue + "\" at element \"" + locator.toString() + "\".");
	}

	@Override
	public void assertText(Object locator, String expectedValue) {
		this.log.info("Verifying \"" + expectedValue + "\" Text Value is displayed.");
		String actualText = this.getText(locator.toString());
		Assert.assertEquals(actualText, expectedValue,
				"Expected \"" + expectedValue + "\" but found \"" + actualText + "\".");
		this.log.info("I see text \"" + expectedValue + "\" from element \"" + locator.toString() + "\".");
	}

	@Override
	public void assertDisplayed(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is displayed.");
		WebElement element = (WebElement) this.getElement(locator);
		boolean isDisplayed = element.isDisplayed();
		Assert.assertTrue(isDisplayed, "The element \"" + locator.toString() + "\" is not displayed.");
		this.log.info("The element \"" + locator.toString() + "\" is displayed.");
	}

	@Override
	public void assertNotDisplayed(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is not displayed.");
		//Boolean isNotDisplayed = this.waitForElementToBeInvisible(this.driver.findElement((By)locator));
		boolean isNotDisplayed;
		try {
			this.driver.findElement((By)locator);
			isNotDisplayed = false;
		}catch(NoSuchElementException e) {
			isNotDisplayed = true;
		}	
		Assert.assertTrue(isNotDisplayed, "The element \"" + locator.toString() + "\" is displayed.");
		this.log.info("The element \"" + locator.toString() + "\" is not displayed.");
	}
	
	@Override
	public void assertEnabled(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is enabled.");
		WebElement element = (WebElement) this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		Assert.assertTrue(isEnabled, "The element \"" + locator.toString() + "\" is disabled.");
		this.log.info("The element \"" + locator.toString() + "\" is enabled.");
	}

	@Override
	public void assertDisabled(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is disabled.");
		WebElement element = (WebElement) this.getElement(locator);
		boolean isDisabled = element.isEnabled();
		Assert.assertFalse(isDisabled, "The element \"" + locator.toString() + "\" is enabled.");
		this.log.info("The element \"" + locator.toString() + "\" is disabled.");
	}

	@Override
	public void assertSelected(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is selected.");
		WebElement element = (WebElement) this.getElement(locator);
		this.waitForElementSelectionStateToBe(element, true);
		boolean isSelected = element.isSelected();
		Assert.assertTrue(isSelected, "The element \"" + locator.toString() + "\" is not selected.");
		this.log.info("The element \"" + locator.toString() + "\" is selected.");
	}

	@Override
	public void assertNotSelected(Object locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is not selected.");
		WebElement element = (WebElement) this.getElement(locator);
		this.waitForElementSelectionStateToBe(element, false);
		boolean isNotSelected = element.isSelected();
		Assert.assertFalse(isNotSelected, "The element \"" + locator.toString() + "\" is selected.");
		this.log.info("The element \"" + locator.toString() + "\" is not selected.");
	}

	@Override
	public void assertTitle(String expectedTitle) {
		this.log.info("Verifying \"" + expectedTitle + "\" Page Title Value is displayed.");
		this.wait.until(ExpectedConditions.titleIs(expectedTitle));
		String actualTitle = this.driver.getTitle();
		Assert.assertEquals(actualTitle, expectedTitle,
				"Expected \"" + expectedTitle + "\" but found \"" + actualTitle + "\".");
		this.log.info("Page Title is \"" + expectedTitle + "\".");
	}

	@Override
	public void assertUrl(String expectedUrl) {
		this.log.info("Verifying \"" + expectedUrl + "\" Page URL Value is displayed.");
		this.wait.until(ExpectedConditions.urlMatches(expectedUrl));
		String actualUrl = this.driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl,
				"Expected \"" + expectedUrl + "\" but found \"" + actualUrl + "\".");
		this.log.info("Url is \"" + expectedUrl + "\".");
	}

	@Override
	public void assertPartialUrl(String partialUrl) {
		this.log.info("Verifying \"" + partialUrl + "\" Partial Page URL Value is displayed.");
		String actualUrl = this.driver.getCurrentUrl();
		if (!actualUrl.contains(partialUrl)) {
			Assert.fail("Expected \"" + partialUrl + "\" but found \"" + actualUrl + "\".");
		}
		this.log.info("Url is \"" + partialUrl + "\".");
	}

	@Override
	public void assertAlertMessage(String expectedMessage) {
		this.log.info("Verifying \"" + expectedMessage + "\" Alert Message is displayed.");
		this.waitForAlertToBePresent();
		Alert alert = this.driver.switchTo().alert();
		String actualMessage = alert.getText();
		Assert.assertEquals(actualMessage, expectedMessage,
				"Expected \"" + expectedMessage + "\" but found \"" + actualMessage + "\".");
		this.log.info("I see Alert Message \"" + expectedMessage + "\".");
	}

	/*#######################################################*/
	/*                  SUPPORTIVE METHODS                   */
	/*#######################################################*/
	
	/**
	 * Waits for WebElement to be visible at the Web Page.
	 * 
	 * @param element Target WebElement to be visible.
	 */

	private final WebElement waitForElementToBeVisible(Object locator) {
		this.log.trace("Waiting for Web Element to be visible.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.visibilityOfElementLocated((By)locator));
			this.log.trace("Web Element had become visible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be visible has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be visible!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be visible!");
			e.printStackTrace();
			Assert.fail("Encountered Exception while waiting for element to be visible!");
		}
		return element;
	}

	/**
	 * Waits for WebElement to be clickable at the Web Page.
	 * 
	 * @param element Target WebElement to be clickable.
	 */

	private WebElement waitForElementToBeClickable(Object locator) {
		this.log.trace("Waiting for Web Element to be clickable.");
		WebElement element = null;
		try {
			element = this.wait.until(ExpectedConditions.elementToBeClickable((By)locator));
			this.log.trace("Web Element had become clickable!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be clickable has expired!");
			Assert.fail("Encountered TimeoutException while waiting for element to be clickable!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be clickable!");
			Assert.fail("Encountered Exception while waiting for element to be clickable!");
		}
		return element;
	}

	/**
	 * Waits for WebElement to be invisible at the Web Page.
	 * 
	 * @param element Target WebElement to be invisible.
	 */

	@SuppressWarnings("unused")
	private final boolean waitForElementToBeInvisible(Object locator) {
		this.log.trace("Waiting for Web Element to be invisible.");
		boolean isVisible = true;
		try {
			isVisible = this.wait.until(ExpectedConditions.invisibilityOfElementLocated((By)locator));
			this.log.trace("Web Element had become invisible!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element to be invisible has expired!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for element to be invisible!");
			Assert.fail("Encountered Exception while waiting for element to be invisible!");
		}
		return isVisible;
	}

	/**
	 * Waits for an element's selection state to be the expected selection state.
	 * 
	 * @param element        Target WebElement for checking of selection state.
	 * @param selectionState expected selection state.
	 */

	private final boolean waitForElementSelectionStateToBe(Object locator, boolean selectionState) {
		this.log.trace("Waiting for Web Element Selection State is " + selectionState + "!");
		boolean status = false;
		try {
			status = this.wait.until(ExpectedConditions.elementSelectionStateToBe((By)locator, selectionState));
			this.log.trace("Web Element Selection State is " + selectionState + "!");
		} catch (TimeoutException e) {
			this.log.error("Wait time for Web Element Selection State to be " + selectionState + " has expired!");
			Assert.fail("Encountered TimeoutException while waiting for Web Element Selection State to be "
					+ selectionState + " has expired!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for Web Element Selection State to be " + selectionState
					+ " has expired!");
			Assert.fail("Encountered Exception while waiting for Web Element Selection State to be " + selectionState
					+ " has expired!");
		}
		return status;
	}

	/**
	 * Waits for a Javascript Alert to be present on the WebPage.
	 */

	private final Alert waitForAlertToBePresent() {
		try {
			this.alert = this.wait.until(ExpectedConditions.alertIsPresent());
		} catch (TimeoutException e) {
			this.log.error("Wait time for Alert to be displayed has expired!");
			Assert.fail("Encountered TimeoutException while getting Alert Message!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while getting Alert Message!");
			Assert.fail("Encountered Exception while getting Alert Message!");
		}
		return this.alert;
	}
	
}
