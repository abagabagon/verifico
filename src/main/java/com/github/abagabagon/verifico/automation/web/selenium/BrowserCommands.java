package com.github.abagabagon.verifico.automation.web.selenium;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class BrowserCommands extends Commands {

	protected WebDriver driver;
	protected Logger log;
	private JavascriptExecutor javascriptExecutor;
	private SeleniumWait seleniumWait;
	
	private enum BrowserAction {
		OPEN_TAB, GO_TO, MAXIMIZE, DELETE_COOKIE, DELETE_ALL_COOKIES, BACK, FORWARD, REFRESH, CLOSE_TAB, CLOSE_BROWSER, SCROLL
	}
	
	private enum SwitchAction {
		BY_TITLE, BY_URL, TO_ORIGINAL
	}
	
	public BrowserCommands(WebDriver driver, JavascriptExecutor javascriptExecutor, SeleniumWait seleniumWait) {
		super(driver, seleniumWait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.javascriptExecutor = javascriptExecutor;
		this.seleniumWait = seleniumWait;
	}

	private void execute(BrowserAction browserAction, String input) {
		this.log.debug("Performing \"" + String.valueOf(browserAction).replace('_', ' ') + "\" Browser Action.");
		
		try {
			switch(browserAction) {
			case OPEN_TAB:
				String link = "window.open('" + input + "', '_blank');";
				this.javascriptExecutor.executeScript(link);
				break;
			case MAXIMIZE:
				this.driver.manage().window().maximize();
				break;
			case DELETE_COOKIE:
				this.driver.manage().deleteCookieNamed(input);
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
	
	private boolean execute(SwitchAction switchAction, String input) {
		this.log.debug("Performing \"SWITCH TAB " + String.valueOf(switchAction).replace('_', ' ') + "\": \"" + input + "\".");
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
	
	/**
	 * Closes Web Browser.
	 */
	
	public final void closeBrowser() {
		this.execute(BrowserAction.CLOSE_BROWSER, null);
	}
	
	/**
	 * Opens Tab.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */
	
	public final void openTab(String url) {
		this.execute(BrowserAction.OPEN_TAB, url);
	}
	
	/**
	 * Closes Tab of a Web Browser.
	 */
	
	public final void closeTab() {
		this.execute(BrowserAction.CLOSE_TAB, null);
	}
	
	/**
	 * Navigates to the Url specified.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */

	public final void goTo(String url) {
		this.execute(BrowserAction.GO_TO, url);
	}
	
	/**
	 * Navigates one item back from the browser's history.
	 */
	
	public final void back() {
		this.execute(BrowserAction.BACK, null);
	}
	
	/**
	 * Navigates one item forward from the browser's history.
	 */

	public final void forward() {
		this.execute(BrowserAction.FORWARD, null);
	}
	
	/**
	 * Refreshes current page.
	 */

	public final void refresh() {
		this.execute(BrowserAction.REFRESH, null);
	}
	
	/**
	 * Maximizes Browser Window.
	 */

	public final void maximize() {
		this.execute(BrowserAction.MAXIMIZE, null);
	}
	
	/**
	 * Delete cookie with the specified name.
	 * 
	 * @param name Name of the Cookie to be deleted.
	 */
	
	public final void deleteCookie(String name) {
		this.execute(BrowserAction.DELETE_COOKIE, name);
	}
	
	/**
	 * Deletes all cookies.
	 */

	public final void deleteAllCookies() {
		this.execute(BrowserAction.DELETE_ALL_COOKIES, null);
	}
	
	/**
	 * Switches to a Tab based on Page Title.
	 * 
	 * @param 	expectedTitle Expected Page Title to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */
	
	public boolean switchTabByTitle(String title) {
		boolean isExisting = this.execute(SwitchAction.BY_TITLE, title);
		return isExisting;
	}
	
	/**
	 * Switches to a Tab based on Page URL.
	 * 
	 * @param 	url Expected Page URL to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */
	
	public boolean switchTabByURL(String url) {
		boolean isExisting = this.execute(SwitchAction.BY_URL, url);
		return isExisting;
	}
	
	/**
	 * Switches back to Original Tab
	 */

	public final void switchTabToOriginal() {
		this.log.debug("Performing \"SWITCH TAB " + String.valueOf(SwitchAction.TO_ORIGINAL).replace('_', ' ') + "\".");
		ArrayList<String> tabs = null;
		try {
			tabs = new ArrayList<String>(this.driver.getWindowHandles());
			this.driver.switchTo().window(tabs.get(0));
		} catch (NullPointerException e) {
			this.log.fatal("Unable to get current browser tabs. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
			if (this.driver == null) {
				System.exit(1);
			}
		} catch (NoSuchWindowException e) {
			this.log.error("Encountered an error while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to switch back to original tab.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Scrolls Page
	 * 
	 * @param pixelHorizontal	Horizontal Pixel Value to scroll to
	 * @param pixelVertical 	Vertical Pixel Value to scroll to
	 */
	
	public final void scroll(String pixelHorizontal, String pixelVertical) {
		this.log.debug("Performing \"" + String.valueOf(BrowserAction.SCROLL).replace('_', ' ') + "\" Browser Action to coordinates \"" + pixelHorizontal + ", " + pixelVertical + "\".");
		String script = "window.scrollBy(" + pixelHorizontal + ", " + pixelVertical + ")";
		try {
			this.javascriptExecutor.executeScript(script);
		} catch (NullPointerException e) {
			this.log.error("Unable to scroll page. Browser might not have been opened or initialized.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			this.log.error("Something went wrong while trying to scroll page.");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
}