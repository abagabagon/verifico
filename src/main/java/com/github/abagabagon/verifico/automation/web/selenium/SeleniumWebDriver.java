package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.github.abagabagon.verifico.enums.Browser;
import com.github.abagabagon.verifico.utilities.OperatingSystem;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Appium implemented Web Driver Commands
 * 
 * @author albagabagon
 *
 */

public class SeleniumWebDriver {
	
	private Logger log;
	private WebDriver driver;
	
	public SeleniumWebDriver() {
		this.log = LogManager.getLogger(this.getClass());
	}
	
	WebDriver getWebDriver(Browser browser, boolean isHeadless) {
		this.log.trace("Initializing Selenium Web Driver.");
		try {
			if(isHeadless) {
				switch (browser) {
				case CHROME:
					this.driver = this.getChromeDriver(true);
					break;
				case FIREFOX:
					this.driver = this.getFirefoxDriver(true);
					break;
				case PHANTOMJS:
					this.driver = this.getPhantomJSDriver();
					break;
				default:
					this.log.fatal("Unsupported Web Browser or Headless Browsing is Unsupported by Web Browser.");
					System.exit(1);
				}
			} else {
				switch (browser) {
				case PHANTOMJS:
					this.driver = this.getPhantomJSDriver();
					break;
				case CHROME:
					this.driver = this.getChromeDriver(false);
					break;
				case SAFARI:
					this.driver = this.getSafariDriver();
					break;
				case FIREFOX:
					this.driver = this.getFirefoxDriver(false);
					break;
				case OPERA:
					this.driver = this.getOperaDriver();
					break;
				case EDGE:
					this.driver = this.getEdgeDriver();
					break;
				case IE:
					this.driver = this.getIEDriver();
					break;
				default:
					this.log.fatal("Unsupported Web Browser.");
					System.exit(1);
				}
			}
		} catch (WebDriverException e) {
			this.log.fatal("Unable to initialize Selenium Web Driver for " + browser + ".");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
			System.exit(1);
		} catch (Exception e) {
			this.log.fatal("Something went wrong while trying to initialize Selenium Web Driver for " + browser + ".");
			this.log.fatal(ExceptionUtils.getStackTrace(e));
			System.exit(1);
		}
		
		return this.driver;
	}
	
	/**
	 * Initializes and returns Google Chrome WebDriver Object.
	 * 
	 * @param  isHeadless Set driver on headless mode.
	 * @return Google Chrome WebDriver Object
	 */
	
	private WebDriver getChromeDriver(boolean isHeadless) {
		this.log.trace("Initializing Google Chrome Driver.");
		WebDriver driver;
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = setChromeDriverOptions(isHeadless); 
		driver = new ChromeDriver(options);
		this.log.trace("Successfully initialized Google Chrome Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Mozilla Firefox WebDriver Object.
	 * 
	 * @param  isHeadless Set driver on headless mode.
	 * @return Mozilla Firefox (Gecko) WebDriver Object
	 */
	
	private WebDriver getFirefoxDriver(boolean isHeadless) {
		this.log.trace("Initializing Mozilla Firefox Driver.");
		WebDriver driver;
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = setFirefoxDriverOptions(isHeadless);
		driver = new FirefoxDriver(options);
		this.log.trace("Successfully initialized Mozilla Firefox Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Opera WebDriver Object.
	 * 
	 * @return Opera WebDriver Object
	 */
	
	private WebDriver getOperaDriver() {
		this.log.trace("Initializing Opera Driver.");
		WebDriver driver;
		WebDriverManager.operadriver().setup();
		driver = new OperaDriver();
		this.log.trace("Successfully initialized Opera Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Microsoft Edge WebDriver Object.
	 * 
	 * @return Microsoft Edge WebDriver Object
	 */
	
	private WebDriver getEdgeDriver() {
		this.log.trace("Initializing Microsoft Edge Driver.");
		WebDriver driver;
		WebDriverManager.edgedriver().setup();
		driver = new EdgeDriver();
		this.log.trace("Successfully initialized Microsoft Edge Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Internet Explorer WebDriver Object.
	 * 
	 * @return Internet Explorer WebDriver Object
	 */
	
	private WebDriver getIEDriver() {
		this.log.trace("Initializing Internet Explorer Driver.");
		WebDriver driver;
		WebDriverManager.iedriver().setup();
		InternetExplorerOptions options = setInternetExplorerDriverOptions();
		driver = new InternetExplorerDriver(options);
		this.log.trace("Successfully initialized Internet Explorer Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Safari WebDriver Object.
	 * 
	 * @return Safari WebDriver Object
	 */

	private WebDriver getSafariDriver() {
		this.log.trace("Setting Property of Safari Driver.");
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case MAC:
			break;
		default:
			this.log.fatal("Unsupported Operating System. Please report issue to QA Team.");
		}

		this.log.trace("Initializing Safari Driver.");
		WebDriver driver = new SafariDriver();
		this.log.trace("Successfully initialized Safari Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns PhantomJS WebDriver Object.
	 * 
	 * @return PhantomJS WebDriver Object
	 */

	private WebDriver getPhantomJSDriver() {
		this.log.trace("Initializing PhantomJS Driver.");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);                
		caps.setCapability("takesScreenshot", true);  
		WebDriverManager.phantomjs().setup();
		WebDriver driver = new PhantomJSDriver(caps);
		this.log.trace("Successfully initialized PhantomJS Driver.");
		return driver;
	}
	
	/**
	 * Sets ChromeOptions for ChromeDriver.
	 * 
	 * @param  isHeadless Set option on headless mode.
	 * @return ChromeOptions
	 */
	
	private static ChromeOptions setChromeDriverOptions(boolean isHeadless) {
		ChromeOptions options = new ChromeOptions();
		if(isHeadless) {
			options.addArguments("--headless");
		}
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-gpu");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--ignore-certificate-errors");
		return options;
    }
	
	/**
	 * Sets FirefoxOptions for FirefoxDriver
	 * 
	 * @param  isHeadless Set option on headless mode.
	 * @return FirefoxOptions
	 */
	
	private static FirefoxOptions setFirefoxDriverOptions(boolean isHeadless) {
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
		options.setHeadless(isHeadless);
		return options;
	}
	
	/**
	 * Sets InternetExplorerOptions for InternetExplorerDriver
	 * 
	 * @return InternetExplorerOptions
	 */

	private static InternetExplorerOptions setInternetExplorerDriverOptions() {
		InternetExplorerOptions options = new InternetExplorerOptions();
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
		options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
		options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
		options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
		options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		return options;
    }

}
