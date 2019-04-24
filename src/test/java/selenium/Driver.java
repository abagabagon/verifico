/*******************************************************************************
 * @name    Driver
 * @author  Arnel L. Bagabagon Jr.
 * @version 0.1
 * @since   02/06/2019
 * 
 * This Class contains all the common functionalities for Selenium WebDriver
 * related methods including EventFiringWebDriver for stand-alone Test Execution
 * or Selenium Grid Parallel Test Execution.
 * 
 ******************************************************************************/

package selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class Driver {
	
	private static WebDriver driver;
	private static EventFiringWebDriver eDriver;
	private static DesiredCapabilities dc;
	private static Logger log;
	
	static {
		log = LogManager.getLogger(Utilities.class);
	}
	
	public enum BrowserName {
		GoogleChrome, MozillaFirefox, MicrosoftEdge
	}

	/**
	 * Initializes and returns Google Chrome WebDriver Object.
	 * 
	 * @return Google Chrome WebDriver Object
	 */
	
	private static WebDriver getGoogleChromeDriver() {
		log.trace("Setting Property of Google Chrome Driver.");
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		log.trace("Successfully set Property of Google Chrome Driver.");
		log.trace("Initializing Google Chrome Driver.");
		driver = new ChromeDriver();
		log.trace("Successfully initialized Google Chrome Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Mozilla Firefox WebDriver Object.
	 * 
	 * @return Mozilla Firefox WebDriver Object
	 */

	private static WebDriver getMozillaFirefoxDriver() {
		log.trace("Setting Property of Mozilla Firefox Driver.");
		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
		log.trace("Successfully set Property of Mozilla Firefox Driver.");
		log.trace("Initializing Mozilla Firefox Driver.");
		driver = new FirefoxDriver();
		log.trace("Successfully initialized Mozilla Firefox Driver.");
		return driver;
	}
	
	/**
	 * Initializes and returns Microsoft Edge WebDriver Object.
	 * 
	 * @return Microsoft Edge WebDriver Object
	 */

	private static WebDriver getMicrosoftEdgeDriver() {
		log.trace("Setting Property of Microsoft Edge Driver.");
		System.setProperty("webdriver.ie.driver", "./drivers/MicrosoftEdgeDriver.exe");
		log.trace("Successfully set Property of Microsoft Edge Driver.");
		log.trace("Initializing Microsoft Edge Driver.");
		driver = new InternetExplorerDriver();
		log.trace("Successfully initialized Microsoft Edge Driver.");
		return driver;
	}
	
	/**
	 * Sets the Browser Name Desired Capability for Selenium Grid.
	 * 
	 * @param browserName Desired Browser Name Capability (enumerated).
	 */

	private static final void setBrowserName(BrowserName browserName) {
		if (browserName == BrowserName.GoogleChrome) {
			log.trace("Setting \"Browser Name\" Desired Capability to Google Chrome.");
			dc.setBrowserName("chrome");
			log.trace("Successfully set \"Browser Name\" Desired Capability to Google Chrome.");
		} else if (browserName == BrowserName.MozillaFirefox) {
			log.trace("Setting \"Browser Name\" Desired Capability to Mozilla Firefox.");
			dc.setBrowserName("firefox");
			log.trace("Successfully set \"Browser Name\" Desired Capability to Mozilla Firefox.");
		} else if (browserName == BrowserName.MicrosoftEdge) {
			log.trace("Setting \"Browser Name\" Desired Capability to Microsoft Edge.");
			dc.setBrowserName("MicrosoftEdge");
			log.trace("Successfully set \"Browser Name\" Desired Capability to Microsoft Edge.");
		} else {
			log.fatal("Invalid Browser Name set!");
		}
	}
	
	/**
	 * Sets the Platform Desired Capability for Selenium Grid.
	 * 
	 * @param platform Desired Platform Capability (enumerated).
	 */

	private static final void setPlatform(Platform platform) {
		if (platform == Platform.WINDOWS) {
			log.trace("Setting \"Platform\" Desired Capability to WINDOWS.");
			dc.setPlatform(Platform.WINDOWS);
			log.trace("Successfully set \"Platform\" Desired Capability to WINDOWS.");
		} else if (platform == Platform.LINUX) {
			log.trace("Setting \"Platform\" Desired Capability to LINUX.");
			dc.setPlatform(Platform.LINUX);
			log.trace("Successfully set \"Platform\" Desired Capability to LINUX.");
		} else if (platform == Platform.MAC) {
			log.trace("Setting \"Platform\" Desired Capability to MAC.");
			dc.setPlatform(Platform.MAC);
			log.trace("Successfully set \"Platform\" Desired Capability to MAC.");
		} else if (platform == Platform.UNIX) {
			log.trace("Setting \"Platform\" Desired Capability to UNIX.");
			dc.setPlatform(Platform.UNIX);
			log.trace("Successfully set \"Platform\" Desired Capability to UNIX.");
		}  else {
			log.fatal("Invalid Platform set!");
		}
	}
	
	/**
	 * Initializes WebDriver depending on the Web Browser set. The initialized
	 * WebDriver is registered with EventFiringWebDriver in order to utilize
	 * WebDriverEventListener logging.
	 * 
	 * @param  browserName Desired WebBrowser to be used.
	 * @return EventFiringWebDriver Object registered with the initialized WebDriver
	 */

	public static final WebDriver getWebDriver(String browserName) {
		log.trace("Initializing Selenium Web Driver.");
		browserName = browserName.toLowerCase().trim();
		if (browserName.equals(BrowserName.GoogleChrome.toString().toLowerCase())) {
			log.debug("Opening Google Chrome Web Browser.");
			driver = getGoogleChromeDriver();
			log.trace("Successfully opened Google Chrome Web Browser.");
		} else if (browserName.equals(BrowserName.MozillaFirefox.toString().toLowerCase())) {
			log.debug("Opening Mozilla Firefox Web Browser.");
			driver = getMozillaFirefoxDriver();
			log.trace("Successfully opened Mozilla Firefox Web Browser.");
		} else if (browserName.equals(BrowserName.MicrosoftEdge.toString().toLowerCase())) {
			log.debug("Opening Microsoft Edge Web Browser.");
			driver = getMicrosoftEdgeDriver();
			log.trace("Successfully opened Microsoft Edge Web Browser.");
		} else {
			log.fatal("Invalid Web Browser set!");
		}
		log.trace("Maximizing Web Browser Window.");
		driver.manage().window().maximize();
		log.trace("Successfully maximized Web Browser Window.");
		log.trace("Deleting all cookies.");
		driver.manage().deleteAllCookies();
		log.trace("Successfully deleted all cookies.");
		return driver;
	}
	
	/**
	 * Initializes WebDriver for Selenium Grid depending on the Desired
	 * Capabilities set. The initialized WebDriver is registered with
	 * EventFiringWebDriver in order to utilize WebDriverEventListener
	 * logging.
	 * 
	 * @param  browserName Desired Browser Name Capability (enumerated).
	 * @param  platform    Desired Platform Capability (enumerated).
	 * @param  gridHubUrl  Url of designated Selenium Grid Hub
	 * @return EventFiringWebDriver Object registered with the initialized WebDriver
	 */

	public static final EventFiringWebDriver getWebDriverForGrid(BrowserName browserName, Platform platform, String gridHubUrl) {
		log.trace("Initializing Selenium Web Driver.");
		dc = new DesiredCapabilities();
		log.trace("Setting \"Browser Name\" Desired Capability.");
		setBrowserName(browserName);
		log.trace("Setting \"Platform\" Desired Capability.");
		setPlatform(platform);
		try {
			driver = new RemoteWebDriver(new URL(gridHubUrl), dc);
		} catch (MalformedURLException e) {
			log.fatal("Encountered MalformedURLException while initializing WebDriver for Selenium Grid!");
		} catch (Exception e) {
			log.fatal("Encountered Exception while initializing WebDriver for Selenium Grid!");
		}
		eDriver = new EventFiringWebDriver(driver);
		log.trace("Maximizing Web Browser Window.");
		eDriver.manage().window().maximize();
		log.trace("Successfully maximized Web Browser Window.");
		log.trace("Deleting all cookies.");
		eDriver.manage().deleteAllCookies();
		log.trace("Successfully deleted all cookies.");
		return eDriver;
	}
}
