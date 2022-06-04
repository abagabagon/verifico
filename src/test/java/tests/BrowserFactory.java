package tests;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.SkipException;

import com.github.abagabagon.verifico.automation.web.WebDriverFactory;
import com.github.abagabagon.verifico.utilities.OperatingSystem;

public class BrowserFactory {

	private Logger log;
	private WebDriver driver;
	private Platform platform;
	private WebDriverFactory driverFactory;

	public BrowserFactory() {
		this.log = LogManager.getLogger(this.getClass());
		driverFactory = new WebDriverFactory();
	}

	public final WebDriver getWebDriver(String browser) {
		this.log.trace("Initializing Selenium Web Driver.");
		try {
			switch (browser) {
			case "CHROME":
				this.driverFactory.setChromeDriver();
				this.driver = this.driverFactory.getChromeDriver();
				break;
			case "SAFARI":
				this.platform = OperatingSystem.getOS();
				if (platform == Platform.MAC) {
					this.driverFactory.setSafariDriver();
					this.driver = this.driverFactory.getSafariDriver();
				} else {
					throw new SkipException("Current Operating System being used is not Mac OS. Skipping Test.");
				}
				break;
			case "FIREFOX":
				this.driverFactory.setFirefoxDriver();
				this.driver = this.driverFactory.getFirefoxDriver();
				break;
			case "EDGE":
				this.driverFactory.setEdgeDriver();
				this.driver = this.driverFactory.getEdgeDriver();
				break;
			case "IE":
				this.platform = OperatingSystem.getOS();
				if (platform == Platform.WINDOWS) {
					this.driverFactory.setIEDriver();
					this.driver = this.driverFactory.getIeDriver();
				} else {
					throw new SkipException("Current Operating System being used is not Windows. Skipping Test.");
				}
				break;
			default:
				this.log.fatal("Unsupported Web Browser.");
				System.exit(1);
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

}
