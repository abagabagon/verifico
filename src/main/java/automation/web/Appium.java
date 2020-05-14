package automation.web;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriverException;

import enums.Browser;
import enums.Mobile;

public class Appium extends Selenium {

	private AppiumWebDriver appiumWebDriver;
	private Mobile mobile;
	private String platformVersion;
	private String deviceName;
	
	public Appium(Mobile mobile, URL appiumServerUrl, String platformVersion, String deviceName) {
		this.log = LogManager.getLogger(this.getClass());
		this.log.debug("Initializing AppiumWebAutomation Class.");
		this.appiumWebDriver = new AppiumWebDriver(appiumServerUrl);
		this.seleniumWait = new SeleniumWait(this.wait);
		this.mobile = mobile;
		this.platformVersion = platformVersion;
		this.deviceName = deviceName;
		this.log.debug("Successfully initialized AppiumWebAutomation Class.");
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
			case CHROME:
				this.driver = this.appiumWebDriver.getChromeDriver(this.mobile, this.platformVersion, this.deviceName);
				break;
			case SAFARI:
				this.driver = this.appiumWebDriver.getSafariDriver(this.mobile, this.platformVersion, this.deviceName);
				break;
			default:
				this.log.fatal("Unsupported Web Browser.");
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
		this.driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
		this.initializeImplicitWait(20);
		this.initializeExplicitWait(20);
		deleteAllCookies();
	}

}