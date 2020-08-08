package automation.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.TestStatus;

public class SeleniumWebAutomation implements WebAutomation {

	WebDriver driver;
	Logger log;
	WebDriverWait wait;
	Select select;
	Alert alert;
	Actions action;
	JavascriptExecutor javascriptExecutor;
	ArrayList<String> tabs;
	SeleniumWait seleniumWait;
	
	private String browser;
	private boolean isHeadless;
	private SeleniumWebDriver seleniumWebDriver;
	
	public SeleniumWebAutomation(String browser) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.browser = browser;
		this.isHeadless = false;
	}

	public SeleniumWebAutomation(String browser, boolean isHeadless) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.browser = browser;
		this.isHeadless = isHeadless;
	}

	/* ####################################################### */
	/*                     BROWSER ACTIONS                     */
	/* ####################################################### */

	@Override
	public void openBrowser() {
		this.log.info("I open Web Browser.");
		this.driver = this.seleniumWebDriver.getWebDriver(this.isHeadless, this.browser);
		maximizeBrowserWindow();
		deleteAllCookies();
		initializeImplicitWait(20);
		initializeExplicitWait(20);
		this.seleniumWait = new SeleniumWait(this.wait);
	}
	
	@Override
	public void openTab(String url) {
		this.log.info("I open New Tab.");
		this.javascriptExecutor = (JavascriptExecutor)this.driver;
		String link = "window.open('" + url + "', '_blank');";
		this.javascriptExecutor.executeScript(link);
	}

	@Override
	public void goTo(String url) {
		this.log.info("I navigate to URL: \"" + url + "\".");
		try {
			this.driver.get(url);
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
		} catch (Exception e) {
			this.log.fatal("Encountered Exception while navigating to Url \"" + url + "\" .");
		}
	}
	
	@Override
	public boolean switchTabByTitle(String title) {
		this.log.info("I switch to Tab with Page Title: \"" + title + "\".");
		String parentWindow = this.driver.getWindowHandle();
		Set<String> windows = this.driver.getWindowHandles();
		this.log.info("Total Tabs Open: " + windows.size());
		boolean isExisting = false;
		for (String winID : windows) {
			if (!winID.equals(parentWindow)) {
				this.driver.switchTo().window(winID);
				if (this.driver.getTitle().equals(title)) {
					this.log.info("Successfully switched to Tab with Page Title: \"" + title + "\".");
					isExisting = true;
				}
				this.driver.switchTo().window(parentWindow);
			}
		}
		if (!isExisting) {
			this.log.error("Tab with the Page Title: \"" + title + "\" was not found.");
		}
		return isExisting;
	}
	
	@Override
	public boolean switchTabByURL(String url) {
		this.log.info("I switch to Tab with Page URL: \"" + url + "\".");
		String parentWindow = this.driver.getWindowHandle();
		Set<String> windows = this.driver.getWindowHandles();
		this.log.info("Total Tabs Open " + windows.size());
		boolean isExisting = false;
		for (String winID : windows) {
			if (!winID.equals(parentWindow)) {
				this.driver.switchTo().window(winID);
				if (this.driver.getCurrentUrl().equals(url)) {
					this.log.info("Successfully switched to Tab with Page URL: \"" + url + "\".");
					isExisting = true;
				}
				this.driver.switchTo().window(parentWindow);
			}
		}
		if (!isExisting) {
			this.log.info("Tab with the Page URL: \"" + url + "\" was not found.");
		}
		return isExisting;
	}
	
	@Override
	public void switchTabToOriginal() {
		this.log.info("I switch to Original Tab.");
		this.tabs = new ArrayList<String>(this.driver.getWindowHandles());
		this.driver.switchTo().window(this.tabs.get(0));
	}

	@Override
	public void back() {
		this.log.info("I click back.");
		this.driver.navigate().back();
	}

	@Override
	public void forward() {
		this.log.info("I click forward.");
		this.driver.navigate().forward();
	}

	@Override
	public void refresh() {
		this.log.info("I click refresh.");
		this.driver.navigate().refresh();
	}

	@Override
	public void closeTab() {
		try {
			this.log.info("I close Tab.");
			this.driver.close();
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while closing Tab.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing Tab.");
			e.printStackTrace();
		}
	}

	@Override
	public void closeBrowser() {
		try {
			this.log.info("I close Browser.");
			this.driver.quit();
			this.driver = null;
			this.log.info("I closed Browser.");
		} catch (NullPointerException e) {
			this.log.error("Encountered NullPointerException while closing Web Browser.");
		} catch (Exception e) {
			this.log.error("Encountered Exception while closing Web Browser.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void maximizeBrowserWindow() {
		this.log.debug("I maximize Web Browser Window.");
		this.driver.manage().window().maximize();
	}

	@Override
	public void deleteAllCookies() {
		this.log.debug("I delete all cookies.");
		this.driver.manage().deleteAllCookies();
	}
	
	@Override
	public void scroll(String pixelHorizontal, String pixelVertical) {
		this.log.debug("I scroll page.");
		String script = "window.scrollBy(" + pixelHorizontal + ", " + pixelVertical + ")";
		this.javascriptExecutor = (JavascriptExecutor) this.driver;
		this.javascriptExecutor.executeScript(script);
	}

	void initializeImplicitWait(long duration) {
		this.log.debug("I initialize Implicit Wait.");
		this.driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
	}
	
	void initializeExplicitWait(long duration) {
		this.log.debug("I initialize Explicit Wait.");
		this.wait = new WebDriverWait(this.driver, duration);
	}
	
	/* ####################################################### */
	/*                       USER ACTIONS                      */
	/* ####################################################### */
	
	WebElement getElement(By locator) {
		WebElement element = null;
		try {
			element = this.driver.findElement(locator);
		} catch (NullPointerException e) {
			this.log.warn("Encountered NullPointerException while getting WebElement.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while getting WebElement.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
		} catch (Exception e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
		}
		return element;
	}
	
	List<WebElement> getElements(By locator) {
		List<WebElement> elements;
		try {
			elements = this.driver.findElements(locator);
		} catch (NullPointerException e) {
			this.log.warn("Encountered NullPointerException while getting WebElement.");
			elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while getting WebElement.");
			elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
		} catch (Exception e) {
			this.log.warn("Encountered NoSuchElementException while getting WebElement.");
			e.printStackTrace();
			elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
		}
		return elements;
	}
	
	@Override
	public void point(By locator) {
		this.log.info("I point at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.moveToElement(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while pointing at Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.action.moveToElement(element).perform();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while pointing at Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.action.moveToElement(element).perform();
		}
	}

	@Override
	public void click(By locator) {
		this.log.info("I click Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while clicking Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clicking Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		}
	}
	
	@Override
	public void clickJS(By locator) {
		this.log.info("I click Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		this.javascriptExecutor = (JavascriptExecutor) this.driver;
		this.javascriptExecutor.executeScript("arguments[0].click();", element);
	}
	
	@Override
	public void clickAndHold(By locator) {
		this.log.info("I click and hold Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.clickAndHold(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while clicking and holding Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clicking and holding Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		}
	}
	
	@Override
	public void clickFromObjectListBasedOnText(By objectList, String textToCheck) {
		this.log.info("I click a Web Element from Object List based on Text: \"" + textToCheck + "\".");
		List<WebElement> elements = this.getElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elements.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elements.get(i).click();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Object List.");
		}
	}
	
	@Override
	public void clickFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToClick) {
		this.log.info("I click a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClick = this.getElements(objectToClick);
		int size = elementToClick.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elementToClick.get(i).click();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}
	
	@Override
	public void doubleClick(By locator) {
		this.log.info("I double click Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.doubleClick(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while double clicking at Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while double clicking at Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		}
	}
	
	@Override
	public void doubleClickFromObjectListBasedOnText(By objectList, String textToCheck) {
		this.log.info("I double click a Web Element from Object List based on Text: \"" + textToCheck + "\".");
		List<WebElement> elements = this.getElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elements.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				this.action = new Actions(this.driver);
				this.action.doubleClick(elements.get(i)).perform();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Object List.");
		}
	}
	
	@Override
	public void doubleClickFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToDoubleClick) {
		this.log.info("I double click a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClick = this.getElements(objectToDoubleClick);
		int size = elementToClick.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				this.action = new Actions(this.driver);
				this.action.doubleClick(elementToClick.get(i)).perform();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}
	
	@Override
	public void dragAndDrop(By sourceObject, By targetObject) {
		this.log.info("I drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
		WebElement sourceElement = this.getElement(sourceObject);
		WebElement targetElement = this.getElement(targetObject);
		try {
			this.action = new Actions(this.driver);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while dragging and dropping Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
			sourceElement = this.seleniumWait.waitForObjectToBeClickable((By)sourceObject);
			targetElement = this.seleniumWait.waitForObjectToBeVisible((By)targetObject);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clicking and holding Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
			e.printStackTrace();
			sourceElement = this.seleniumWait.waitForObjectToBeClickable((By)sourceObject);
			targetElement = this.seleniumWait.waitForObjectToBeVisible((By)targetObject);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		}
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.info("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			element.sendKeys(inputText);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while typing text at Web Element: \"" + locator.toString() + "\"");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (IllegalArgumentException e) {
			this.log.warn("Encountered IllegalArgumentException while typing text at Web Element: \"" + locator.toString() + "\". Input Text is NULL.");
		} catch (Exception e) {
			this.log.warn("Encountered Exception while typing text at Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(inputText);
		}
	}
	
	@Override
	public void typeJS(By locator, String inputText) {
		this.log.info("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		this.javascriptExecutor = (JavascriptExecutor) this.driver;
		this.javascriptExecutor.executeScript("arguments[0].value=arguments[1];", element, inputText);
	}
	
	@Override
	public void typeFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, String inputText) {
		this.log.info("I type at Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToFill = this.getElements(objectToFill);
		int size = elementToFill.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elementToFill.get(i).sendKeys(inputText);
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}
	
	@Override
	public void press(By locator, Keys keyButton) {
		this.log.info("I press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\"."); 
		WebElement element = this.getElement(locator);
		Keys keys = keyButton;
		try {
			element.sendKeys(keys);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while pressing \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(keys);
		} catch (IllegalArgumentException e) {
			this.log.warn("Encountered IllegalArgumentException while pressing \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\". Input Text is NULL");
		} catch (Exception e) {
			this.log.warn("Encountered Exception while pressing \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(keys);
		}
	}
	
	@Override
	public void pressFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, Keys keyButton) {
		this.log.info("I press keys at Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToFill = this.getElements(objectToFill);
		int size = elementToFill.size();
		Keys keys = keyButton;
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elementToFill.get(i).sendKeys(keys);
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}

	@Override
	public void clear(By locator) {
		this.log.info("I clear Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			element.clear();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while clearing Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.clear();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while clearing Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.clear();
		}
	}
	
	@Override
	public void clearFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToClear) {
		this.log.info("I clear a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClear = this.getElements(objectToClear);
		int size = elementToClear.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				elementToClear.get(i).clear();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
	}

	@Override
	public void select(By locator, String option) {
		this.log.info("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to select the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while selecting the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
			e.printStackTrace();
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
		}
		if (flgOptionSelected == false) {
			this.log.error("Failed to select an option. Option \"" + option + "\" is invalid!");
		}
	}

	@Override
	public void deselect(By locator, String option) {
		this.log.info("I deselect option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to deselect the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Encountered Exception while selecting the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
			e.printStackTrace();
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
	public String getText(By locator) {
		this.log.info("I get text from Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		String text = null;
		try {
			text = element.getText().trim();
			if (text.length() == 0) {
				this.log.debug("Web Element: \"" + locator.toString() + "\" has no text.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving text from Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.getText();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving text from Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.getText();
		}
		return text;
	}
	
	@Override
	public String getTextFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetTextFrom) {
		this.log.info("I get text from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetText = this.getElements(objectToGetTextFrom);
		int size = elementToGetText.size();
		boolean flgTextFound = false;
		String retrievedText = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				retrievedText = elementToGetText.get(i).getText().trim();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
		return retrievedText;
	}

	@Override
	public String getValue(By locator) {
		this.log.info("I get value from Web Element: \"" + locator.toString() + "\".");
		String text = null;
		WebElement element = this.getElement(locator);
		try {
			text = element.getAttribute("value");
			if (text.length() == 0) {
				this.log.debug("The Text Box/Area Web Element: \"" + locator.toString() + "\" has no value.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Text Box Value from Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute("value");
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving Text Box Value from Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute("value");
		}
		return text;
	}
	
	@Override
	public String getValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom) {
		this.log.info("I get value from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetValue = this.getElements(objectToGetValueFrom);
		int size = elementToGetValue.size();
		boolean flgTextFound = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				retrievedValue = elementToGetValue.get(i).getAttribute("value").trim();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
		return retrievedValue;
	}

	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.info("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = null;
		WebElement element = this.getElement(locator);
		try {
			text = element.getAttribute(attribute);
			if (text.length() == 0) {
				this.log.debug("The Attribute: " + attribute + " of Web Element: \"" + locator.toString() + "\".");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Attribute Value from Web Element: \"" + locator.toString() + "\".");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute(attribute);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while retrieving Attribute Value from Web Element: \"" + locator.toString() + "\".");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute(attribute);
		}
		return text;
	}
	
	@Override
	public String getAttributeValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom, String attribute) {
		this.log.info("I get attribute value from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetValue = this.getElements(objectToGetValueFrom);
		int size = elementToGetValue.size();
		boolean flgTextFound = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				retrievedValue = elementToGetValue.get(i).getAttribute(attribute).trim();
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textToCheck + "\" is not found from Table.");
		}
		return retrievedValue;
	}

	@Override
	public String getDropDownListValue(By locator) {
		this.log.info("I get value from Drop-down List Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		this.select = new Select(element);
		String text = null;
		try {
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
			if (text.length() == 0) {
				this.log.warn("The Drop-down List Web Element: \"" + locator.toString() + "\" has no value.");
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Encountered StaleElementReferenceException while retrieving Drop-down List Web Element: \"" + locator.toString() + "\" Value.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		} catch (Exception e) {
			this.log.debug("Encountered Exception while retrieving Drop-down List Web Element: \"" + locator.toString() + "\" Value.");
			e.printStackTrace();
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		}
		return text;
	}

	@Override
	public void wait(int duration) {
		this.log.info("I wait for " + duration + ".");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for " + duration + ".");
			e.printStackTrace();
		}
	}

	/* ####################################################### */
	/*                      VERIFICATIONS                      */
	/* ####################################################### */
	
	@Override
	public TestStatus verifyUrl(String expectedUrl) {
		this.log.info("I verify Page URL: \"" + expectedUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToBe(expectedUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		TestStatus status = TestStatus.FAILED;
		if(isUrlEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see Page URL: \"" + expectedUrl + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see Page URL: \"" + expectedUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public TestStatus verifyPartialUrl(String partialUrl) {
		this.log.info("I verify partial Page URL: \"" + partialUrl + "\".");
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.contains(partialUrl);
		TestStatus status = TestStatus.FAILED;
		if(isUrlEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see partial Page URL: \"" + partialUrl + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see partial Page URL: \"" + partialUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;	
	}
	
	@Override
	public TestStatus verifyTitle(String expectedTitle) {
		this.log.info("I verify Page Title: \"" + expectedTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToBe(expectedTitle);
		String actualTitle = this.driver.getTitle().trim();
		TestStatus status = TestStatus.FAILED;
		if(isTitleEqual) {
			status = TestStatus.PASSED;
			this.log.info("I saw Page Title: \"" + expectedTitle + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see Page Title: \"" + expectedTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public TestStatus verifyClickable(By locator) {
		this.log.info("I verify Web Element: \"" + locator.toString() + "\" is clickable.");
		WebElement element = this.seleniumWait.waitForObjectToBeClickable(locator);
		TestStatus status = TestStatus.FAILED;
		if (element != null) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is clickable.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not clickable.");
		}
		return status;
	}

	@Override
	public TestStatus verifyValue(By locator, String expectedValue) {
		this.log.info("I verify \"" + expectedValue + "\" is displayed at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(expectedValue);
		TestStatus status = TestStatus.FAILED;
		if(isValueEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see value: \"" + expectedValue + "\" from Web Element: \"" + locator.toString() + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see value: \"" + expectedValue + "\" from Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public TestStatus verifyAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.info("I verify \"" + expectedValue + "\" is displayed for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		TestStatus status = TestStatus.FAILED;
		if(isValueEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public TestStatus verifyDropDownListValue(By locator, String expectedValue) {
		this.log.info("I verify \"" + expectedValue + "\" Drop-down List Value is displayed at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		this.select = new Select(element);
		String actualValue = this.select.getFirstSelectedOption().getText().toLowerCase();
		boolean isValueEqual = actualValue.equals(expectedValue);
		TestStatus status = TestStatus.FAILED;
		if(isValueEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see value: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see value: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;
	}

	@Override
	public TestStatus verifyText(By locator, String expectedValue) {
		this.log.info("Verifying \"" + expectedValue + "\" Text Value is displayed.");
		String actualText = this.getText(locator);
		boolean isValueEqual = actualText.equals(expectedValue);
		TestStatus status = TestStatus.FAILED;
		if(isValueEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see text: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see text: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}

	@Override
	public TestStatus verifyDisplayed(By locator) {
		this.log.info("I verify Web Element: \"" + locator.toString() + "\" is displayed.");
		List<WebElement> elements = this.getElements(locator);
		TestStatus status = TestStatus.FAILED;
		if (elements.size() > 0) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is displayed.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not displayed.");
		}
		return status;
	}

	@Override
	public TestStatus verifyNotDisplayed(By locator) {
		this.log.info("I verify Web Element: \"" + locator.toString() + "\" is not displayed.");
		this.initializeImplicitWait(2);
		this.initializeExplicitWait(2);
		List<WebElement> elements = this.getElements(locator);
		TestStatus status = TestStatus.FAILED;
		if (elements.size() == 0) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is not displayed.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is displayed.");
		}
		this.initializeImplicitWait(20);
		this.initializeExplicitWait(20);
		return status;
	}
	
	@Override
	public TestStatus verifyEnabled(By locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is enabled.");
		WebElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		TestStatus status = TestStatus.FAILED;
		if (isEnabled) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is enabled.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not enabled.");
		}
		return status;
	}

	@Override
	public TestStatus verifyDisabled(By locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is disabled.");
		WebElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		TestStatus status = TestStatus.FAILED;
		if (!isEnabled) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is disabled.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not disabled.");
		}
		return status;
	}

	@Override
	public TestStatus verifySelected(By locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, true);
		WebElement element = this.getElement(locator);
		boolean isSelected = element.isSelected();
		TestStatus status = TestStatus.FAILED;
		if (isSelected) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is selected.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not selected.");
		}
		return status;
	}

	@Override
	public TestStatus verifyNotSelected(By locator) {
		this.log.info("Verifying element \"" + locator.toString() + "\" is not selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, false);
		WebElement element = this.getElement(locator);
		boolean isSelected = element.isSelected();
		TestStatus status = TestStatus.FAILED;
		if (!isSelected) {
			status = TestStatus.PASSED;
			this.log.info("I verified Web Element: \"" + locator.toString() + "\" is not selected.");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is selected.");
		}
		return status;
	}

	@Override
	public TestStatus verifyAlertMessage(String expectedMessage) {
		this.log.info("Verifying \"" + expectedMessage + "\" Alert Message is displayed.");
		this.alert = this.seleniumWait.waitForAlertToBePresent();
		String actualMessage = this.alert.getText();
		boolean isValueEqual = actualMessage.equals(expectedMessage);
		TestStatus status = TestStatus.FAILED;
		if(isValueEqual) {
			status = TestStatus.PASSED;
			this.log.info("I see alert message: \"" + expectedMessage + "\".");
		} else {
			status = TestStatus.FAILED;
			this.log.error("I don't see alert message: \"" + expectedMessage + "\".");
		}
		return status;
	}

}