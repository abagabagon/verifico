package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.utilities.OperatingSystem;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumWebDriver {
	
	private Logger log;
	private WebDriver chromeDriver;
	private WebDriver firefoxDriver;
	private WebDriver edgeDriver;
	private WebDriver ieDriver;
	private WebDriver safariDriver;
	
	public SeleniumWebDriver() {
		this.log = LogManager.getLogger(this.getClass());
	}
	
	/**
	 * Sets/initializes Chrome WebDriver.
	 */
	
	public final void setChromeDriver() {
		this.log.trace("Initializing " + String.valueOf(Browser.CHROME) + " WebDriver.");
		WebDriverManager.chromedriver().setup();
		this.chromeDriver = new ChromeDriver();
		this.log.trace("Successfully initialized " + String.valueOf(Browser.CHROME) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Chrome WebDriver with specified Options.
	 * 
	 * @param options User-specified ChromeOptions for Chrome WebDriver.
	 */
	
	public final void setChromeDriver(ChromeOptions options) {
		this.log.trace("Initializing " + String.valueOf(Browser.CHROME) + " WebDriver.");
		WebDriverManager.chromedriver().setup();
		this.chromeDriver = new ChromeDriver(options);
		this.log.trace("Successfully initialized " + String.valueOf(Browser.CHROME) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Firefox WebDriver.
	 */
	
	public final void setFirefoxDriver() {
		this.log.trace("Initializing " + String.valueOf(Browser.FIREFOX) + " WebDriver.");
		WebDriverManager.firefoxdriver().setup();
		this.firefoxDriver = new FirefoxDriver();
		this.log.trace("Successfully initialized " + String.valueOf(Browser.FIREFOX) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Firefox WebDriver with specified Options.
	 * 
	 * @param options User-specified FirefoxOptions for Firefox WebDriver.
	 */
	
	public final void setFirefoxDriver(FirefoxOptions options) {
		this.log.trace("Initializing " + String.valueOf(Browser.FIREFOX) + " WebDriver.");
		WebDriverManager.firefoxdriver().setup();
		this.firefoxDriver = new FirefoxDriver(options);
		this.log.trace("Successfully initialized " + String.valueOf(Browser.FIREFOX) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Microsoft Edge WebDriver.
	 */
	
	public final void setEdgeDriver() {
		this.log.trace("Initializing " + String.valueOf(Browser.EDGE) + " WebDriver.");
		WebDriverManager.edgedriver().setup();
		this.edgeDriver = new EdgeDriver();
		this.log.trace("Successfully initialized " + String.valueOf(Browser.EDGE) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Microsoft Edge WebDriver with specified Options.
	 * 
	 * @param options User-specified EdgeOptions for Microsoft Edge WebDriver.
	 */
	
	public final void setEdgeDriver(EdgeOptions options) {
		this.log.trace("Initializing " + String.valueOf(Browser.EDGE) + " WebDriver.");
		WebDriverManager.edgedriver().setup();
		this.edgeDriver = new EdgeDriver(options);
		this.log.trace("Successfully initialized " + String.valueOf(Browser.EDGE) + " WebDriver.");
	}
	
	/**
	 * Sets/initializes Internet Explorer WebDriver.
	 * 
	 * Note: Can be run only on Windows.
	 */
	
	public final void setIEDriver() {
		this.log.trace("Initializing " + String.valueOf(Browser.IE) + " WebDriver.");		
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case WINDOWS:
			WebDriverManager.iedriver().setup();
			this.ieDriver = new InternetExplorerDriver();
			this.log.trace("Successfully initialized " + String.valueOf(Browser.IE) + " WebDriver.");
			break;
		default:
			this.log.fatal("Unsupported Operating System for running automation using " + String.valueOf(Browser.IE) + " String.valueOf(Browser. Please report issue to Automation Team.");
		}
	}
	
	/**
	 * Sets/initializes Internet Explorer WebDriver with specified Options.
	 * 
	 * Note: Can be run only on Windows.
	 * 
	 * @param options User-specified InternetExplorerOptions for Internet Explorer WebDriver.
	 */
	
	public final void setIEDriver(InternetExplorerOptions options) {
		this.log.trace("Initializing " + String.valueOf(Browser.IE) + " WebDriver.");
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case WINDOWS:
			WebDriverManager.iedriver().setup();
			this.ieDriver = new InternetExplorerDriver(options);
			this.log.trace("Successfully initialized " + String.valueOf(Browser.IE) + " WebDriver.");
			break;
		default:
			this.log.fatal("Unsupported Operating System for running automation using " + String.valueOf(Browser.IE) + " String.valueOf(Browser. Please report issue to Automation Team.");
		}
	}
	
	/**
	 * Sets/initializes Safari WebDriver.
	 * 
	 * Note: Can be run only on MacOS.
	 */
	
	public final void setSafariDriver() {
		this.log.trace("Initializing " + String.valueOf(Browser.SAFARI) + " WebDriver.");
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case MAC:
			this.safariDriver = new SafariDriver();
			this.log.trace("Successfully initialized " + String.valueOf(Browser.SAFARI) + " WebDriver.");
			break;
		default:
			this.log.fatal("Unsupported Operating System for running automation using " + String.valueOf(Browser.SAFARI) + " String.valueOf(Browser. Please report issue to Automation Team.");
		}
	}
	
	/**
	 * Sets/initializes Safari WebDriver with specified Options.
	 * 
	 * Note: Can be run only on MacOS.
	 * 
	 * @param options User-specified SafariOptions for Safari WebDriver.
	 */
	
	public final void setSafariDriver(SafariOptions options) {
		this.log.trace("Initializing " + String.valueOf(Browser.SAFARI) + " WebDriver.");
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case MAC:
			this.safariDriver = new SafariDriver(options);
			this.log.trace("Successfully initialized " + String.valueOf(Browser.SAFARI) + " WebDriver.");
			break;
		default:
			this.log.fatal("Unsupported Operating System for running automation using " + String.valueOf(Browser.SAFARI) + " String.valueOf(Browser. Please report issue to Automation Team.");
		}
	}
	
	/**
	 * Gets Chrome WebDriver.
	 * 
	 * Note: Make sure Chrome WebDriver is initialized first before getting.
	 * 
	 * @return Google Chrome WebDriver
	 */
	
	public final WebDriver getChromeDriver() {
		this.log.trace("Getting " + String.valueOf(Browser.CHROME) + " WebDriver.");
		this.checkNullWebDriver(Browser.CHROME, this.chromeDriver);
		return this.chromeDriver;
	}
	
	/**
	 * Gets Firefox WebDriver.
	 * 
	 * Note: Make sure Firefox WebDriver is initialized first before getting.
	 * 
	 * @return Firefox WebDriver
	 */
	
	public final WebDriver getFirefoxDriver() {
		this.log.trace("Getting " + String.valueOf(Browser.FIREFOX) + " WebDriver.");
		this.checkNullWebDriver(Browser.FIREFOX, this.firefoxDriver);
		return this.firefoxDriver;
	}
	
	/**
	 * Gets Microsoft Edge WebDriver.
	 * 
	 * Note: Make sure Microsoft Edge Driver is initialized first before getting.
	 * 
	 * @return Microsoft Edge WebDriver
	 */
	
	public final WebDriver getEdgeDriver() {
		this.log.trace("Getting " + String.valueOf(Browser.EDGE) + " WebDriver.");
		this.checkNullWebDriver(Browser.EDGE, this.edgeDriver);
		return this.edgeDriver;
	}
	
	/**
	 * Gets Internet Explorer WebDriver.
	 * 
	 * Note: Make sure Internet Explorer WebDriver is initialized first before getting.
	 * 
	 * @return Internet Explorer WebDriver
	 */
	
	public final WebDriver getIeDriver() {
		this.log.trace("Getting " + String.valueOf(Browser.IE) + " WebDriver.");
		this.checkNullWebDriver(Browser.IE, this.ieDriver);
		return this.ieDriver;
	}
	
	/**
	 * Gets Safari WebDriver.
	 * 
	 * Note: Make sure Safari WebDriver is initialized first before getting.
	 * 
	 * @return Safari WebDriver
	 */
	
	public final WebDriver getSafariDriver() {
		this.log.trace("Getting " + String.valueOf(Browser.SAFARI) + " WebDriver.");
		this.checkNullWebDriver(Browser.SAFARI, this.safariDriver);
		return this.safariDriver;
	}
	
	private void checkNullWebDriver(Browser browser, WebDriver driver) {
		if (driver == null) {
			this.log.fatal(String.valueOf(browser) + " Driver still not initialized. Initialize first before getting the WebDriver.");
			System.exit(1);
		}
	}

}
