package automation.web.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
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
import org.openqa.selenium.support.events.EventFiringWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.OperatingSystem;

public class SeleniumWebDriver {
	
	private Logger log;
	private WebDriver driver;
	private EventFiringWebDriver eDriver;
	
	public SeleniumWebDriver() {
		this.log = LogManager.getLogger(this.getClass());
		this.log.debug("Initializing SeleniumWebDriver Class.");
		this.log.debug("Successfully initialized SeleniumWebDriver Class.");
	}
	
	/**
	 * Initializes and returns EventFiringWebDriver Object.
	 * 
	 * @return EventFiringWebDriver Object
	 */
	
	protected EventFiringWebDriver getEventFiringWebDriver() {
		this.log.debug("Initializing EventFiringWebDriver.");
		this.eDriver = new EventFiringWebDriver(this.driver);
		this.log.debug("Successfully initialized EventFiringWebDriver.");
		return this.eDriver;
	}
	
	/**
	 * Initializes and returns Google Chrome WebDriver Object.
	 * 
	 * @return Google Chrome WebDriver Object
	 */
	
	protected WebDriver getChromeDriver() {
		this.log.debug("Initializing Google Chrome Driver.");
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = this.setChromeDriverOptions(); 
		this.driver = new ChromeDriver(options);
		this.log.debug("Successfully initialized Google Chrome Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns Mozilla Firefox WebDriver Object.
	 * 
	 * @return Mozilla Firefox (Gecko) WebDriver Object
	 */
	
	protected WebDriver getFirefoxDriver() {
		this.log.debug("Initializing Mozilla Firefox Driver.");
		WebDriverManager.firefoxdriver().setup();
		FirefoxOptions options = this.setFirefoxDriverOptions();
		this.driver = new FirefoxDriver(options);
		this.log.debug("Successfully initialized Mozilla Firefox Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns Opera WebDriver Object.
	 * 
	 * @return Opera WebDriver Object
	 */
	
	protected WebDriver getOperaDriver() {
		this.log.debug("Initializing Opera Driver.");
		WebDriverManager.operadriver().setup();
		this.driver = new OperaDriver();
		this.log.debug("Successfully initialized Opera Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns Microsoft Edge WebDriver Object.
	 * 
	 * @return Microsoft Edge WebDriver Object
	 */
	
	protected WebDriver getEdgeDriver() {
		this.log.debug("Initializing Microsoft Edge Driver.");
		WebDriverManager.edgedriver().setup();
		this.driver = new EdgeDriver();
		this.log.debug("Successfully initialized Microsoft Edge Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns Internet Explorer WebDriver Object.
	 * 
	 * @return Internet Explorer WebDriver Object
	 */
	
	protected WebDriver getIEDriver() {
		this.log.debug("Initializing Internet Explorer Driver.");
		WebDriverManager.iedriver().setup();
		InternetExplorerOptions options = this.setInternetExplorerDriverOptions();
		this.driver = new InternetExplorerDriver(options);
		this.log.debug("Successfully initialized Internet Explorer Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns Safari WebDriver Object.
	 * 
	 * @return Safari WebDriver Object
	 */

	protected WebDriver getSafariDriver() {
		this.log.debug("Setting Property of Safari Driver.");
		Platform operatingSystem = OperatingSystem.getOS();
		
		switch(operatingSystem) {
		case MAC:
			break;
		default:
			this.log.fatal("Unsupported Operating System. Please report issue to QA Team.");
		}

		this.log.debug("Initializing Safari Driver.");
		this.driver = new SafariDriver();
		this.log.debug("Successfully initialized Safari Driver.");
		return this.driver;
	}
	
	/**
	 * Initializes and returns PhantomJS WebDriver Object.
	 * 
	 * @return PhantomJS WebDriver Object
	 */

	protected WebDriver getPhantomJSDriver() {
		this.log.debug("Initializing PhantomJS Driver.");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);                
		caps.setCapability("takesScreenshot", true);  
		WebDriverManager.phantomjs().setup();
		this.driver = new PhantomJSDriver(caps);
		this.log.debug("PhantomJSDriver: " + this.driver);
		this.log.debug("Successfully initialized PhantomJS Driver.");
		return this.driver;
	}
	
	/**
	 * Sets ChromeOptions for ChromeDriver.
	 * 
	 * @return ChromeOptions
	 */
	
	private ChromeOptions setChromeDriverOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		return options;
    }
	
	/**
	 * Sets FirefoxOptions for FirefoxDriver
	 * 
	 * @return FirefoxOptions
	 */
	
	private FirefoxOptions setFirefoxDriverOptions() {
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability(CapabilityType.HAS_NATIVE_EVENTS, false);
		return options;
	}
	
	/**
	 * Sets InternetExplorerOptions for InternetExplorerDriver
	 * 
	 * @return InternetExplorerOptions
	 */

	private InternetExplorerOptions setInternetExplorerDriverOptions() {
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
