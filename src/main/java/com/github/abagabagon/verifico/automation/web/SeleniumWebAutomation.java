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
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.utilities.OperatingSystem;

public class SeleniumWebAutomation implements WebAutomation {

	protected WebDriver driver;
	protected Logger log;
	private JavascriptExecutor javascriptExecutor;
	private Actions action;
	private ArrayList<String> tabs;
	private Browser browser;
	private boolean isHeadless;
	private SeleniumWebDriver seleniumWebDriver;
	private SeleniumWait seleniumWait;
	
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
	
	enum UserAction {
		CLEAR, CLICK, CLICKJS, CLICK_AND_HOLD, COUNT, DESELECT, DOUBLE_CLICK, DRAG_AND_DROP,
		GET_ATTRIBUTE, GET_DROPDOWN, GET_TEXT, POINT, PRESS, SELECT, SEND_KEYS
	}
	
	/* ####################################################### */
	/*                     BROWSER ACTIONS                     */
	/* ####################################################### */

	@Override
	public void openBrowser() {
		this.log.debug("I open Web Browser.");
		this.driver = this.seleniumWebDriver.getWebDriver(this.browser, this.isHeadless);
		this.maximizeBrowserWindow();
		this.deleteAllCookies();
		this.setImplicitWait(10);
		this.seleniumWait = new SeleniumWait(this.driver, this.setExplicitWait(15, this.driver));
		this.action = new Actions(this.driver);
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
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to navigate to Url \"" + url + "\" .");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void goTo(String url) {
		this.log.debug("I navigate to URL: \"" + url + "\".");
		try {
			this.driver.get(url);
			this.seleniumWait.waitForPage();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to navigate to Url \"" + url + ". Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to navigate to Url \"" + url + "\" has expired.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to navigate to Url \"" + url + "\" .");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public boolean switchTabByTitle(String title) {
		this.log.debug("I switch to Tab with Page Title: \"" + title + "\".");
		boolean isExisting = false;
		for(int i = 1; i <= 4; i++) {
			Set<String> windows = this.driver.getWindowHandles();
			for (String windowId: windows) {
				try {
					this.driver.switchTo().window(windowId);
					String currentTitle = this.driver.getTitle();
					if(currentTitle.equals(title)) {
						isExisting = true;
						this.log.debug("Successfully switched to Tab with Title: \"" + title + "\".");
						break;
					}
				} catch (NoSuchWindowException e) {
					this.log.error("Tab with Title: \"" + title + "\" could not be found. Please check if provided Page Title is correct.");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				} catch (Exception e) {
					this.log.error("Something went wrong while trying to switch tab by Title: \"" + title + "\".");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (!isExisting) {
				if(i < 4) {
					this.log.debug("Tab with the Page Title: \"" + title + "\" was not found. Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.debug("Tab with the Page Title: \"" + title + "\" was not found.");
				}
			} else {
				break;
			}
		}
		return isExisting;
	}
	
	@Override
	public boolean switchTabByURL(String url) {
		this.log.debug("I switch to Tab with Page URL: \"" + url + "\".");
		boolean isExisting = false;
		for(int i = 1; i <= 4; i++) {
			Set<String> windows = this.driver.getWindowHandles();
			for (String windowId: windows) {
				try {
					this.driver.switchTo().window(windowId);
					String currentUrl = this.driver.getCurrentUrl();
					if(currentUrl.equals(url)) {
						isExisting = true;
						this.log.debug("Successfully switched to Tab with URL: \"" + url + "\".");
						break;
					}
				} catch (NoSuchWindowException e) {
					this.log.error("Tab with URL: \"" + url + "\" could not be found. Please check if provided Page URL is correct.");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				} catch (Exception e) {
					this.log.error("Something went wrong while trying to switch tab by URL: \"" + url + "\".");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (!isExisting) {
				if(i < 4) {
					this.log.debug("Tab with the Page URL: \"" + url + "\" was not found. Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.debug("Tab with the Page URL: \"" + url + "\" was not found.");
				}
			} else {
				break;
			}
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
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to get current browser tabs.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		try {
			this.driver.switchTo().window(this.tabs.get(0));
		} catch (NoSuchWindowException e) {
			this.log.error("Encountered an error while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void back() {
		this.log.debug("I click back.");
		try {
			this.driver.navigate().back();
			this.seleniumWait.waitForPage();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click back. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click back.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void forward() {
		this.log.debug("I click forward.");
		try {
			this.driver.navigate().forward();
			this.seleniumWait.waitForPage();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click forward. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click forward.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void refresh() {
		this.log.debug("I click refresh.");
		try {
			this.driver.navigate().refresh();
			this.seleniumWait.waitForPage();
		} catch (NullPointerException e) {
			this.log.fatal("Unable to click refresh. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to click refresh.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void closeTab() {
		try {
			this.log.debug("I close Tab.");
			this.driver.close();
		} catch (NullPointerException e) {
			this.log.error("Unable to close Tab. Tab might have already been closed or browser was never opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to close Tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void closeBrowser() {
		try {
			this.log.debug("I close Browser.");
			this.driver.quit();
		} catch (NullPointerException e) {
			this.log.error("Unable to close Browser. Browser might have already been closed or was never opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to close Browser.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public void maximizeBrowserWindow() {
		this.log.trace("I maximize Web Browser Window.");
		try {
			this.driver.manage().window().maximize();
		} catch (NullPointerException e) {
			this.log.error("Unable to maximize browser window. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to maximize browser window.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public void deleteAllCookies() {
		this.log.trace("I delete all cookies.");
		try {
			this.driver.manage().deleteAllCookies();
		} catch (NullPointerException e) {
			this.log.error("Unable to delete all cookies. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to delete all cookies.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
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
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}

	void setImplicitWait(long duration) {
		this.log.trace("I initialize Implicit Wait.");
		try {
			this.driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Implicit Wait. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	WebDriverWait setExplicitWait(long duration, WebDriver driver) {
		this.log.trace("I initialize Explicit Wait.");
		WebDriverWait wait = null;
		try {
			wait = new WebDriverWait(driver, duration);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Explicit Wait. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return wait;
	}
	
	WebDriverWait setExplicitWait(long duration, WebElement element) {
		this.log.trace("I initialize Explicit Wait.");
		WebDriverWait wait = null;
		try {
			wait = new WebDriverWait((WebDriver) element, duration);
		} catch (NullPointerException e) {
			this.log.error("Unable to initialize Explicit Wait. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
		return wait;
	}
	
	/* ####################################################### */
	/*                       USER ACTIONS                      */
	/* ####################################################### */
	
	private void executeMouseCommands(UserAction userAction, By locator) {
		boolean actionPerformed = false;
		WebElement element = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				this.javascriptExecutor = (JavascriptExecutor) this.driver;
				switch(userAction) {
				case CLICK:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					element.click();
					break;
				case CLICKJS:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.javascriptExecutor.executeScript("arguments[0].click();", element);
					break;
				case CLICK_AND_HOLD:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.action.clickAndHold(element).perform();
					break;
				case DOUBLE_CLICK:
					element = this.seleniumWait.waitForObjectToBeClickable(locator);
					this.action.doubleClick(element).perform();
					break;
				case POINT:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					this.action.moveToElement(element).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				element = this.seleniumWait.waitForObjectToBeVisible(locator);
				script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(element).perform();
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (MoveTargetOutOfBoundsException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is out-of-bounds.");
				element = this.seleniumWait.waitForObjectToBeVisible(locator);
				script = "window.scrollTo(" + element.getLocation().x + ","+ element.getLocation().y + ")";
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(element).perform();
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void click(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		this.executeMouseCommands(UserAction.CLICK, locator);
	}
	
	@Override
	public void clickJS(By locator) {
		this.log.debug("I click Web Element: \"" + locator.toString() + "\".");
		this.executeMouseCommands(UserAction.CLICKJS, locator);
	}
	
	@Override
	public void clickAndHold(By locator) {
		this.log.debug("I click and hold Web Element: \"" + locator.toString() + "\".");
		this.executeMouseCommands(UserAction.CLICK_AND_HOLD, locator);
	}
	
	@Override
	public void doubleClick(By locator) {
		this.log.debug("I double click Web Element: \"" + locator.toString() + "\".");
		this.executeMouseCommands(UserAction.DOUBLE_CLICK, locator);
	}
	
	@Override
	public void point(By locator) {
		this.log.debug("I point at Web Element: \"" + locator.toString() + "\".");
		this.executeMouseCommands(UserAction.POINT, locator);
	}
	
	private void executeKeyboardCommands(UserAction userAction, By locator, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case CLEAR:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					Platform platform = OperatingSystem.getOS();
					if (platform == Platform.MAC) {
						this.action.click(element)
				        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					} else {
						this.action.click(element)
				        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					}
					break;
				case PRESS:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					element.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					element.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				element.click();
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void clear(By locator) {
		this.log.debug("I clear Web Element: \"" + locator.toString() + "\".");
		this.executeKeyboardCommands(UserAction.CLEAR, locator, null, null);
	}
	
	@Override
	public void press(By locator, Keys keyButton) {
		this.log.debug("I press \"" + keyButton + "\" at Web Element: \"" + locator.toString() + "\"."); 
		this.executeKeyboardCommands(UserAction.SEND_KEYS, locator, null, keyButton);
	}
	
	@Override
	public void type(By locator, String inputText) {
		this.log.debug("I type \"" + inputText + "\" at Web Element: \"" + locator.toString() + "\".");
		this.executeKeyboardCommands(UserAction.SEND_KEYS, locator, inputText, null);
	}
	
	private String executeGetCommands(UserAction userAction, By locator, String attribute) {
		boolean actionPerformed = false;
		WebElement element = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case GET_ATTRIBUTE:
					element = this.seleniumWait.waitForObjectToBePresent(locator);
					value = element.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					element = this.seleniumWait.waitForObjectToBePresent(locator);
					value = element.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	@Override
	public String getText(By locator) {
		this.log.debug("I get text from Web Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_TEXT, locator, null);
		return text;
	}
	
	@Override
	public String getValue(By locator) {
		this.log.debug("I get value from Web Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_ATTRIBUTE, locator, "value");
		return text;
	}
	
	@Override
	public String getAttributeValue(By locator, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_ATTRIBUTE, locator, attribute);
		return text;
	}
	
	@Override
	public String getDropDownListValue(By locator) {
		this.log.debug("I get value from Drop-down List Web Element: \"" + locator.toString() + "\".");
		String text = this.executeGetCommands(UserAction.GET_DROPDOWN, locator, null);
		return text;
	}
	
	private void executeSelectCommands(UserAction userAction, By locator, String option) {
		boolean actionPerformed = false;
		WebElement element = null;
		for(int i = 1; i <= 4; i++) {
			try {
				switch(userAction) {
				case SELECT:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					this.select(userAction, element, option);
					break;
				case DESELECT:
					element = this.seleniumWait.waitForObjectToBeVisible(locator);
					this.select(userAction, element, option);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void select(UserAction userAction, WebElement element, String option) {
		Select select = new Select(element);
		int size = select.getOptions().size();
		boolean flgOptionTicked = false;
		for (int j = 0; j < size; j++) {
			if (option.equals(select.getOptions().get(j).getText().trim())) {
				switch(userAction) {
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
	}
	
	@Override
	public void select(By locator, String option) {
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.executeSelectCommands(UserAction.SELECT, locator, option);
	}
	
	@Override
	public void select(By locator, By optionList, String option) {
		this.log.debug("I select option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.click(locator);
		this.clickOnListElementBasedOnText(optionList, option);
	}
	
	@Override
	public void deselect(By locator, String option) {
		this.log.debug("I deselect option: \"" + option + "\" from Web Element: \"" + locator.toString() + "\".");
		this.executeSelectCommands(UserAction.DESELECT, locator, option);
	}
	
	private void executeMouseCommands(UserAction userAction, By parent, By child, int index) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor = (JavascriptExecutor) this.driver;
				this.javascriptExecutor.executeScript(script);
				switch(userAction) {
				case CLICK:
					this.seleniumWait.waitForObjectToBeClickable(childElement);
					childElement.click();
					break;
				case CLICKJS:
					this.seleniumWait.waitForObjectToBeClickable(childElement);
					this.javascriptExecutor = (JavascriptExecutor) this.driver;
					this.javascriptExecutor.executeScript("arguments[0].click();", childElement);
					break;
				case CLICK_AND_HOLD:
					this.seleniumWait.waitForObjectToBeClickable(childElement);
					this.action.clickAndHold(childElement).perform();
					break;
				case DOUBLE_CLICK:
					this.seleniumWait.waitForObjectToBeClickable(childElement);
					this.action.doubleClick(childElement).perform();
					break;
				case POINT:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					this.action.moveToElement(childElement).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				this.seleniumWait.waitForObjectToBeVisible(childElement);
				script = "window.scrollTo(" + childElement.getLocation().x + ","+ childElement.getLocation().y + ")";
				this.javascriptExecutor = (JavascriptExecutor) this.driver;
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(childElement).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeKeyboardCommands(UserAction userAction, By parent, By child, int index, String inputText, Keys keyButton) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(userAction) {
				case CLEAR:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					Platform platform = OperatingSystem.getOS();
					if (platform == Platform.MAC) {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					} else {
						this.action.click(childElement)
				        .pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				        .pause(200).sendKeys(Keys.DELETE)
				        .perform();
					}
					break;
				case PRESS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(keyButton);
					break;
				case SEND_KEYS:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					childElement.sendKeys(inputText);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (InvalidElementStateException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element might be disabled and unclickable.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (IllegalArgumentException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Input Text is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private String executeGetCommands(UserAction userAction, By parent, By child, int index, String attribute) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String value = null;
		Select select = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(userAction) {
				case GET_ATTRIBUTE:
					value = childElement.getAttribute(attribute);
					break;
				case GET_DROPDOWN:
					value = select.getFirstSelectedOption().getText().toLowerCase();
					break;
				case GET_TEXT:
					value = childElement.getText().trim();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElement.click();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	@SuppressWarnings("unused")
	private String executeSelectCommands(UserAction userAction, By parent, By child, int index, String option) {
		boolean actionPerformed = false;
		WebElement parentElement = null;
		WebElement childElement = null;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			try {
				parentElement = this.seleniumWait.waitForObjectsToBeVisible(parent).get(index);
				childElement = this.seleniumWait.waitForNestedObjectToBePresent(parentElement, child);
				switch(userAction) {
				case SELECT:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					this.select(userAction, childElement, option);
					break;
				case DESELECT:
					this.seleniumWait.waitForObjectToBeVisible(childElement);
					this.select(userAction, childElement, option);
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (UnexpectedTagNameException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\". Element does not have a SELECT Tag.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				childElement.click();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Child Web Element \"" + child.toString() + "\" under Parent Web Element \"" + parent.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	private void executeListMouseCommands(UserAction userAction, By objectList, String textToCheck) {
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(objectList);
		int size = elements.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				String text = elements.get(j).getText().trim();
				if (text.equals(textToCheck)) {
					switch(userAction) {
					case CLICK:
						this.executeMouseCommands(UserAction.CLICK, objectList, j);
						break;
					case CLICKJS:
						this.executeMouseCommands(UserAction.CLICKJS, objectList, j);
						break;
					case CLICK_AND_HOLD:
						this.executeMouseCommands(UserAction.CLICK_AND_HOLD, objectList, j);
						break;
					case DOUBLE_CLICK:
						this.executeMouseCommands(UserAction.DOUBLE_CLICK, objectList, j);
						break;
					case POINT:
						this.executeMouseCommands(UserAction.POINT, objectList, j);
						break;
					default:
						this.log.fatal("Unsupported User Action.");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element List: \"" +  objectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	private void executeMouseCommands(UserAction userAction, By locator, int index) {
		boolean actionPerformed = false;
		List<WebElement> elements = null;
		String script = null;
		for(int i = 1; i <= 4; i++) {
			try {
				elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
				switch(userAction) {
				case CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					elements.get(index).click();
					break;
				case CLICKJS:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.javascriptExecutor = (JavascriptExecutor) this.driver;
					this.javascriptExecutor.executeScript("arguments[0].click();", elements.get(index));
					break;
				case CLICK_AND_HOLD:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.clickAndHold(elements.get(index)).perform();
					break;
				case DOUBLE_CLICK:
					this.seleniumWait.waitForObjectToBeClickable(elements.get(index));
					this.action.doubleClick(elements.get(index)).perform();
					break;
				case POINT:
					script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
					this.javascriptExecutor = (JavascriptExecutor) this.driver;
					this.javascriptExecutor.executeScript(script);
					this.action.moveToElement(elements.get(index)).perform();
					break;
				default:
					this.log.fatal("Unsupported User Action.");
				}
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (ElementClickInterceptedException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\". The Web Element is unclickable because it's not on view.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
				elements = this.seleniumWait.waitForObjectsToBeVisible(locator);
				script = "window.scrollTo(" + elements.get(index).getLocation().x + ","+ elements.get(index).getLocation().y + ")";
				this.javascriptExecutor = (JavascriptExecutor) this.driver;
				this.javascriptExecutor.executeScript(script);
				this.action.moveToElement(elements.get(index)).perform();
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(userAction) + "\" for Web Element \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void clickOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.executeListMouseCommands(UserAction.CLICK, objectList, textToCheck);
	}
	
	@Override
	public void clickJSOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I click a Web Element from the Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.executeListMouseCommands(UserAction.CLICKJS, objectList, textToCheck);
	}
	
	@Override
	public void doubleClickOnListElementBasedOnText(By objectList, String textToCheck) {
		this.log.debug("I double-click a Web Element from a Web Element List: \"" + objectList.toString() + "\" based on the text: \"" + textToCheck + "\".");
		this.executeListMouseCommands(UserAction.DOUBLE_CLICK, objectList, textToCheck);
	}
	
	private void executeTableMouseCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.equals(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case CLICK:
							this.executeMouseCommands(UserAction.CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICKJS:
							this.executeMouseCommands(UserAction.CLICKJS, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case CLICK_AND_HOLD:
							this.executeMouseCommands(UserAction.CLICK_AND_HOLD, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case DOUBLE_CLICK:
							this.executeMouseCommands(UserAction.DOUBLE_CLICK, rowObjectList, rowObjectToDoActionTo, j);
							break;
						case POINT:
							this.executeMouseCommands(UserAction.POINT, rowObjectList,rowObjectToDoActionTo,  j);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void clickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableMouseCommands(UserAction.CLICK, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableMouseCommands(UserAction.CLICKJS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClick);
	}
	
	@Override
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick) {
		this.log.debug("I click the Web Element: \"" + rowObjectToDoubleClick.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableMouseCommands(UserAction.DOUBLE_CLICK, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToDoubleClick);
	}
	
	private void executeTableKeyboardCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String inputText, Keys keyButton) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.equals(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case CLEAR:
							this.executeKeyboardCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null, null);
							break;
						case PRESS:
							this.executeKeyboardCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null, keyButton);
							break;
						case SEND_KEYS:
							this.executeKeyboardCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, inputText, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}
	
	@Override
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear) {
		this.log.debug("I clear Web Element: \"" + rowObjectToClear.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableMouseCommands(UserAction.CLEAR, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToClear);
	}
	
	@Override
	public void pressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton) {
		this.log.debug("I press \"" + keyButton.toString() + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableKeyboardCommands(null, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, null, keyButton);
	}
	
	@Override
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText) {
		this.log.debug("I type \"" + inputText + "\" on Web Element: \"" + rowObjectToTypeOn.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.executeTableKeyboardCommands(UserAction.SEND_KEYS, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToTypeOn, inputText, null);
	}
	
	private String executeTableGetCommands(UserAction userAction, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoActionTo, String attribute) {
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		String value = null;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String text = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					text = elementToCheckText.getText().trim();
				}
				if (text.equals(textToCheck)) {
					WebElement elementToDoActionTo = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToDoActionTo, j);
					if (elementToDoActionTo != null) {
						switch(userAction) {
						case GET_ATTRIBUTE:
							value = this.executeGetCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, attribute);
							break;
						case GET_DROPDOWN:
							value = this.executeGetCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						case GET_TEXT:
							value = this.executeGetCommands(userAction, rowObjectList, rowObjectToDoActionTo, j, null);
							break;
						default:
							this.log.fatal("Unsupported User Action.");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToDoActionTo.toString() + "\" to perform the User Action \"" + String.valueOf(userAction) + "\" on at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return value;
	}
	
	@Override
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute) {
		this.log.debug("I get attribute value from Web Element: \"" + rowObjectToGetAttributeValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.executeTableGetCommands(null, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetAttributeValueFrom, attribute);
		return retrievedValue;
	}
	
	@Override
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom) {
		this.log.debug("I get text from Web Element: \"" + rowObjectToGetTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedText = this.executeTableGetCommands(UserAction.GET_TEXT, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetTextFrom, null);
		return retrievedText;
	}
	
	@Override
	public String getValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom) {
		this.log.debug("I get value from Web Element: \"" + rowObjectToGetValueFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		String retrievedValue = this.executeTableGetCommands(UserAction.GET_ATTRIBUTE, rowObjectList, rowObjectToCheckText, textToCheck, rowObjectToGetValueFrom, "value");
		return retrievedValue;
	}
	
	@Override
	public void dragAndDrop(By sourceObject, By targetObject) {
		this.log.debug("I drag and drop Web Element: \"" + sourceObject.toString() + "\" to Web Element: \"" + targetObject.toString() + "\".");
		boolean actionPerformed = false;
		WebElement sourceElement = null;
		WebElement targetElement = null;
		for(int i = 1; i <= 4; i++) {
			try {
				sourceElement = this.seleniumWait.waitForObjectToBeClickable(sourceObject);
				targetElement = this.seleniumWait.waitForObjectToBeClickable(targetObject);
				this.action.dragAndDrop(sourceElement, targetElement).perform();
				actionPerformed = true;
			} catch (NullPointerException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". Element created is NULL.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (StaleElementReferenceException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". The Web Element is no longer present in the Web Page.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (TimeoutException e) {
				this.log.warn("Unable to perform \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\". Wait time has expired.");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				this.log.warn("Unable to perform \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\".");
				this.log.debug(ExceptionUtils.getStackTrace(e));
			}
			if (!actionPerformed) {
				if(i < 4) {
					this.log.debug("Retrying User Action \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\" " + i + "/3.");
					wait(1);
				} else {
					this.log.error("Failed to perform User Action \"" + String.valueOf(UserAction.DRAG_AND_DROP) + "\" for Web Element \"" + sourceObject.toString() + "\".");
				}
			} else {
				break;
			}
		}
	}

	@Override
	public void acceptAlert() {
		this.log.debug("I accept Javascript Alert.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.accept();
	}
	
	@Override
	public void cancelAlert() {
		this.log.debug("I cancel Javascript Alert.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.dismiss();
	}
	
	@Override
	public void typeAlert(String inputText) {
		this.log.debug("I type: \"" + inputText + "\" at Javascript Alert Text Box.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		alert.sendKeys(inputText);
	}
	
	@Override
	public int count(By locator) {
		this.log.debug("I count Web Element: \"" + locator.toString() + "\".");
		this.seleniumWait.waitForPage();
		this.setImplicitWait(2);
		List<WebElement> element = this.driver.findElements(locator);
		int size = element.size();
		this.setImplicitWait(10);
		this.log.debug("I counted " + size + " instances of Web Element: \"" + locator.toString() + "\".");
		return size;
	}
	
	@Override
	public void wait(int duration) {
		this.log.debug("I wait for " + duration + " Second(s).");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for " + duration + ".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/* ####################################################### */
	/*                      VERIFICATIONS                      */
	/* ####################################################### */
	
	@Override
	public boolean seeUrl(String expectedUrl) {
		this.log.debug("I see Page URL: \"" + expectedUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToBe(expectedUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.debug("I saw Page URL: \"" + expectedUrl + "\".");
		} else {
			this.log.error("I didn't see Page URL: \"" + expectedUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeUrl(String url) {
		this.log.debug("I see Page URL is not \"" + url + "\".");
		this.seleniumWait.waitForPage();
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.equals(url);
		boolean status = false;
		if(isUrlEqual) {
			this.log.error("I saw Page URL: \"" + url + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see Page URL: \"" + url + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialUrl(String partialUrl) {
		this.log.debug("I see partial Page URL: \"" + partialUrl + "\".");
		boolean isUrlEqual = this.seleniumWait.waitForUrlToContain(partialUrl);
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean status = false;
		if(isUrlEqual) {
			status = true;
			this.log.debug("I saw partial Page URL: \"" + partialUrl + "\".");
		} else {
			this.log.error("I didn't see partial Page URL: \"" + partialUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeePartialUrl(String partialUrl) {
		this.log.debug("I see partial Page URL is not \"" + partialUrl + "\".");
		this.seleniumWait.waitForPage();
		String actualUrl = this.driver.getCurrentUrl().trim();
		boolean isUrlEqual = actualUrl.contains(partialUrl);
		boolean status = false;
		if(isUrlEqual) {
			this.log.error("I saw partial Page URL: \"" + partialUrl + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see partial Page URL: \"" + partialUrl + "\". Actual URL is \"" + actualUrl + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seeTitle(String expectedTitle) {
		this.log.debug("I see Page Title: \"" + expectedTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToBe(expectedTitle);
		String actualTitle = this.driver.getTitle().trim();
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.debug("I saw Page Title: \"" + expectedTitle + "\".");
		} else {
			this.log.error("I didn't see Page Title: \"" + expectedTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTitle(String title) {
		this.log.debug("I see Page Title is not \"" + title + "\".");
		this.seleniumWait.waitForPage();
		String actualTitle = this.driver.getTitle().trim();
		boolean isTitleEqual = actualTitle.equals(title);
		boolean status = false;
		if(isTitleEqual) {
			this.log.error("I saw Page Title: \"" + title + "\".");
		} else {
			status = true;
			this.log.debug("I don't see Page Title: \"" + title + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialTitle(String expectedPartialTitle) {
		this.log.debug("I see partial Page Title: \"" + expectedPartialTitle + "\".");
		boolean isTitleEqual = this.seleniumWait.waitForTitleToContain(expectedPartialTitle);
		String actualTitle = this.driver.getTitle().trim();
		boolean status = false;
		if(isTitleEqual) {
			status = true;
			this.log.debug("I saw Page Title: \"" + expectedPartialTitle + "\".");
		} else {
			this.log.error("I didn't see Page Title: \"" + expectedPartialTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTitle(String partialTitle) {
		this.log.debug("I see partial Page Title is not \"" + partialTitle + "\".");
		this.seleniumWait.waitForPage();
		String actualTitle = this.driver.getTitle().trim();
		boolean isTitleEqual = actualTitle.contains(partialTitle);
		boolean status = false;
		if(isTitleEqual) {
			this.log.error("I saw Page Title: \"" + partialTitle + "\".");
		} else {
			status = true;
			this.log.debug("I don't see Page Title: \"" + partialTitle + "\". Actual Title is \"" + actualTitle + "\".");
		}
		return status;
	}
	
	@Override
	public boolean typed(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is typed on Web Element: \"" + locator.toString() + "\".");
		boolean isValueEqual = this.seleniumWait.waitForValueToBe(locator, expectedValue);
		String actualValue = this.getValue(locator);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I typed \"" + expectedValue + "\" on Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't type \"" + expectedValue + "\" on Web Element: \"" + locator.toString() + "\". Actual value typed is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean didntType(By locator, String value) {
		this.log.debug("I see \"" + value + "\" is not typed on Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getValue(locator);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I typed \"" + value + "\" on Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't type \"" + value + "\" on Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seeAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeeAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean seePartialAttributeValue(By locator, String attribute, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}
	
	@Override
	public boolean dontSeePartialAttributeValue(By locator, String attribute, String value) {
		this.log.debug("I see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		String actualValue = this.getAttributeValue(locator, attribute);
		boolean isValueEqual = actualValue.contains(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not the value for attribute: \"" + attribute + "\" of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;	
	}

	@Override
	public boolean selectedDropDown(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" is selected at Drop-down List Web Element: \"" + locator.toString() + "\".");
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		Select select = new Select(element);
		String actualValue = select.getFirstSelectedOption().getText().toLowerCase();
		boolean isValueEqual = actualValue.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I selected: \"" + expectedValue + "\" at Drop-down List Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't select: \"" + expectedValue + "\" at Drop-down List Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualValue + "\".");
		}
		return status;
	}

	@Override
	public boolean seeText(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(expectedValue);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedValue + "\" as text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedValue + "\" as not text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeeText(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.equals(value);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + value + "\" as text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + value + "\" as not text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfListElement(By locator, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.equals(expectedValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfListElement(By locator, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.equals(value)) {
					flgTextFound = true;
					this.log.error("I saw \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
			int size = rows.size();
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(expectedValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						flgTextFound = false;
						this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeeTextFrom.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						boolean isValueEqual = seeText.equals(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if(!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.equals(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								status = false;
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
								status = true;
							}
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialText(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of Web Element: \"" + locator.toString() + "\".");
		boolean isValueEqual = this.seleniumWait.waitForTextToContain(locator, expectedPartialValue);
		String actualText = this.getText(locator).trim();
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see \"" + expectedPartialValue + "\" as not the partial text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialText(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as not the partial text value of Web Element: \"" + locator.toString() + "\".");
		String actualText = this.getText(locator).trim();
		boolean isValueEqual = actualText.contains(partialValue);
		boolean status = false;
		if(isValueEqual) {
			this.log.error("I saw \"" + partialValue + "\" as the partial text value of at Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see \"" + partialValue + "\" as not the partial the text value of Web Element: \"" + locator.toString() + "\". Actual value is \"" + actualText + "\".");
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfListElement(By locator, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.contains(expectedPartialValue)) {
					flgTextFound = true;
					status = true;
					this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfListElement(By locator, String partialValue) {
		this.log.debug("I see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		int size = elements.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator, j);
				String text = element.getText().trim();
				if (text.contains(partialValue)) {
					flgTextFound = true;
					this.log.error("I saw \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + partialValue + "\" as the partial text value of one of the Web Elements from the Web Element List: \"" + locator.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String expectedPartialValue) {
		this.log.debug("I see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeePartialTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(expectedPartialValue)) {
						status = true;
						flgTextFound = true;
						this.log.debug("I saw \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see \"" + expectedPartialValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeePartialTextFrom, j);
				if (elementToSeeTextFrom != null) {
					String seeText = elementToSeeTextFrom.getText().trim();
					if(seeText.equals(value)) {
						flgTextFound = true;
						this.log.error("I saw \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						break;
					} else {
						this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
					}
				} else {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToSeePartialTextFrom.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeePartialTextFrom + "\" in one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						this.log.info("Text found: " + seeText);
						boolean isValueEqual = seeText.equals(expectedValue);
						if(isValueEqual) {
							status = true;
							this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + expectedValue + "\" as the text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value) {
		this.log.debug("I see \"" + value + "\" as not the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.contains(textToCheck)) {
					WebElement elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						String seeText = elementToSeeTextFrom.getText().trim();
						this.log.info("Text found: " + seeText);
						boolean isValueEqual = seeText.equals(value);
						if(isValueEqual) {
							this.log.error("I saw \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						} else {
							this.log.debug("I didn't see \"" + value + "\" as the partial text value of the Web Element: \"" + rowObjectToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								status = true;
								this.log.debug("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
						if (!status) {
							this.log.error("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see partial text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue) {
		this.log.debug("I see \"" + expectedValue + "\" as not the partial text value of the Web Element: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSeeTextFrom = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectListToSeeTextFrom, j);
					if (elementToSeeTextFrom != null) {
						int listSize = elementToSeeTextFrom.size();
						for(int k = 0; k < listSize; k++) {
							String seeText = elementToSeeTextFrom.get(k).getText().trim();
							boolean isValueEqual = seeText.contains(expectedValue);
							if(isValueEqual) {
								this.log.error("I saw \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
								break;
							} else {
								this.log.debug("I didn't see \"" + expectedValue + "\" as the partial text value of one of the Elements in the Web Element List: \"" + rowObjectListToSeeTextFrom.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\". Actual value is \"" + seeText + "\".");
							}
						}
					} else {
						this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" to see text of at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}

	@Override
	public boolean see(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" displayed.");
		this.seleniumWait.waitForObjectToBeVisible(locator);
		List<WebElement> elements = this.seleniumWait.waitForListToBeVisible(locator);
		boolean status = false;
		if (elements.size() > 0) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\".");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\".");
		}
		return status;
	}

	@Override
	public boolean dontSee(By locator) {
		this.log.debug("I see Web Element: \"" + locator.toString() + "\" is not displayed.");
		this.setImplicitWait(2);
		this.seleniumWait.waitForObjectToBeInvisible(locator);
		List<WebElement> elements = this.driver.findElements(locator);
		boolean status = false;
		if (elements.size() > 0) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\".");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\".");
		}
		this.setImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSee = this.seleniumWait.waitForNestedObjectsToBeVisible(rowObjectList, rowObjectToSee, j);
					if (elementToSee.size() > 0) {
						status = true;
						this.log.debug("I saw the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					} else {
						this.log.debug("I didn't see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.error("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		return status;
	}
	
	@Override
	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee) {
		this.log.debug("I see the Web Element: \"" + rowObjectToSee.toString() + "\" to not be displayed within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\" based on the text: \"" + textToCheck + "\" from the Web Element: \"" + rowObjectToCheckText.toString() + "\" within the same row.");
		this.setImplicitWait(2);
		List<WebElement> rows = this.seleniumWait.waitForTableRowsToBeVisible(rowObjectList);
		int size = rows.size();
		boolean flgTextFound = false;
		boolean status = false;
		for(int i = 1; i <= 4; i++) {
			for(int j = 0; j < size; j++) {
				WebElement elementToCheckText = this.seleniumWait.waitForNestedObjectToBeVisible(rowObjectList, rowObjectToCheckText, j);
				String checkText = null;
				if (elementToCheckText == null) {
					this.log.debug("I didn't see the Web Element: \"" +  rowObjectToCheckText.toString() + "\" for checking text at Row \"" + j + "\" of Web Element: \"" + rowObjectList.toString() + "\". Skipping.");
					continue;
				} else {
					checkText = elementToCheckText.getText().trim();
				}
				if (checkText.equals(textToCheck)) {
					List<WebElement> elementToSee = rows.get(j).findElements(rowObjectToSee);
					if (elementToSee.size() > 0) {
						this.log.error("I saw the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					} else {
						status = true;
						this.log.debug("I didn't see the Web Element: \"" + rowObjectToSee.toString() + "\" within one of the Rows of the Web Element: \"" + rowObjectList.toString() + "\".");
					}
					flgTextFound = true;
					break;
				}
			}
			if (!flgTextFound) {
				if(i < 4) {
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\". Retrying " + i + "/3.");
					wait(1);
				} else {
					status = true;
					this.log.debug("I didn't see the text \"" + textToCheck + "\" from the Web Element: \"" +  rowObjectToCheckText.toString() + "\" within one of the Rows of Web Element: \"" + rowObjectList.toString() + "\".");
				}
			} else {
				break;
			}
		}
		this.setImplicitWait(10);
		return status;
	}
	
	@Override
	public boolean seeEnabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" enabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\" enabled.");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\" enabled.");
		}
		return status;
	}

	@Override
	public boolean seeDisabled(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" disabled.");
		WebElement element = this.seleniumWait.waitForObjectToBePresent(locator);
		boolean isEnabled = element.isEnabled();
		boolean status = false;
		if (isEnabled) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\" enabled.");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\" enabled.");
		}
		this.log.info("Status Enabled? " + isEnabled);
		return status;
	}

	@Override
	public boolean selected(By locator) {
		this.log.debug("I see Web Element \"" + locator.toString() + "\" selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, true);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (isSelected) {
			status = true;
			this.log.debug("I saw Web Element: \"" + locator.toString() + "\" selected.");
		} else {
			this.log.error("I didn't see Web Element: \"" + locator.toString() + "\" selected.");
		}
		return status;
	}

	@Override
	public boolean deselected(By locator) {
		this.log.debug("I see element \"" + locator.toString() + "\" not selected.");
		this.seleniumWait.waitForObjectSelectionStateToBe(locator, false);
		WebElement element = this.seleniumWait.waitForObjectToBeVisible(locator);
		boolean isSelected = element.isSelected();
		boolean status = false;
		if (isSelected) {
			this.log.error("I saw Web Element: \"" + locator.toString() + "\" selected.");
		} else {
			status = true;
			this.log.debug("I didn't see Web Element: \"" + locator.toString() + "\" selected.");
		}
		return status;
	}
	
	@Override
	public boolean counted(By locator, int count) {
		this.log.debug("I count Web Element: \"" + locator.toString() + "\".");
		boolean isEqual = this.seleniumWait.waitForCountToBe(locator, count);
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

	@Override
	public boolean seeAlertMessage(String expectedMessage) {
		this.log.debug("I see \"" + expectedMessage + "\" Alert Message displayed.");
		Alert alert = this.seleniumWait.waitForAlertToBePresent();
		String actualMessage = alert.getText().trim();
		boolean isValueEqual = actualMessage.equals(expectedMessage);
		boolean status = false;
		if(isValueEqual) {
			status = true;
			this.log.debug("I saw alert message: \"" + expectedMessage + "\" displayed.");
		} else {
			this.log.error("I didn't see alert message: \"" + expectedMessage + "\" displayed.");
		}
		return status;
	}
}