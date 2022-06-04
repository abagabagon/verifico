package tests.automation;

import java.time.Duration;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.github.abagabagon.verifico.automation.web.WebDriverFactory;
import com.github.abagabagon.verifico.utilities.OperatingSystem;

import tests.Tests;

public class TestWebDriverFactory extends Tests {

	@Test(groups = "WEBDRIVER", description = "Verify Google Chrome WebDriver initialization", enabled = true)
	void WDF1() {
		WebDriverFactory driverFactory = new WebDriverFactory();
		driverFactory.setChromeDriver();
		WebDriver driver = driverFactory.getChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		softAssert.assertNotNull(driver);
		driver.get(URL);
		softAssert.assertEquals(driver.getCurrentUrl(), URL);
		driver.quit();
		softAssert.assertAll();
	}

	@Test(groups = "WEBDRIVER", description = "Verify Mozilla Firefox WebDriver initialization", enabled = true)
	void WDF2() {
		WebDriverFactory driverFactory = new WebDriverFactory();
		driverFactory.setFirefoxDriver();
		WebDriver driver = driverFactory.getFirefoxDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		softAssert.assertNotNull(driver);
		driver.get(URL);
		softAssert.assertEquals(driver.getCurrentUrl(), URL);
		driver.quit();
		softAssert.assertAll();
	}

	@Test(groups = "WEBDRIVER", description = "Verify Safari WebDriver initialization", enabled = true)
	void WDF3() {
		Platform operatingSystem = OperatingSystem.getOS();
		if (operatingSystem == Platform.MAC) {
			WebDriverFactory driverFactory = new WebDriverFactory();
			driverFactory.setSafariDriver();
			WebDriver driver = driverFactory.getSafariDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			softAssert.assertNotNull(driver);
			driver.get(URL);
			softAssert.assertEquals(driver.getCurrentUrl(), URL);
			driver.quit();
			softAssert.assertAll();
		} else {
			throw new SkipException("Current Operating System being used is not Mac OS. Skipping Test.");
		}

	}

	@Test(groups = "WEBDRIVER", description = "Verify Microsoft Edge WebDriver initialization", enabled = true)
	void WDF4() {
		WebDriverFactory driverFactory = new WebDriverFactory();
		driverFactory.setEdgeDriver();
		WebDriver driver = driverFactory.getEdgeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		softAssert.assertNotNull(driver);
		driver.get(URL);
		softAssert.assertEquals(driver.getCurrentUrl(), URL);
		driver.quit();
		softAssert.assertAll();
	}

	@Test(groups = "WEBDRIVER", description = "Verify Internet Explorer WebDriver initialization", enabled = true)
	void WDF5() {
		Platform operatingSystem = OperatingSystem.getOS();
		if (operatingSystem == Platform.WINDOWS) {
			WebDriverFactory driverFactory = new WebDriverFactory();
			driverFactory.setIEDriver();
			WebDriver driver = driverFactory.getIeDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			softAssert.assertNotNull(driver);
			driver.get(URL);
			softAssert.assertEquals(driver.getCurrentUrl(), URL);
			driver.quit();
			softAssert.assertAll();
		} else {
			throw new SkipException("Current Operating System being used is not Windows. Skipping Test.");
		}

	}

}
