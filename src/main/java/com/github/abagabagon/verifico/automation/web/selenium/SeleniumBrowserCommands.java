package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class SeleniumBrowserCommands extends SeleniumCommands {

	protected WebDriver driver;
	protected Logger log;
	private JavascriptExecutor javascriptExecutor;
	private SeleniumWait seleniumWait;
	
	public SeleniumBrowserCommands(WebDriver driver, JavascriptExecutor javascriptExecutor, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.javascriptExecutor = javascriptExecutor;
		this.seleniumWait = seleniumWait;
	}

	void execute(BrowserAction browserAction, String input) {
		this.log.debug("I open New Tab.");
		try {
			switch(browserAction) {
			case OPEN_TAB:
				String link = "window.open('" + input + "', '_blank');";
				this.javascriptExecutor.executeScript(link);
				break;
			case MAXIMIZE:
				this.driver.manage().window().maximize();
				break;
			case DELETE_ALL_COOKIES:
				this.driver.manage().deleteAllCookies();
				break;
			case GO_TO:
				this.driver.get(input);
				this.seleniumWait.waitForPage();
				break;
			case BACK:
				this.driver.navigate().back();
				this.seleniumWait.waitForPage();
				break;
			case FORWARD:
				this.driver.navigate().forward();
				this.seleniumWait.waitForPage();
				break;
			case REFRESH:
				this.driver.navigate().refresh();
				this.seleniumWait.waitForPage();
				break;
			case CLOSE_TAB:
				this.driver.close();
				break;
			case CLOSE_BROWSER:
				this.driver.quit();
				break;
			default:
				this.log.fatal("Unsupported Browser Action.");
			}
		} catch (NullPointerException e) {
			this.log.fatal("Unable to perform \"" + String.valueOf(browserAction) + "\". Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (TimeoutException e) {
			this.log.fatal("Wait time to perform \"" + String.valueOf(browserAction) + "\" has expired.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to perform \"" + String.valueOf(browserAction) + "\" has expired.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	
	boolean executeSwitchTab(SwitchAction switchAction, String input) {
		this.log.debug("I switch to Tab with Page URL/Title: \"" + input + "\".");
		boolean isExisting = false;
		for(int i = 1; i <= 4; i++) {
			Set<String> windows = this.driver.getWindowHandles();
			for (String windowId: windows) {
				this.driver.switchTo().window(windowId);
				try {
					switch(switchAction) {
					case BY_TITLE:
						String currentTitle = this.driver.getTitle();
						if(currentTitle.equals(input)) {
							isExisting = true;
							this.log.debug("Successfully switched to Tab with Title: \"" + input + "\".");
							break;
						}
					case BY_URL:
						String currentUrl = this.driver.getCurrentUrl();
						if(currentUrl.equals(input)) {
							isExisting = true;
							this.log.debug("Successfully switched to Tab with URL: \"" + input + "\".");
							break;
						}
						break;
					default:
						this.log.fatal("Unsupported Switch Action.");
					}

				} catch (NoSuchWindowException e) {
					this.log.error("Tab with URL/Title: \"" + input + "\" could not be found. Please check if provided Page Title is correct.");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				} catch (Exception e) {
					this.log.error("Something went wrong while trying to switch tab by URL/Title: \"" + input + "\".");
					this.log.debug(ExceptionUtils.getStackTrace(e));
				}
			}
			if (!isExisting) {
				if(i < 4) {
					this.log.debug("Tab with the Page URL/Title: \"" + input + "\" was not found. Retrying " + i + "/3.");
					wait(1);
				} else {
					this.log.debug("Tab with the Page URL/Title: \"" + input + "\" was not found.");
				}
			} else {
				break;
			}
		}
		return isExisting;
	}
	
}