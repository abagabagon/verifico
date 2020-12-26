package com.github.abagabagon.verifico.automation.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.abagabagon.verifico.enums.Browser;

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
	
	private Browser browser;
	private boolean isHeadless;
	private SeleniumWebDriver seleniumWebDriver;
	
	public SeleniumWebAutomation(Browser browser) {
		this.log = LogManager.getLogger(this.getClass());
		this.seleniumWebDriver = new SeleniumWebDriver();
		this.browser = browser;
		this.isHeadless = false;
	}

	public SeleniumWebAutomation(Browser browser, boolean isHeadless) {
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
		this.log.debug("I open Web Browser.");
		this.driver = this.seleniumWebDriver.getWebDriver(this.browser, this.isHeadless);
		maximizeBrowserWindow();
		deleteAllCookies();
		initializeImplicitWait(20);
		initializeExplicitWait(20);
		this.seleniumWait = new SeleniumWait(this.wait);
	}
	
	@Override
	public void openTab(String url) {
		this.log.debug("I open New Tab.");
		try {
			this.javascriptExecutor = (JavascriptExecutor)this.driver;
			String link = "window.open('" + url + "', '_blank');";
			this.javascriptExecutor.executeScript(link);
		} catch (NullPointerException e) {
			this.log.fatal("Unable to open New Tab. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to navigate to Url \"" + url + "\" .");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void goTo(String url) {
		this.log.debug("I navigate to URL: \"" + url + "\".");
		try {
			this.driver.get(url);
		} catch (NullPointerException e) {
			this.log.fatal("Unable to navigate to Url \"" + url + ". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to navigate to Url \"" + url + "\" .");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public boolean switchTabByTitle(String title) {
		this.log.debug("I switch to Tab with Page Title: \"" + title + "\".");
		String parentWindow = null;
		Set<String> windows = null;
		boolean isExisting = false;
		try {
			parentWindow = this.driver.getWindowHandle();
			windows = this.driver.getWindowHandles();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to get current browser windows. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get current browser windows.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
		for (String windowId : windows) {
			if (!windowId.equals(parentWindow)) {
				try {
					this.driver.switchTo().window(windowId);
					if (this.driver.getTitle().equals(title)) {
						this.log.debug("Successfully switched to Tab with Page Title: \"" + title + "\".");
						isExisting = true;
					}
					this.driver.switchTo().window(parentWindow);
				} catch (NoSuchWindowException e) {
					this.log.error("Tab with Page Title: \"" + title + "\" could not be found. Please check if provided Page Title is correct.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				} catch (Exception e) {
					this.log.error("Something went wrong while trying to switch tab by Page Title: \"" + title + "\".");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		if (!isExisting) {
			this.log.error("Tab with the Page Title: \"" + title + "\" was not found.");
		}
		return isExisting;
	}
	
	@Override
	public boolean switchTabByURL(String url) {
		this.log.debug("I switch to Tab with Page URL: \"" + url + "\".");
		String parentWindow = null;
		Set<String> windows = null;
		boolean isExisting = false;
		try {
			parentWindow = this.driver.getWindowHandle();
			windows = this.driver.getWindowHandles();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to get current browser windows. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get current browser windows.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
		for (String windowId : windows) {
			if (!windowId.equals(parentWindow)) {
				try {
					this.driver.switchTo().window(windowId);
					if (this.driver.getCurrentUrl().equals(url)) {
						this.log.debug("Successfully switched to Tab with Page URL: \"" + url + "\".");
						isExisting = true;
					}
					this.driver.switchTo().window(parentWindow);
				} catch (NoSuchWindowException e) {
					this.log.error("Tab with Page URL: \"" + url + "\" could not be found. Please check if provided Page URL is correct.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				} catch (Exception e) {
					this.log.error("Something went wrong while trying to switch tab by Page URL: \"" + url + "\".");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		if (!isExisting) {
			this.log.debug("Tab with the Page URL: \"" + url + "\" was not found.");
		}
		return isExisting;
	}
	
	@Override
	public void switchTabToOriginal() {
		this.log.debug("I switch to Original Tab.");
		try {
			this.tabs = new ArrayList<String>(this.driver.getWindowHandles());
		} catch (NullPointerException e) {
			this.log.fatal("Unable to get current browser tabs. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get current browser tabs.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
		try {
			this.driver.switchTo().window(this.tabs.get(0));
		} catch (NoSuchWindowException e) {
			this.log.error("Encountered an error while trying to switch back to original tab.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to switch back to original tab.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void back() {
		this.log.debug("I click back.");
		try {
			this.driver.navigate().back();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click back. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click back.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void forward() {
		this.log.debug("I click forward.");
		try {
			this.driver.navigate().forward();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click forward. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click forward.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void refresh() {
		this.log.debug("I click refresh.");
		try {
			this.driver.navigate().refresh();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click refresh. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click refresh.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void closeTab() {
		try {
			this.log.debug("I close Tab.");
			this.driver.close();
		} catch (NullPointerException e) {
			this.log.error("Unable to close Tab. Tab might have already been closed or browser was never opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to close Tab.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void closeBrowser() {
		try {
			this.log.debug("I close Browser.");
			this.driver.quit();
		} catch (NullPointerException e) {
			this.log.error("Unable to close Browser. Browser might have already been closed or was never opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to close Browser.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void maximizeBrowserWindow() {
		this.log.trace("I maximize Web Browser Window.");
		try {
			this.driver.manage().window().maximize();
		} catch (NullPointerException e) {
			this.log.error("Unable to maximize browser window. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to maximize browser window.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void deleteAllCookies() {
		this.log.trace("I delete all cookies.");
		try {
			this.driver.manage().deleteAllCookies();
		} catch (NullPointerException e) {
			this.log.error("Unable to delete all cookies. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to delete all cookies.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void scroll(String pixelHorizontal, String pixelVertical) {
		this.log.trace("I scroll page.");
		String script = "window.scrollBy(" + pixelHorizontal + ", " + pixelVertical + ")";
		try {
			this.javascriptExecutor = (JavascriptExecutor) this.driver;
			this.javascriptExecutor.executeScript(script);
		} catch (NullPointerException e) {
			this.log.error("Unable to scroll page. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	void initializeImplicitWait(long duration) {
		this.log.trace("I initialize Implicit Wait.");
		try {
			this.driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Implicit Wait. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}
	
	void initializeExplicitWait(long duration) {
		this.log.trace("I initialize Explicit Wait.");
		try {
			this.wait = new WebDriverWait(this.driver, duration);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Explicit Wait. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/* ####################################################### */
	/*                       USER ACTIONS                      */
	/* ####################################################### */
	
	WebElement getElement(By locator) {
		WebElement element = null;
		try {
			element = this.driver.findElement(locator);
		} catch (NullPointerException e) {
			this.log.warn("Unable to get Web Element. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to get Web Element. Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBePresent(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Unable to get Web Element. Unable to find the Web Element.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBePresent(locator);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to get Web Element.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBePresent(locator);
		}
		return element;
	}
	
	List<WebElement> getElements(By locator) {
		List<WebElement> elements = null;
		try {
			elements = this.driver.findElements(locator);
		} catch (NullPointerException e) {
			this.log.warn("Unable to get Web Elements. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to get Web Elements. The Web Elements are no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			elements = this.seleniumWait.waitForObjectsToBePresent(locator);
		} catch (NoSuchElementException e) {
			this.log.warn("Unable to get Web Elements. Unable to find the Web Elements.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			elements = this.seleniumWait.waitForObjectsToBePresent(locator);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to get Web Element.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			elements = this.seleniumWait.waitForObjectsToBePresent(locator);
		}
		return elements;
	}
	
	@Override
	public void point(By locator) {
		this.log.debug("I point at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.moveToElement(element).perform();
		} catch (NullPointerException e) {
			this.log.warn("Unable to point at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.action.moveToElement(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to point at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.action.moveToElement(element).perform();
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to point at Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.action.moveToElement(element).perform();
		}
	}
	
	@Override
	public void pointJS(By locator) {
		this.log.trace("I scroll to Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		String script = "window.scrollTo(0,"+ element.getLocation().y + ")";
		try {
			this.javascriptExecutor = (JavascriptExecutor) this.driver;
			this.javascriptExecutor.executeScript(script);
		} catch (NullPointerException e) {
			this.log.error("Unable to scroll to Web Element. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll to Web Element.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void click(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (NullPointerException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (ElementClickInterceptedException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			this.point(locator);
			this.clickJS(locator);
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to click Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
		}
	}
	
	@Override
	public void clickJS(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.javascriptExecutor = (JavascriptExecutor) this.driver;
			this.javascriptExecutor.executeScript("arguments[0].click();", element);
		} catch (NullPointerException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.javascriptExecutor.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to click Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.javascriptExecutor.executeScript("arguments[0].click();", element);
		}
	}
	
	@Override
	public void clickAndHold(By locator) {
		this.log.debug("I click and hold Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.clickAndHold(element).perform();
		} catch (NullPointerException e) {
			this.log.warn("Unable to click and hold Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to click and hold Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		} catch (ElementClickInterceptedException e) {
			this.log.warn("Unable to click and hold Web Element: \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			this.point(locator);
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to click and hold Web Element: \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		} catch (Exception e) {
			this.log.warn("Unable click and hold Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.clickAndHold(element).perform();
		}
	}
	
	@Override
	public void clickFromListBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from Object List based on Text: \"" + textToCheck + "\".");
		List<WebElement> elements = this.getElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elements.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				} catch (NullPointerException e) {
					this.log.warn("Unable to click a Web Element based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to click a Web Element based on Text. The Web Element is no longer present in the Web Page.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				} catch (ElementClickInterceptedException e) {
					this.log.warn("Unable to click a Web Element based on Text. The Web Element is unclickable because it's not on view.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					this.point(objectList);
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to click a Web Element based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				} catch (Exception e) {
					this.log.warn("Something went wrong while trying to click Web Element.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					elements.get(i).click();
				}
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
		this.log.debug("I click a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClick = this.getElements(objectToClick);
		int size = elementToClick.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				} catch (NullPointerException e) {
					this.log.warn("Unable to click a Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to click a Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				} catch (ElementClickInterceptedException e) {
					this.log.warn("Unable to click a Web Element from a Table based on Text. The Web Element is unclickable because it's not on view.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					this.point(objectToClick);
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to click a Web Element from a Table based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				} catch (Exception e) {
					this.log.warn("Something went wrong while trying to click Web Element from a Table based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToClick);
					elementToClick.get(i).click();
				}
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
		this.log.debug("I double click Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			this.action = new Actions(this.driver);
			this.action.doubleClick(element).perform();
		} catch (NullPointerException e) {
			this.log.warn("Unable to double click Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to double click at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		} catch (ElementClickInterceptedException e) {
			this.log.warn("Unable to click a Web Element from a Table based on Text. The Web Element is unclickable because it's not on view.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			this.point(locator);
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to click a Web Element from a Table based on Text. The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		} catch (Exception e) {
			this.log.warn("Encountered Exception while double clicking at Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.action.doubleClick(element).perform();
		}
	}
	
	@Override
	public void doubleClickFromListBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I double click a Web Element from Object List based on Text: \"" + textToCheck + "\".");
		List<WebElement> elements = this.getElements(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elements.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					this.action = new Actions(this.driver);
					this.action.doubleClick(elements.get(i)).perform();
				} catch (NullPointerException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					this.action.doubleClick(elements.get(i)).perform();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element is no longer present in the Web Page.");
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					this.action.doubleClick(elements.get(i)).perform();
				} catch (ElementClickInterceptedException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element is unclickable because it's not on view.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					this.point(objectList);
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					this.action.doubleClick(elements.get(i)).perform();
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					this.action.doubleClick(elements.get(i)).perform();
				} catch (Exception e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elements = this.seleniumWait.waitForObjectsToBeVisible(objectList);
					this.action.doubleClick(elements.get(i)).perform();
				}
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
		this.log.debug("I double click a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClick = this.getElements(objectToDoubleClick);
		int size = elementToClick.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					this.action = new Actions(this.driver);
					this.action.doubleClick(elementToClick.get(i)).perform();
				} catch (NullPointerException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToDoubleClick);
					this.action.doubleClick(elementToClick.get(i)).perform();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element is no longer present in the Web Page.");
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToDoubleClick);
					this.action.doubleClick(elementToClick.get(i)).perform();
				} catch (ElementClickInterceptedException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element is unclickable because it's not on view.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					this.point(objectToDoubleClick);
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToDoubleClick);
					this.action.doubleClick(elementToClick.get(i)).perform();
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToDoubleClick);
					this.action.doubleClick(elementToClick.get(i)).perform();
				} catch (Exception e) {
					this.log.warn("Unable to double click a Web Element from a List based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClick = this.seleniumWait.waitForObjectsToBeVisible(objectToDoubleClick);
					this.action.doubleClick(elementToClick.get(i)).perform();
				}
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
		this.log.debug("I drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
		WebElement sourceElement = this.getElement(sourceObject);
		WebElement targetElement = this.getElement(targetObject);
		try {
			this.action = new Actions(this.driver);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		} catch (NullPointerException e) {
			this.log.warn("Unable to drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceObject);
			targetElement = this.seleniumWait.waitForObjectToBeClickable(targetObject);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\". One or both of the Web Elements are no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceObject);
			targetElement = this.seleniumWait.waitForObjectToBeClickable(targetObject);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceObject);
			targetElement = this.seleniumWait.waitForObjectToBeClickable(targetObject);
			this.action.dragAndDrop(sourceElement, targetElement).perform();
		}
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			element.sendKeys(inputText);
		} catch (NullPointerException e) {
			this.log.warn("Unable to type text at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to type text at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(inputText);
		} catch (IllegalArgumentException e) {
			this.log.warn("Unable to type text at Web Element: \"" + locator.toString() + "\". Input Text is NULL.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to type text at Web Element: \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.click();
			element.sendKeys(inputText);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to type text at Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(inputText);
		}
	}
	
	@Override
	public void typeJS(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.javascriptExecutor = (JavascriptExecutor) this.driver;
			this.javascriptExecutor.executeScript("arguments[0].value=arguments[1];", element, inputText);
		} catch (NullPointerException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.javascriptExecutor.executeScript("arguments[0].value=arguments[1];", element, inputText);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to click Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.javascriptExecutor.executeScript("arguments[0].value=arguments[1];", element, inputText);
		}
	}
	
	@Override
	public void typeFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, String inputText) {
		this.log.debug("I type at Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToFill = this.getElements(objectToFill);
		int size = elementToFill.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					elementToFill.get(i).sendKeys(inputText);
				} catch (NullPointerException e) {
					this.log.warn("Unable to type text at Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(inputText);
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to type text at Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(inputText);
				} catch (IllegalArgumentException e) {
					this.log.warn("Encountered error while typing text at Web Element from a Table based on Text. Input Text is NULL.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to type text at Web Element from a Table based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).click();
					elementToFill.get(i).sendKeys(inputText);;
				} catch (Exception e) {
					this.log.warn("Encountered Exception while typing text at Web Element.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(inputText);
				}
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
		this.log.debug("I press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\"."); 
		WebElement element = this.getElement(locator);
		Keys keys = keyButton;
		try {
			element.sendKeys(keys);
		} catch (NullPointerException e) {
			this.log.warn("Unable to press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(keys);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(keys);
		} catch (IllegalArgumentException e) {
			this.log.warn("Unable to press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\". Input Text is NULL.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.click();
			element.sendKeys(keys);
		} catch (Exception e) {
			this.log.warn("Encountered Exception while typing text at Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.sendKeys(keys);
		}
	}
	
	@Override
	public void pressFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, Keys keyButton) {
		this.log.debug("I press keys at Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToFill = this.getElements(objectToFill);
		int size = elementToFill.size();
		Keys keys = keyButton;
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					elementToFill.get(i).sendKeys(keys);
				} catch (NullPointerException e) {
					this.log.warn("Unable to press \"" + keyButton + "\" at Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(keys);
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to press \"" + keyButton + "\" at Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(keys);
				} catch (IllegalArgumentException e) {
					this.log.warn("Unable to press \"" + keyButton + "\" at Web Element from a Table based on Text. Input Text is NULL.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to press \"" + keyButton + "\" at Web Element from a Table based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill.get(i).click();
					elementToFill.get(i).sendKeys(keys);
				} catch (Exception e) {
					this.log.warn("Unable to press \"" + keyButton + "\" at Web Element from a Table based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToFill = this.seleniumWait.waitForObjectsToBeVisible(objectToFill);
					elementToFill.get(i).sendKeys(keys);
				}
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
		this.log.debug("I clear Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		try {
			element.clear();
		} catch (NullPointerException e) {
			this.log.warn("Unable to clear text at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.clear();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to clear text at Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.clear();
		} catch (IllegalArgumentException e) {
			this.log.warn("Unable to clear text at Web Element: \"" + locator.toString() + "\". Input Text is NULL.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		} catch (InvalidElementStateException e) {
			this.log.warn("Unable to clear text at Web Element: \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			element.click();
			element.clear();
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to clear at Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			element.clear();
		}

	}
	
	@Override
	public void clearJS(By locator) {
		this.log.debug("I clear Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.javascriptExecutor = (JavascriptExecutor) this.driver;
			this.javascriptExecutor.executeScript("arguments[0].setAttribute('value', '');", element);
		} catch (NullPointerException e) {
			this.log.warn("Unable to click at Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.javascriptExecutor.executeScript("arguments[0].setAttribute('value', '');", element);
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to click Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeClickable(locator);
			this.javascriptExecutor.executeScript("arguments[0].setAttribute('value', '');", element);
		}
	}
	
	@Override
	public void clearFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToClear) {
		this.log.debug("I clear a Web Element from Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToClear = this.getElements(objectToClear);
		int size = elementToClear.size();
		boolean flgTextFound = false;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					elementToClear.get(i).clear();
				} catch (NullPointerException e) {
					this.log.warn("Unable to clear text at Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClear = this.seleniumWait.waitForObjectsToBeVisible(objectToClear);
					elementToClear.get(i).clear();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to clear text at Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClear = this.seleniumWait.waitForObjectsToBeVisible(objectToClear);
					elementToClear.get(i).clear();
				} catch (IllegalArgumentException e) {
					this.log.warn("Unable to clear text at Web Element from a Table based on Text. Input Text is NULL.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
				} catch (InvalidElementStateException e) {
					this.log.warn("Unable to clear text at Web Element from a Table based on Text. The Web Element might be disabled and unclickable.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClear.get(i).click();
					elementToClear.get(i).clear();
				} catch (Exception e) {
					this.log.warn("Something went wrong while trying to clear at Web Element from a Table based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToClear = this.seleniumWait.waitForObjectsToBeVisible(objectToClear);
					elementToClear.get(i).clear();
				}
				
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
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (NullPointerException e) {
			this.log.error("Failed to select the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element may not exist or unable to create Select Instance!");
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to select the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to select the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
		int size = this.select.getOptions().size();
		boolean flgOptionSelected = false;
		for (int i = 0; i < size; i++) {
			if (option.equals(this.select.getOptions().get(i).getText().trim())) {
				this.select.selectByVisibleText(option);
				flgOptionSelected = true;
				this.log.trace("I select Option \"" + option + "\".");
				break;
			}
		}
		if (flgOptionSelected == false) {
			this.log.error("Failed to select an option. Option \"" + option + "\" is invalid!");
		}
	}
	
	@Override
	public void select(By locator, By optionList, String option) {
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.clickJS(locator);
		this.clickFromListBasedOnText(optionList, option);
	}

	@Override
	public void deselect(By locator, String option) {
		this.log.debug("I deselect option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		WebElement element =  this.getElement(locator);
		try {
			this.select = new Select(element);
		} catch (NullPointerException e) {
			this.log.error("Failed to deselect the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element may not exist or unable to create Select Instance!");
		} catch (UnexpectedTagNameException e) {
			this.log.error("Failed to deselect the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\". Element is not a SELECT!");
		} catch (Exception e) {
			this.log.error("Something while trying to select the option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
		int size = this.select.getOptions().size();
		boolean flgOptionDeselected = false;
		for (int i = 0; i < size; i++) {
			if (option.equals(this.select.getOptions().get(i).getText().trim())) {
				this.log.trace("Deselecting Option \"" + option + "\".");
				this.select.deselectByVisibleText(option);
				flgOptionDeselected = true;
				this.log.trace("Successfully deselected Option \"" + option + "\".");
				break;
			}
		}
		if (flgOptionDeselected == false) {
			this.log.error("Failed to deselect an option. Option \"" + option + "\" is invalid!");
		}
	}

	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		String text = null;
		try {
			text = element.getText().trim();
			if (text.length() == 0) {
				this.log.trace("Web Element: \"" + locator.toString() + "\" has no text.");
			}
		} catch (NullPointerException e) {
			this.log.warn("Unable to retrieve text from Web Element. Browser might not have been opened or initialized.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getText().trim();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to retrieve text from Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getText().trim();
		} catch (Exception e) {
			this.log.warn("Unable to retrieve text from Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getText().trim();
		}
		return text;
	}
	
	@Override
	public String getTextFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetTextFrom) {
		this.log.debug("I get text from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetText = this.getElements(objectToGetTextFrom);
		int size = elementToGetText.size();
		boolean flgTextFound = false;
		String retrievedText = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					retrievedText = elementToGetText.get(i).getText().trim();
					if (text.length() == 0) {
						this.log.trace("Web Element: \"" + objectToGetTextFrom.toString() + "\" has no text.");
					}
				} catch (NullPointerException e) {
					this.log.warn("Unable to retrieve text from Web Element. Browser might not have been opened or initialized.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToGetText = this.seleniumWait.waitForObjectsToBeVisible(objectToGetTextFrom);
					retrievedText = elementToGetText.get(i).getText().trim();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to retrieve text from Web Element: \"" + objectToGetTextFrom.toString() + "\". The Web Element is no longer present in the Web Page.");
					elementToGetText = this.seleniumWait.waitForObjectsToBeVisible(objectToGetTextFrom);
					retrievedText = elementToGetText.get(i).getText().trim();
				} catch (Exception e) {
					this.log.warn("Something went wrong while trying to retrieve text from Web Element: \"" + objectToGetTextFrom.toString() + "\".");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToGetText = this.seleniumWait.waitForObjectsToBeVisible(objectToGetTextFrom);
					retrievedText = elementToGetText.get(i).getText().trim();
				}
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
		this.log.debug("I get value from Web Element: \"" + locator.toString() + "\".");
		String text = null;
		WebElement element = this.getElement(locator);
		try {
			text = element.getAttribute("value");
			if (text.length() == 0) {
				this.log.trace("The Text Box/Area Web Element: \"" + locator.toString() + "\" has no value.");
			}
		} catch (NullPointerException e) {
			this.log.warn("Unable to retrieve Text Box Value from Web Element: \"" + locator.toString() + "\". Browser might not have been opened or initialized.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute("value");
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to retrieve Text Box Value from Web Element: \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute("value");
		} catch (Exception e) {
			this.log.warn("Something went wrong while trying to retrieve Text Value from Web Element: \"" + locator.toString() + "\".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute("value");
		}
		return text;
	}
	
	@Override
	public String getValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom) {
		this.log.debug("I get value from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetValue = this.getElements(objectToGetValueFrom);
		int size = elementToGetValue.size();
		boolean flgTextFound = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					retrievedValue = elementToGetValue.get(i).getAttribute("value").trim();
				} catch (NullPointerException e) {
					this.log.warn("Unable to retrieve Text Box Value from Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute("value").trim();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to retrieve Text Box Value from Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute("value").trim();
				} catch (Exception e) {
					this.log.warn("Something went wrong while trying to retrieve Text Box Value from Web Element from a Table based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute("value").trim();
				}
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
		this.log.debug("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = null;
		WebElement element = this.getElement(locator);
		try {
			text = element.getAttribute(attribute);
			if (text.length() == 0) {
				this.log.trace("The Attribute: " + attribute + " of Web Element: \"" + locator.toString() + "\".");
			}
		} catch (NullPointerException e) {
			this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text. Browser might not have been opened or initialized.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute(attribute);
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute(attribute);
		} catch (Exception e) {
			this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			text = element.getAttribute(attribute);
		}
		return text;
	}
	
	@Override
	public String getAttributeValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom, String attribute) {
		this.log.debug("I get attribute value from a Web Element from a Table based on Text: \"" + textToCheck + "\".");
		List<WebElement> elementToCheckText = this.getElements(objectToCheckText);
		List<WebElement> elementToGetValue = this.getElements(objectToGetValueFrom);
		int size = elementToGetValue.size();
		boolean flgTextFound = false;
		String retrievedValue = null;
		for(int i = 0; i < size; i++) {
			String text = elementToCheckText.get(i).getText().trim();
			if (text.equals(textToCheck)) {
				try {
					retrievedValue = elementToGetValue.get(i).getAttribute(attribute).trim();
				} catch (NullPointerException e) {
					this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text. Browser might not have been opened or initialized.");
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute(attribute).trim();
				} catch (StaleElementReferenceException e) {
					this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text. The Web Element is no longer present in the Web Page.");
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute(attribute).trim();
				} catch (Exception e) {
					this.log.warn("Unable to retrieve Attribute Value from Web Element from a Table based on Text.");
					this.log.trace(ExceptionUtils.getStackTrace(e));
					elementToGetValue = this.seleniumWait.waitForObjectsToBeVisible(objectToGetValueFrom);
					retrievedValue = elementToGetValue.get(i).getAttribute(attribute).trim();
				}
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
		this.log.debug("I get value from Drop-down List Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		this.select = new Select(element);
		String text = null;
		try {
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
			if (text.length() == 0) {
				this.log.warn("The Drop-down List Web Element: \"" + locator.toString() + "\" has no value.");
			}
		} catch (NullPointerException e) {
			this.log.warn("Unable to retrieve Drop-down List Web Element: \"" + locator.toString() + "\" Value. Browser might not have been opened or initialized.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		} catch (StaleElementReferenceException e) {
			this.log.warn("Unable to retrieve Drop-down List Web Element: \"" + locator.toString() + "\" Value. The Web Element is no longer present in the Web Page.");
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		} catch (Exception e) {
			this.log.trace("Something went wrong while trying to retrieve Drop-down List Web Element: \"" + locator.toString() + "\" Value.");
			this.log.trace(ExceptionUtils.getStackTrace(e));
			element = this.seleniumWait.waitForObjectToBeVisible(locator);
			this.select = new Select(element);
			text = this.select.getFirstSelectedOption().getText().toLowerCase();
		}
		return text;
	}
	
	@Override
	public void wait(int duration) {
		this.log.debug("I wait for " + duration + " Seconds.");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for " + duration + ".");
			this.log.trace(ExceptionUtils.getStackTrace(e));
		}
	}

	/* ####################################################### */
	/*                      VERIFICATIONS                      */
	/* ####################################################### */
	
	@Override
	public boolean seeUrl(String expectedUrl) {
		this.log.debug("I verify Page URL: \"" + expectedUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToBe(expectedUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.debug("I see Page URL: \"" + expectedUrl + "\".");
		} else {
			status = false;
			this.log.error("I don't see Page URL: \"" + expectedUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeUrl(String url) {
		this.log.debug("I verify Page URL is not \"" + url + "\".");
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.equals(url);
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.error("I see Page URL: \"" + url + "\".");
		} else {
			status = false;
			this.log.debug("I don't see Page URL: \"" + url + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seeTitle(String expectedTitle) {
		this.log.debug("I verify Page Title: \"" + expectedTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToBe(expectedTitle);
		String actualTitle = this.driver.getTitle().trim();
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.debug("I saw Page Title: \"" + expectedTitle + "\".");
		} else {
			status = false;
			this.log.error("I don't see Page Title: \"" + expectedTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTitle(String title) {
		this.log.debug("I verify Page Title is not \"" + title + "\".");
		String actualTitle = this.driver.getTitle().trim();
		boolean isTitleEqual = actualTitle.equals(title);
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.error("I saw Page Title: \"" + title + "\".");
		} else {
			status = false;
			this.log.debug("I don't see Page Title: \"" + title + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean typed(By locator, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" is typed at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see value: \"" + expectedValue + "\" from Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see value: \"" + expectedValue + "\" from Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean didntType(By locator, String value) {
		this.log.debug("I verify \"" + value + "\" is not typed at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.error("I see value: \"" + value + "\" from Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.debug("I don't see value: \"" + value + "\" from Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" is displayed for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see value: \"" + expectedValue + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I verify \"" + value + "\" is not displayed for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.error("I see value: \"" + value + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.debug("I don't see value: \"" + value + "\" for attribute: \"" + attribute + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean selectedDropDown(By locator, String expectedValue) {
		this.log.debug("I verify \"" + expectedValue + "\" Drop-down List Value is selected at Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.getElement(locator);
		this.select = new Select(element);
		String actualValue = this.select.getFirstSelectedOption().getText().toLowerCase();
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I selected: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I didn't select: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;
	}

	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("Verifying \"" + expectedValue + "\" Text Value is displayed.");
		String actualText = this.getText(locator);
		boolean isValueEqual = actualText.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see text: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.error("I don't see text: \"" + expectedValue + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("Verifying \"" + value + "\" Text Value is not displayed.");
		String actualText = this.getText(locator);
		boolean isValueEqual = actualText.equals(value);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.error("I see text: \"" + value + "\" at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = false;
			this.log.debug("I don't see text: \"" + value + "\" at Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seeTextFromList(By locator, String textValue) {
		this.log.debug("Verifying \"" + textValue + "\" Text Value is displayed on List.");
		List<WebElement> element = this.getElements(locator);
		int size = element.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 0; i < size; i++) {
			String text = element.get(i).getText().trim();
			if (text.equals(textValue)) {
				status = true;
				flgTextFound = true;
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error("The text \"" + textValue + "\" is not found from List.");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextFromList(By locator, String textValue) {
		this.log.debug("Verifying \"" + textValue + "\" Text Value is not displayed on List.");
		List<WebElement> element = this.getElements(locator);
		int size = element.size();
		boolean status = true;
		for(int i = 0; i < size; i++) {
			String text = element.get(i).getText().trim();
			if (text.equals(textValue)) {
				status = false;
				this.log.error("The text \"" + textValue + "\" is found from List.");
				break;
			}
		}
		return status;
	}

	@Override
	public boolean see(By locator) {
		this.log.debug("I verify Web Element: \"" + locator.toString() + "\" is displayed.");
		List<WebElement> elements = this.getElements(locator);
		boolean status = false;
		if (elements.size() > 0) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is displayed.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not displayed.");
		}
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I verify Web Element: \"" + locator.toString() + "\" is not displayed.");
		this.initializeImplicitWait(2);
		this.initializeExplicitWait(2);
		List<WebElement> elements = this.getElements(locator);
		boolean status = false;
		if (elements.size() == 0) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is not displayed.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is displayed.");
		}
		this.initializeImplicitWait(20);
		this.initializeExplicitWait(20);
		return status;
	}
	
	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("Verifying element \"" + locator.toString() + "\" is enabled.");
		WebElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is enabled.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not enabled.");
		}
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("Verifying element \"" + locator.toString() + "\" is disabled.");
		WebElement element = this.getElement(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (!isEnabled) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is disabled.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not disabled.");
		}
		return status;
	}

	@Override
	public boolean selected(By locator) {
		this.log.debug("Verifying element \"" + locator.toString() + "\" is selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, true);
		WebElement element = this.getElement(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (isSelected) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is selected.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is not selected.");
		}
		return status;
	}

	@Override
	public boolean deselected(By locator) {
		this.log.debug("Verifying element \"" + locator.toString() + "\" is not selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, false);
		WebElement element = this.getElement(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (!isSelected) {
			status = true;
			this.log.debug("I verified Web Element: \"" + locator.toString() + "\" is not selected.");
		} else {
			status = false;
			this.log.error("I verified Web Element: \"" + locator.toString() + "\" is selected.");
		}
		return status;
	}

	@Override
	public boolean seeAlertMessage(String expectedMessage) {
		this.log.debug("Verifying \"" + expectedMessage + "\" Alert Message is displayed.");
		this.alert = this.seleniumWait.waitForAlertToBePresent();
		String actualMessage = this.alert.getText();
		boolean isValueEqual = actualMessage.equals(expectedMessage);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I see alert message: \"" + expectedMessage + "\".");
		} else {
			status = false;
			this.log.error("I don't see alert message: \"" + expectedMessage + "\".");
		}
		return status;
	}

}